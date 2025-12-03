package com.pragma.trazabilidad.domain.api;

import com.pragma.trazabilidad.domain.model.EmployeeRanking;
import com.pragma.trazabilidad.domain.model.OrderEfficiency;
import com.pragma.trazabilidad.domain.model.Traceability;

import java.util.List;

public interface ITraceabilityServicePort {
    void saveTraceability(Traceability traceability);
    List<Traceability> getTraceabilityByOrderId(Long orderId);
    List<Traceability> getTraceabilityByClientId(Long clientId);

    /**
     * Obtiene la eficiencia de todos los pedidos de un restaurante.
     * Muestra el tiempo entre que un pedido inicia y termina.
     *
     * @param restaurantId ID del restaurante
     * @return Lista de eficiencias de pedidos
     */
    List<OrderEfficiency> getOrdersEfficiencyByRestaurant(Long restaurantId);

    /**
     * Obtiene el ranking de empleados por tiempo medio de preparación de pedidos.
     *
     * @param restaurantId ID del restaurante
     * @return Lista de rankings de empleados ordenados por eficiencia
     */
    List<EmployeeRanking> getEmployeeRankingByRestaurant(Long restaurantId);
}
