package com.pragma.trazabilidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Model that represents the efficiency of an individual order.
 * Contains information about the time it took the order from creation to completion.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEfficiency {
    private Long orderId;
    private Long restaurantId;
    private Long employeeId;
    private String employeeEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long durationInMinutes;
    private String finalStatus;
}
