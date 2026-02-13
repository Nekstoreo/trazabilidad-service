package com.pragma.trazabilidad.infrastructure.output.mongodb.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraceabilityOrderItemDocument {
    private Long dishId;
    private String dishName;
    private Integer quantity;
    private Long unitPrice;
    private Long linePrice;
    private String category;
}
