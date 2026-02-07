package com.pragma.trazabilidad.application.mapper;

import com.pragma.trazabilidad.domain.model.EmployeeRanking;
import com.pragma.trazabilidad.domain.model.OrderEfficiency;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.EmployeeRankingResponseDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.OrderEfficiencyResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EfficiencyDtoMapperTest {
    private static final Long ORDER_ID = 1L;
    private static final Long RESTAURANT_ID = 2L;
    private static final Long EMPLOYEE_ID = 3L;
    private static final String EMPLOYEE_EMAIL = "emp@test.com";
    private static final String FINAL_STATUS = "DELIVERED";

    private EfficiencyDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EfficiencyDtoMapper.class);
    }

    @Test
    void toOrderEfficiencyDto_ShouldMapFields() {
        OrderEfficiency model = new OrderEfficiency();
        model.setOrderId(ORDER_ID);
        model.setRestaurantId(RESTAURANT_ID);
        model.setEmployeeId(EMPLOYEE_ID);
        model.setEmployeeEmail(EMPLOYEE_EMAIL);
        model.setStartTime(LocalDateTime.now().minusHours(1));
        model.setEndTime(LocalDateTime.now());
        model.setDurationInMinutes(60L);
        model.setFinalStatus(FINAL_STATUS);

        OrderEfficiencyResponseDto dto = mapper.toOrderEfficiencyDto(model);

        assertNotNull(dto);
        assertEquals(model.getOrderId(), dto.getOrderId());
        assertEquals(model.getRestaurantId(), dto.getRestaurantId());
        assertEquals(model.getEmployeeId(), dto.getEmployeeId());
        assertEquals(model.getEmployeeEmail(), dto.getEmployeeEmail());
        assertEquals(model.getDurationInMinutes(), dto.getDurationInMinutes());
        assertEquals(model.getFinalStatus(), dto.getFinalStatus());
    }

    @Test
    void toOrderEfficiencyDtoList_ShouldMapList() {
        OrderEfficiency model = new OrderEfficiency();
        model.setOrderId(ORDER_ID);
        List<OrderEfficiencyResponseDto> dtoList = mapper.toOrderEfficiencyDtoList(Collections.singletonList(model));

        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        assertEquals(ORDER_ID, dtoList.get(0).getOrderId());
    }

    @Test
    void toEmployeeRankingDto_ShouldMapFields() {
        EmployeeRanking model = new EmployeeRanking();
        model.setEmployeeId(EMPLOYEE_ID);
        model.setEmployeeEmail(EMPLOYEE_EMAIL);
        model.setRestaurantId(RESTAURANT_ID);
        model.setTotalOrdersCompleted(10L);
        model.setAverageDurationInMinutes(15.5);
        model.setRankingPosition(1);

        EmployeeRankingResponseDto dto = mapper.toEmployeeRankingDto(model);

        assertNotNull(dto);
        assertEquals(model.getEmployeeId(), dto.getEmployeeId());
        assertEquals(model.getEmployeeEmail(), dto.getEmployeeEmail());
        assertEquals(model.getTotalOrdersCompleted(), dto.getTotalOrdersCompleted());
        assertEquals(model.getAverageDurationInMinutes(), dto.getAverageDurationInMinutes());
        assertEquals(model.getRankingPosition(), dto.getRankingPosition());
    }

    @Test
    void toEmployeeRankingDtoList_ShouldMapList() {
        EmployeeRanking model = new EmployeeRanking();
        model.setEmployeeId(EMPLOYEE_ID);
        List<EmployeeRankingResponseDto> dtoList = mapper.toEmployeeRankingDtoList(Collections.singletonList(model));

        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        assertEquals(EMPLOYEE_ID, dtoList.get(0).getEmployeeId());
    }
}
