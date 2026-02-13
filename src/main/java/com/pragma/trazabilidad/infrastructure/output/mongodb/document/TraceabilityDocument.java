package com.pragma.trazabilidad.infrastructure.output.mongodb.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "traceability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraceabilityDocument {
    @Id
    private String id;
    private Long orderId;
    private Long clientId;
    private String clientEmail;
    private LocalDateTime date;
    private String previousStatus;
    private String newStatus;
    private Long employeeId;
    private String employeeEmail;
    private Long restaurantId;
    private List<TraceabilityOrderItemDocument> orderItems = new ArrayList<>();
    private Long totalOrderAmount;
}
