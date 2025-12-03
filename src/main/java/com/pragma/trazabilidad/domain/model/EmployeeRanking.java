package com.pragma.trazabilidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Modelo que representa el ranking de eficiencia de un empleado.
 * Contiene información sobre el tiempo medio de preparación de pedidos por empleado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRanking {
    private Long employeeId;
    private String employeeEmail;
    private Long restaurantId;
    private Long totalOrdersCompleted;
    private Double averageDurationInMinutes;
    private Integer rankingPosition;
}
