package com.pragma.trazabilidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model that represents an employee efficiency ranking.
 * Contains information about the average order preparation time per employee.
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
