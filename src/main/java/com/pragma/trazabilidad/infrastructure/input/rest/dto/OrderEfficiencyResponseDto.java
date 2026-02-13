package com.pragma.trazabilidad.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO that represents the efficiency of an individual order.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEfficiencyResponseDto {
    private Long orderId;
    private Long restaurantId;
    private Long employeeId;
    private String employeeEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationInMinutes;
    private String finalStatus;
}
