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
     * Retrieves the efficiency of all orders for a specific restaurant.
     * Shows the time between order creation and completion for each order.
     *
     * @param restaurantId ID of the restaurant
     * @return List of order efficiencies
     */
    List<OrderEfficiency> getOrdersEfficiencyByRestaurant(Long restaurantId);

    /**
     * Retrieves the ranking of employees by average order completion time.
     *
     * @param restaurantId ID of the restaurant
     * @return List of employee rankings ordered by efficiency
     */
    List<EmployeeRanking> getEmployeeRankingByRestaurant(Long restaurantId);
}
