package com.pragma.trazabilidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Modelo que representa la eficiencia de un pedido individual.
 * Contiene información sobre el tiempo que tardó el pedido desde su creación hasta su finalización.
 */
@Getter
@Setter
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
