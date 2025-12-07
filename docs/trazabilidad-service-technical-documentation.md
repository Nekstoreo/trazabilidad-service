# Documentación Técnica: Microservicio de Trazabilidad

Este documento describe la arquitectura, diseño e implementación del microservicio de trazabilidad (`trazabilidad-service`). El servicio es responsable del registro histórico de cambios de estado de pedidos y el cálculo de métricas de eficiencia operativa.

## Arquitectura

El servicio implementa **Arquitectura Hexagonal (Puertos y Adaptadores)**, manteniendo el dominio independiente de la tecnología de persistencia (MongoDB) y de los frameworks.

Estructura de capas:

1.  **Dominio (Domain)**: Lógica de negocio y cálculos de eficiencia.
2.  **Aplicación (Application)**: Orquestación y transformación de datos.
3.  **Infraestructura (Infrastructure)**: Implementación técnica con MongoDB y REST.

---

## 1. Capa de Dominio (Domain)

Núcleo del servicio, sin dependencias de frameworks externos.

### Modelos
*   **Traceability**: Representa un evento de cambio de estado de un pedido (orderId, clientId, employeeId, previousStatus, newStatus, timestamp).
*   **OrderEfficiency**: Modelo calculado que representa la eficiencia de un pedido (duración total, empleado responsable, estado final).
*   **EmployeeRanking**: Modelo de ranking de empleados basado en tiempo promedio de procesamiento de pedidos.

### Puertos (Ports)
*   **API (Inbound Port)**:
    *   `ITraceabilityServicePort`: Define las operaciones del servicio (guardar trazabilidad, consultar por pedido/cliente, calcular eficiencias y rankings).
*   **SPI (Outbound Port)**:
    *   `ITraceabilityPersistencePort`: Contrato para la persistencia de eventos de trazabilidad y consultas especializadas (ej. obtener IDs de pedidos únicos por restaurante).

### Casos de Uso (Use Cases)
*   **TraceabilityUseCase**: Implementa toda la lógica del dominio.
    *   **Registro de Eventos**: Almacena cada cambio de estado de un pedido.
    *   **Consultas**: Recupera el historial de un pedido o de todos los pedidos de un cliente.
    *   **Cálculo de Eficiencia**: Método `calculateOrderEfficiency` que analiza la línea de tiempo de un pedido (desde PENDING hasta DELIVERED/CANCELLED) para calcular la duración total en minutos. Identifica al empleado responsable del pedido.
    *   **Ranking de Empleados**: Método `getEmployeeRankingByRestaurant` que agrupa las eficiencias por empleado, calcula el tiempo promedio de procesamiento y genera un ranking ordenado (menor tiempo = mejor posición).

**Lógica de Negocio Clave**: El cálculo de eficiencia se basa en la diferencia temporal entre el primer evento (creación del pedido) y el último evento terminal (entrega o cancelación). Si un pedido no ha finalizado, no se incluye en las métricas.

---

## 2. Capa de Aplicación (Application)

Capa de coordinación entre la infraestructura y el dominio.

### Handlers
*   **TraceabilityHandler**: Recibe DTOs desde los controladores, los convierte a modelos de dominio usando Mappers, invoca el caso de uso correspondiente y retorna DTOs de respuesta.

### Mappers
*   Interfaces de **MapStruct** para la conversión bidireccional entre DTOs y Modelos de Dominio.

---

## 3. Capa de Infraestructura (Infrastructure)

Implementación de los detalles técnicos.

### Input (Driving Adapters)
*   **TraceabilityRestController**: Expone endpoints REST para:
    *   Guardar eventos de trazabilidad (invocado por el servicio de Plazoleta).
    *   Consultar historial de un pedido o cliente.
    *   Obtener métricas de eficiencia de pedidos por restaurante.
    *   Obtener ranking de empleados por restaurante.

### Output (Driven Adapters)
*   **MongoDB Adapter**: Implementa `ITraceabilityPersistencePort`.
    *   Utiliza **Spring Data MongoDB** con repositorios reactivos o tradicionales.
    *   **Document**: `TraceabilityDocument` es la representación de la entidad en MongoDB, separada del modelo de dominio.
    *   **Mapper**: Convierte entre `Traceability` (dominio) y `TraceabilityDocument` (persistencia).
    *   **Consultas Personalizadas**: El repositorio incluye métodos para obtener IDs únicos de pedidos por restaurante, necesarios para el cálculo de eficiencias.

### Configuración
*   **BeanConfiguration**: Crea manualmente el Bean del caso de uso (`TraceabilityUseCase`), inyectando la implementación del puerto de persistencia. Esto mantiene el dominio libre de anotaciones de Spring.
*   **SecurityConfiguration**: Configuración de seguridad para validar tokens JWT en las peticiones entrantes.

---

## Decisiones de Diseño y Tecnologías

*   **MongoDB**: Elegido como base de datos NoSQL por la naturaleza de los datos de trazabilidad (eventos secuenciales, esquema flexible, alto volumen de escrituras). MongoDB es ideal para almacenar logs y eventos temporales.
*   **Separación de Modelos**: El modelo de dominio (`Traceability`) está completamente desacoplado del documento de MongoDB (`TraceabilityDocument`), permitiendo evolucionar la estructura de persistencia sin afectar la lógica de negocio.
*   **Cálculos en Memoria**: Las métricas de eficiencia y rankings se calculan en el caso de uso (en memoria) en lugar de usar agregaciones de MongoDB. Esto mantiene la lógica de negocio en el dominio y facilita el testing, aunque puede ser menos eficiente para grandes volúmenes (trade-off entre pureza arquitectónica y rendimiento).
*   **Inyección por Constructor**: Patrón utilizado consistentemente para asegurar inmutabilidad y facilitar pruebas unitarias.
*   **MapStruct y Lombok**: Reducción de código boilerplate para mapeos y getters/setters.
