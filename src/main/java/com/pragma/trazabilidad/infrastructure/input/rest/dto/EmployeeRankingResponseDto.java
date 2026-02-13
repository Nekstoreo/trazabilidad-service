package com.pragma.trazabilidad.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa el ranking de eficiencia de un empleado.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRankingResponseDto {
    private Long employeeId;
    private String employeeEmail;
    private Long restaurantId;
    private Long totalOrdersCompleted;
    private Double averageDurationInMinutes;
    private Integer rankingPosition;
}
