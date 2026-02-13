package com.pragma.trazabilidad.application.mapper;

import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.domain.model.TraceabilityOrderItem;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityOrderItemDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityRequestDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TraceabilityDtoMapperTest {
    private static final Long ORDER_ID = 1L;
    private static final Long CLIENT_ID = 2L;
    private static final String CLIENT_EMAIL = "client@test.com";
    private static final String PREVIOUS_STATUS = "PENDING";
    private static final String NEW_STATUS = "IN_PREPARATION";
    private static final Long EMPLOYEE_ID = 3L;
    private static final String EMPLOYEE_EMAIL = "employee@test.com";
    private static final Long RESTAURANT_ID = 4L;
    private static final String TRACE_ID = "trace-123";

    private TraceabilityDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TraceabilityDtoMapper.class);
    }

    @Test
    void toModel_ShouldMapAllFields() {
        TraceabilityRequestDto requestDto = new TraceabilityRequestDto();
        requestDto.setOrderId(ORDER_ID);
        requestDto.setClientId(CLIENT_ID);
        requestDto.setClientEmail(CLIENT_EMAIL);
        requestDto.setPreviousStatus(PREVIOUS_STATUS);
        requestDto.setNewStatus(NEW_STATUS);
        requestDto.setEmployeeId(EMPLOYEE_ID);
        requestDto.setEmployeeEmail(EMPLOYEE_EMAIL);
        requestDto.setRestaurantId(RESTAURANT_ID);
        requestDto.setOrderItems(Collections.singletonList(createOrderItemDto()));
        requestDto.setTotalOrderAmount(2500L);

        Traceability model = mapper.toModel(requestDto);

        assertNotNull(model);
        assertEquals(requestDto.getOrderId(), model.getOrderId());
        assertEquals(requestDto.getClientId(), model.getClientId());
        assertEquals(requestDto.getClientEmail(), model.getClientEmail());
        assertEquals(requestDto.getPreviousStatus(), model.getPreviousStatus());
        assertEquals(requestDto.getNewStatus(), model.getNewStatus());
        assertEquals(requestDto.getEmployeeId(), model.getEmployeeId());
        assertEquals(requestDto.getEmployeeEmail(), model.getEmployeeEmail());
        assertEquals(requestDto.getRestaurantId(), model.getRestaurantId());
        assertNotNull(model.getDate());
        assertEquals(1, model.getOrderItems().size());
        assertEquals(2500L, model.getTotalOrderAmount());
    }

    @Test
    void toResponse_ShouldMapAllFields() {
        Traceability model = new Traceability();
        model.setId(TRACE_ID);
        model.setOrderId(ORDER_ID);
        model.setClientId(CLIENT_ID);
        model.setClientEmail(CLIENT_EMAIL);
        model.setPreviousStatus(PREVIOUS_STATUS);
        model.setNewStatus(NEW_STATUS);
        model.setEmployeeId(EMPLOYEE_ID);
        model.setEmployeeEmail(EMPLOYEE_EMAIL);
        model.setRestaurantId(RESTAURANT_ID);
        model.setOrderItems(Collections.singletonList(createOrderItem()));
        model.setTotalOrderAmount(2500L);

        TraceabilityResponseDto responseDto = mapper.toResponse(model);

        assertNotNull(responseDto);
        assertEquals(model.getId(), responseDto.getId());
        assertEquals(model.getOrderId(), responseDto.getOrderId());
        assertEquals(model.getClientId(), responseDto.getClientId());
        assertEquals(model.getClientEmail(), responseDto.getClientEmail());
        assertEquals(model.getPreviousStatus(), responseDto.getPreviousStatus());
        assertEquals(model.getNewStatus(), responseDto.getNewStatus());
        assertEquals(model.getEmployeeId(), responseDto.getEmployeeId());
        assertEquals(model.getEmployeeEmail(), responseDto.getEmployeeEmail());
        assertEquals(model.getRestaurantId(), responseDto.getRestaurantId());
        assertEquals(1, responseDto.getOrderItems().size());
        assertEquals(2500L, responseDto.getTotalOrderAmount());
    }

    @Test
    void toResponseList_ShouldMapAllItems() {
        Traceability model = new Traceability();
        model.setId(TRACE_ID);
        List<Traceability> list = Collections.singletonList(model);

        List<TraceabilityResponseDto> responseList = mapper.toResponseList(list);

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(TRACE_ID, responseList.get(0).getId());
    }

    private TraceabilityOrderItemDto createOrderItemDto() {
        TraceabilityOrderItemDto item = new TraceabilityOrderItemDto();
        item.setDishId(12L);
        item.setDishName("Taco");
        item.setQuantity(3);
        item.setUnitPrice(250L);
        item.setLinePrice(750L);
        item.setCategory("Mexican");
        return item;
    }

    private TraceabilityOrderItem createOrderItem() {
        TraceabilityOrderItem item = new TraceabilityOrderItem();
        item.setDishId(12L);
        item.setDishName("Taco");
        item.setQuantity(3);
        item.setUnitPrice(250L);
        item.setLinePrice(750L);
        item.setCategory("Mexican");
        return item;
    }
}
