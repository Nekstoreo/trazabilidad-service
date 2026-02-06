package com.pragma.trazabilidad.domain.usecase;

import com.pragma.trazabilidad.domain.api.ITraceabilityServicePort;
import com.pragma.trazabilidad.domain.model.EmployeeRanking;
import com.pragma.trazabilidad.domain.model.OrderEfficiency;
import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.domain.spi.ITraceabilityPersistencePort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TraceabilityUseCase implements ITraceabilityServicePort {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_DELIVERED = "DELIVERED";
    private static final String STATUS_CANCELLED = "CANCELLED";

    private final ITraceabilityPersistencePort traceabilityPersistencePort;

    public TraceabilityUseCase(ITraceabilityPersistencePort traceabilityPersistencePort) {
        this.traceabilityPersistencePort = traceabilityPersistencePort;
    }

    @Override
    public void saveTraceability(Traceability traceability) {
        traceabilityPersistencePort.saveTraceability(traceability);
    }

    @Override
    public List<Traceability> getTraceabilityByOrderId(Long orderId) {
        return traceabilityPersistencePort.getTraceabilityByOrderId(orderId);
    }

    @Override
    public List<Traceability> getTraceabilityByClientId(Long clientId) {
        return traceabilityPersistencePort.getTraceabilityByClientId(clientId);
    }

    @Override
    public List<OrderEfficiency> getOrdersEfficiencyByRestaurant(Long restaurantId) {
        List<Long> orderIds = traceabilityPersistencePort.getDistinctOrderIdsByRestaurantId(restaurantId);
        List<OrderEfficiency> efficiencies = new ArrayList<>();

        for (Long orderId : orderIds) {
            OrderEfficiency efficiency = calculateOrderEfficiency(orderId, restaurantId);
            if (efficiency != null) {
                efficiencies.add(efficiency);
            }
        }

        return efficiencies.stream()
                .sorted(Comparator.comparing(OrderEfficiency::getOrderId))
                .toList();
    }

    @Override
    public List<EmployeeRanking> getEmployeeRankingByRestaurant(Long restaurantId) {
        List<OrderEfficiency> efficiencies = getOrdersEfficiencyByRestaurant(restaurantId);

        // Agrupar por empleado y calcular estadísticas
        Map<Long, List<OrderEfficiency>> efficienciesByEmployee = new HashMap<>();
        Map<Long, String> employeeEmails = new HashMap<>();

        for (OrderEfficiency efficiency : efficiencies) {
            if (efficiency.getEmployeeId() != null) {
                efficienciesByEmployee
                        .computeIfAbsent(efficiency.getEmployeeId(), k -> new ArrayList<>())
                        .add(efficiency);
                employeeEmails.putIfAbsent(efficiency.getEmployeeId(), efficiency.getEmployeeEmail());
            }
        }

        List<EmployeeRanking> rankings = new ArrayList<>();

        for (Map.Entry<Long, List<OrderEfficiency>> entry : efficienciesByEmployee.entrySet()) {
            Long employeeId = entry.getKey();
            List<OrderEfficiency> employeeEfficiencies = entry.getValue();

            double averageDuration = employeeEfficiencies.stream()
                    .mapToLong(OrderEfficiency::getDurationInMinutes)
                    .average()
                    .orElse(0.0);

            EmployeeRanking ranking = new EmployeeRanking();
            ranking.setEmployeeId(employeeId);
            ranking.setEmployeeEmail(employeeEmails.get(employeeId));
            ranking.setRestaurantId(restaurantId);
            ranking.setTotalOrdersCompleted((long) employeeEfficiencies.size());
            ranking.setAverageDurationInMinutes(Math.round(averageDuration * 100.0) / 100.0);

            rankings.add(ranking);
        }

        // Ordenar por tiempo promedio (menor es mejor) y asignar posición
        rankings.sort(Comparator.comparing(EmployeeRanking::getAverageDurationInMinutes));

        AtomicInteger position = new AtomicInteger(1);
        rankings.forEach(r -> r.setRankingPosition(position.getAndIncrement()));

        return rankings;
    }

    private OrderEfficiency calculateOrderEfficiency(Long orderId, Long restaurantId) {
        List<Traceability> traces = traceabilityPersistencePort.getTraceabilityByOrderId(orderId);

        if (traces.isEmpty()) {
            return null;
        }

        // Ordenar por fecha
        traces.sort(Comparator.comparing(Traceability::getDate));

        // Buscar el primer registro (cuando se crea el pedido - estado PENDING)
        Traceability firstTrace = traces.stream()
                .filter(t -> STATUS_PENDING.equals(t.getNewStatus()))
                .findFirst()
                .orElse(traces.get(0));

        // Buscar el último registro (DELIVERED o CANCELLED)
        Traceability lastTrace = traces.stream()
                .filter(t -> STATUS_DELIVERED.equals(t.getNewStatus()) || STATUS_CANCELLED.equals(t.getNewStatus()))
                .reduce((first, second) -> second)
                .orElse(null);

        // Si el pedido no ha terminado, no calcular eficiencia
        if (lastTrace == null) {
            return null;
        }

        LocalDateTime startTime = firstTrace.getDate();
        LocalDateTime endTime = lastTrace.getDate();
        long durationMinutes = Duration.between(startTime, endTime).toMinutes();

        // Obtener el empleado que procesó el pedido (el que lo marcó como entregado o el último que interactuó)
        Long employeeId = lastTrace.getEmployeeId();
        String employeeEmail = lastTrace.getEmployeeEmail();

        // Si no hay empleado en el último registro, buscar en registros anteriores
        if (employeeId == null) {
            for (int i = traces.size() - 1; i >= 0; i--) {
                if (traces.get(i).getEmployeeId() != null) {
                    employeeId = traces.get(i).getEmployeeId();
                    employeeEmail = traces.get(i).getEmployeeEmail();
                    break;
                }
            }
        }

        OrderEfficiency efficiency = new OrderEfficiency();
        efficiency.setOrderId(orderId);
        efficiency.setRestaurantId(restaurantId);
        efficiency.setEmployeeId(employeeId);
        efficiency.setEmployeeEmail(employeeEmail);
        efficiency.setStartTime(startTime);
        efficiency.setEndTime(endTime);
        efficiency.setDurationInMinutes(durationMinutes);
        efficiency.setFinalStatus(lastTrace.getNewStatus());

        return efficiency;
    }
}
