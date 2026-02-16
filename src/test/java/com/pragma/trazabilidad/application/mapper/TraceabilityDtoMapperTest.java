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

    @Test
    void toResponseList_WithNullInput_ShouldReturnNull() {
        List<TraceabilityResponseDto> responseList = mapper.toResponseList(null);
        assertNull(responseList);
    }

    @Test
    void toOrderItemModel_ShouldMapAllFields() {
        TraceabilityOrderItemDto itemDto = createOrderItemDto();

        TraceabilityOrderItem item = mapper.toOrderItemModel(itemDto);

        assertNotNull(item);
        assertEquals(itemDto.getDishId(), item.getDishId());
        assertEquals(itemDto.getDishName(), item.getDishName());
        assertEquals(itemDto.getQuantity(), item.getQuantity());
        assertEquals(itemDto.getUnitPrice(), item.getUnitPrice());
        assertEquals(itemDto.getLinePrice(), item.getLinePrice());
        assertEquals(itemDto.getCategory(), item.getCategory());
    }

    @Test
    void toOrderItemDto_ShouldMapAllFields() {
        TraceabilityOrderItem item = createOrderItem();

        TraceabilityOrderItemDto itemDto = mapper.toOrderItemDto(item);

        assertNotNull(itemDto);
        assertEquals(item.getDishId(), itemDto.getDishId());
        assertEquals(item.getDishName(), itemDto.getDishName());
        assertEquals(item.getQuantity(), itemDto.getQuantity());
        assertEquals(item.getUnitPrice(), itemDto.getUnitPrice());
        assertEquals(item.getLinePrice(), itemDto.getLinePrice());
        assertEquals(item.getCategory(), itemDto.getCategory());
    }

    @Test
    void toOrderItemModelList_WithNullInput_ShouldReturnNull() {
        List<TraceabilityOrderItem> items = mapper.toOrderItemModelList(null);
        assertNull(items);
    }

    @Test
    void toOrderItemDtoList_WithNullInput_ShouldReturnNull() {
        List<TraceabilityOrderItemDto> items = mapper.toOrderItemDtoList(null);
        assertNull(items);
    }

    @Test
    void toOrderItemLists_ShouldMapAllItems() {
        List<TraceabilityOrderItemDto> dtoList = Collections.singletonList(createOrderItemDto());
        List<TraceabilityOrderItem> modelList = mapper.toOrderItemModelList(dtoList);

        assertNotNull(modelList);
        assertEquals(1, modelList.size());
        assertEquals(dtoList.get(0).getDishId(), modelList.get(0).getDishId());

        List<TraceabilityOrderItemDto> mappedBack = mapper.toOrderItemDtoList(modelList);

        assertNotNull(mappedBack);
        assertEquals(1, mappedBack.size());
        assertEquals(modelList.get(0).getDishName(), mappedBack.get(0).getDishName());
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
