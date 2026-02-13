package com.pragma.trazabilidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraceabilityOrderItem {
    private Long dishId;
    private String dishName;
    private Integer quantity;
    private Long unitPrice;
    private Long linePrice;
    private String category;
}
