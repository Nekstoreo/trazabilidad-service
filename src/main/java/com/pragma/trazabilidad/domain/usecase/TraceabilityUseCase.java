package com.pragma.trazabilidad.domain.usecase;

import com.pragma.trazabilidad.domain.api.ITraceabilityServicePort;
import com.pragma.trazabilidad.domain.model.EmployeeRanking;
import com.pragma.trazabilidad.domain.model.OrderEfficiency;
import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.domain.spi.ITraceabilityPersistencePort;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class TraceabilityUseCase implements ITraceabilityServicePort {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_DELIVERED = "DELIVERED";
    private static final String STATUS_CANCELLED = "CANCELLED";

    private final ITraceabilityPersistencePort traceabilityPersistencePort;

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

        // Group by employee and calculate statistics
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

            rankings.add(EmployeeRanking.builder()
                    .employeeId(employeeId)
                    .employeeEmail(employeeEmails.get(employeeId))
                    .restaurantId(restaurantId)
                    .totalOrdersCompleted((long) employeeEfficiencies.size())
                    .averageDurationInMinutes(Math.round(averageDuration * 100.0) / 100.0)
                    .build());
        }

        // Sort by average time (lower is better) and assign position
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

        // Sort by date
        traces.sort(Comparator.comparing(Traceability::getDate));

        // Find the first record (when the order is created - status PENDING)
        Traceability firstTrace = traces.stream()
                .filter(t -> STATUS_PENDING.equals(t.getNewStatus()))
                .findFirst()
                .orElse(traces.get(0));

        // Find the last record (DELIVERED or CANCELLED)
        Traceability lastTrace = traces.stream()
                .filter(t -> STATUS_DELIVERED.equals(t.getNewStatus()) || STATUS_CANCELLED.equals(t.getNewStatus()))
                .reduce((first, second) -> second)
                .orElse(null);

        // If the order hasn't finished, do not calculate efficiency
        if (lastTrace == null) {
            return null;
        }

        LocalDateTime startTime = firstTrace.getDate();
        LocalDateTime endTime = lastTrace.getDate();
        long durationMinutes = Duration.between(startTime, endTime).toMinutes();

        // Get the employee who processed the order (the one who marked it as delivered or the last one who interacted)
        Long employeeId = lastTrace.getEmployeeId();
        String employeeEmail = lastTrace.getEmployeeEmail();

        // If there is no employee in the last record, search previous records
        if (employeeId == null) {
            for (int i = traces.size() - 1; i >= 0; i--) {
                if (traces.get(i).getEmployeeId() != null) {
                    employeeId = traces.get(i).getEmployeeId();
                    employeeEmail = traces.get(i).getEmployeeEmail();
                    break;
                }
            }
        }

        return OrderEfficiency.builder()
                .orderId(orderId)
                .restaurantId(restaurantId)
                .employeeId(employeeId)
                .employeeEmail(employeeEmail)
                .startTime(startTime)
                .endTime(endTime)
                .durationInMinutes(durationMinutes)
                .finalStatus(lastTrace.getNewStatus())
                .build();
    }
}
