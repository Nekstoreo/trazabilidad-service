package com.pragma.trazabilidad.infrastructure.output.mongodb.mapper;

import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.domain.model.TraceabilityOrderItem;
import com.pragma.trazabilidad.infrastructure.output.mongodb.document.TraceabilityDocument;
import com.pragma.trazabilidad.infrastructure.output.mongodb.document.TraceabilityOrderItemDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TraceabilityEntityMapperTest {
    private static final Long ORDER_ID = 1L;
    private static final Long CLIENT_ID = 2L;
    private static final String CLIENT_EMAIL = "client@test.com";
    private static final String PREVIOUS_STATUS = "PENDING";
    private static final String NEW_STATUS = "IN_PREPARATION";
    private static final Long EMPLOYEE_ID = 3L;
    private static final String EMPLOYEE_EMAIL = "employee@test.com";
    private static final Long RESTAURANT_ID = 4L;
    private static final String TRACE_ID = "trace-789";

    private TraceabilityEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TraceabilityEntityMapper.class);
    }

    @Test
    void toDocument_ShouldMapAllFields() {
        Traceability model = new Traceability();
        model.setId(TRACE_ID);
        model.setOrderId(ORDER_ID);
        model.setClientId(CLIENT_ID);
        model.setClientEmail(CLIENT_EMAIL);
        model.setDate(LocalDateTime.now());
        model.setPreviousStatus(PREVIOUS_STATUS);
        model.setNewStatus(NEW_STATUS);
        model.setEmployeeId(EMPLOYEE_ID);
        model.setEmployeeEmail(EMPLOYEE_EMAIL);
        model.setRestaurantId(RESTAURANT_ID);
        model.setOrderItems(Collections.singletonList(createOrderItem()));
        model.setTotalOrderAmount(900L);

        TraceabilityDocument document = mapper.toDocument(model);

        assertNotNull(document);
        assertEquals(model.getId(), document.getId());
        assertEquals(model.getOrderId(), document.getOrderId());
        assertEquals(model.getClientId(), document.getClientId());
        assertEquals(model.getClientEmail(), document.getClientEmail());
        assertEquals(model.getDate(), document.getDate());
        assertEquals(model.getPreviousStatus(), document.getPreviousStatus());
        assertEquals(model.getNewStatus(), document.getNewStatus());
        assertEquals(model.getEmployeeId(), document.getEmployeeId());
        assertEquals(model.getEmployeeEmail(), document.getEmployeeEmail());
        assertEquals(model.getRestaurantId(), document.getRestaurantId());
        assertEquals(1, document.getOrderItems().size());
        assertEquals(900L, document.getTotalOrderAmount());
    }

    @Test
    void toModel_ShouldMapAllFields() {
        TraceabilityDocument document = new TraceabilityDocument();
        document.setId(TRACE_ID);
        document.setOrderId(ORDER_ID);
        document.setClientId(CLIENT_ID);
        document.setClientEmail(CLIENT_EMAIL);
        document.setDate(LocalDateTime.now());
        document.setPreviousStatus(PREVIOUS_STATUS);
        document.setNewStatus(NEW_STATUS);
        document.setEmployeeId(EMPLOYEE_ID);
        document.setEmployeeEmail(EMPLOYEE_EMAIL);
        document.setRestaurantId(RESTAURANT_ID);
        document.setOrderItems(Collections.singletonList(createOrderItemDocument()));
        document.setTotalOrderAmount(900L);

        Traceability model = mapper.toModel(document);

        assertNotNull(model);
        assertEquals(document.getId(), model.getId());
        assertEquals(document.getOrderId(), model.getOrderId());
        assertEquals(document.getClientId(), model.getClientId());
        assertEquals(document.getClientEmail(), model.getClientEmail());
        assertEquals(document.getDate(), model.getDate());
        assertEquals(document.getPreviousStatus(), model.getPreviousStatus());
        assertEquals(document.getNewStatus(), model.getNewStatus());
        assertEquals(document.getEmployeeId(), model.getEmployeeId());
        assertEquals(document.getEmployeeEmail(), model.getEmployeeEmail());
        assertEquals(document.getRestaurantId(), model.getRestaurantId());
        assertEquals(1, model.getOrderItems().size());
        assertEquals(900L, model.getTotalOrderAmount());
    }

    @Test
    void toModelList_ShouldMapAllItems() {
        TraceabilityDocument document = new TraceabilityDocument();
        document.setId(TRACE_ID);
        List<TraceabilityDocument> list = Collections.singletonList(document);

        List<Traceability> modelList = mapper.toModelList(list);

        assertNotNull(modelList);
        assertEquals(1, modelList.size());
        assertEquals(TRACE_ID, modelList.get(0).getId());
    }

    private TraceabilityOrderItem createOrderItem() {
        TraceabilityOrderItem item = new TraceabilityOrderItem();
        item.setDishId(5L);
        item.setDishName("Sushi");
        item.setQuantity(2);
        item.setUnitPrice(450L);
        item.setLinePrice(900L);
        item.setCategory("Japanese");
        return item;
    }

    private TraceabilityOrderItemDocument createOrderItemDocument() {
        TraceabilityOrderItemDocument item = new TraceabilityOrderItemDocument();
        item.setDishId(5L);
        item.setDishName("Sushi");
        item.setQuantity(2);
        item.setUnitPrice(450L);
        item.setLinePrice(900L);
        item.setCategory("Japanese");
        return item;
    }
}
