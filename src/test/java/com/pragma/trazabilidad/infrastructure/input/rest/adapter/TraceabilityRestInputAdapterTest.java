package com.pragma.trazabilidad.infrastructure.input.rest.adapter;

import com.pragma.trazabilidad.domain.api.ITraceabilityServicePort;
import com.pragma.trazabilidad.domain.model.EmployeeRanking;
import com.pragma.trazabilidad.domain.model.OrderEfficiency;
import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.EmployeeRankingResponseDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.OrderEfficiencyResponseDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityRequestDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityResponseDto;
import com.pragma.trazabilidad.application.mapper.EfficiencyDtoMapper;
import com.pragma.trazabilidad.application.mapper.TraceabilityDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraceabilityRestInputAdapterTest {
    private static final Long ORDER_ID = 1L;
    private static final Long CLIENT_ID = 2L;
    private static final Long RESTAURANT_ID = 1L;

    @Mock
    private ITraceabilityServicePort traceabilityServicePort;

    @Mock
    private TraceabilityDtoMapper traceabilityDtoMapper;

    @Mock
    private EfficiencyDtoMapper efficiencyDtoMapper;

    @InjectMocks
    private TraceabilityRestInputAdapter adapter;

    @Test
    void saveTraceability_ShouldCallServicePort() {
        TraceabilityRequestDto requestDto = new TraceabilityRequestDto();
        Traceability model = new Traceability();

        when(traceabilityDtoMapper.toModel(requestDto)).thenReturn(model);

        adapter.saveTraceability(requestDto);

        verify(traceabilityDtoMapper).toModel(requestDto);
        verify(traceabilityServicePort).saveTraceability(model);
    }

    @Test
    void getTraceabilityByOrderId_ShouldReturnList() {
        List<Traceability> list = Collections.singletonList(new Traceability());
        List<TraceabilityResponseDto> dtoList = Collections.singletonList(new TraceabilityResponseDto());

        when(traceabilityServicePort.getTraceabilityByOrderId(ORDER_ID)).thenReturn(list);
        when(traceabilityDtoMapper.toResponseList(list)).thenReturn(dtoList);

        List<TraceabilityResponseDto> result = adapter.getTraceabilityByOrderId(ORDER_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(traceabilityServicePort).getTraceabilityByOrderId(ORDER_ID);
        verify(traceabilityDtoMapper).toResponseList(list);
    }

    @Test
    void getTraceabilityByClientId_ShouldReturnList() {
        List<Traceability> list = Collections.singletonList(new Traceability());
        List<TraceabilityResponseDto> dtoList = Collections.singletonList(new TraceabilityResponseDto());

        when(traceabilityServicePort.getTraceabilityByClientId(CLIENT_ID)).thenReturn(list);
        when(traceabilityDtoMapper.toResponseList(list)).thenReturn(dtoList);

        List<TraceabilityResponseDto> result = adapter.getTraceabilityByClientId(CLIENT_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(traceabilityServicePort).getTraceabilityByClientId(CLIENT_ID);
        verify(traceabilityDtoMapper).toResponseList(list);
    }

    @Test
    void getOrdersEfficiencyByRestaurant_ShouldReturnList() {
        List<OrderEfficiency> list = Collections.singletonList(new OrderEfficiency());
        List<OrderEfficiencyResponseDto> dtoList = Collections.singletonList(new OrderEfficiencyResponseDto());

        when(traceabilityServicePort.getOrdersEfficiencyByRestaurant(RESTAURANT_ID)).thenReturn(list);
        when(efficiencyDtoMapper.toOrderEfficiencyDtoList(list)).thenReturn(dtoList);

        List<OrderEfficiencyResponseDto> result = adapter.getOrdersEfficiencyByRestaurant(RESTAURANT_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(traceabilityServicePort).getOrdersEfficiencyByRestaurant(RESTAURANT_ID);
        verify(efficiencyDtoMapper).toOrderEfficiencyDtoList(list);
    }

    @Test
    void getEmployeeRankingByRestaurant_ShouldReturnList() {
        List<EmployeeRanking> list = Collections.singletonList(new EmployeeRanking());
        List<EmployeeRankingResponseDto> dtoList = Collections.singletonList(new EmployeeRankingResponseDto());

        when(traceabilityServicePort.getEmployeeRankingByRestaurant(RESTAURANT_ID)).thenReturn(list);
        when(efficiencyDtoMapper.toEmployeeRankingDtoList(list)).thenReturn(dtoList);

        List<EmployeeRankingResponseDto> result = adapter.getEmployeeRankingByRestaurant(RESTAURANT_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(traceabilityServicePort).getEmployeeRankingByRestaurant(RESTAURANT_ID);
        verify(efficiencyDtoMapper).toEmployeeRankingDtoList(list);
    }
}
