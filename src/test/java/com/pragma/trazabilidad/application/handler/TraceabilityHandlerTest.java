package com.pragma.trazabilidad.application.handler;

import com.pragma.trazabilidad.application.mapper.EfficiencyDtoMapper;
import com.pragma.trazabilidad.application.mapper.TraceabilityDtoMapper;
import com.pragma.trazabilidad.domain.api.ITraceabilityServicePort;
import com.pragma.trazabilidad.domain.model.EmployeeRanking;
import com.pragma.trazabilidad.domain.model.OrderEfficiency;
import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.EmployeeRankingResponseDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.OrderEfficiencyResponseDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityRequestDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraceabilityHandlerTest {

    @Mock
    private ITraceabilityServicePort traceabilityServicePort;

    @Mock
    private TraceabilityDtoMapper traceabilityDtoMapper;

    @Mock
    private EfficiencyDtoMapper efficiencyDtoMapper;

    @InjectMocks
    private TraceabilityHandler traceabilityHandler;

    private static final Long RESTAURANT_ID = 1L;
    private static final Long ORDER_ID = 100L;
    private static final Long CLIENT_ID = 10L;
    private static final Long EMPLOYEE_ID = 50L;

    @Nested
    @DisplayName("Tests for saveTraceability")
    class SaveTraceabilityTests {

        @Test
        @DisplayName("Should save traceability successfully")
        void shouldSaveTraceabilitySuccessfully() {
            // Given
            TraceabilityRequestDto requestDto = new TraceabilityRequestDto();
            requestDto.setOrderId(ORDER_ID);
            requestDto.setClientId(CLIENT_ID);
            requestDto.setNewStatus("PENDING");
            requestDto.setRestaurantId(RESTAURANT_ID);

            Traceability traceability = new Traceability();
            when(traceabilityDtoMapper.toModel(requestDto)).thenReturn(traceability);

            // When
            traceabilityHandler.saveTraceability(requestDto);

            // Then
            verify(traceabilityDtoMapper, times(1)).toModel(requestDto);
            verify(traceabilityServicePort, times(1)).saveTraceability(traceability);
        }
    }

    @Nested
    @DisplayName("Tests for getTraceabilityByOrderId")
    class GetTraceabilityByOrderIdTests {

        @Test
        @DisplayName("Should return traceability list by order id")
        void shouldReturnTraceabilityListByOrderId() {
            // Given
            List<Traceability> traceabilityList = Arrays.asList(new Traceability(), new Traceability());
            List<TraceabilityResponseDto> responseDtos = Arrays.asList(
                    new TraceabilityResponseDto(),
                    new TraceabilityResponseDto()
            );

            when(traceabilityServicePort.getTraceabilityByOrderId(ORDER_ID)).thenReturn(traceabilityList);
            when(traceabilityDtoMapper.toResponseList(traceabilityList)).thenReturn(responseDtos);

            // When
            List<TraceabilityResponseDto> result = traceabilityHandler.getTraceabilityByOrderId(ORDER_ID);

            // Then
            assertEquals(2, result.size());
            verify(traceabilityServicePort, times(1)).getTraceabilityByOrderId(ORDER_ID);
            verify(traceabilityDtoMapper, times(1)).toResponseList(traceabilityList);
        }
    }

    @Nested
    @DisplayName("Tests for getTraceabilityByClientId")
    class GetTraceabilityByClientIdTests {

        @Test
        @DisplayName("Should return traceability list by client id")
        void shouldReturnTraceabilityListByClientId() {
            // Given
            List<Traceability> traceabilityList = Arrays.asList(new Traceability());
            List<TraceabilityResponseDto> responseDtos = Arrays.asList(new TraceabilityResponseDto());

            when(traceabilityServicePort.getTraceabilityByClientId(CLIENT_ID)).thenReturn(traceabilityList);
            when(traceabilityDtoMapper.toResponseList(traceabilityList)).thenReturn(responseDtos);

            // When
            List<TraceabilityResponseDto> result = traceabilityHandler.getTraceabilityByClientId(CLIENT_ID);

            // Then
            assertEquals(1, result.size());
            verify(traceabilityServicePort, times(1)).getTraceabilityByClientId(CLIENT_ID);
        }
    }

    @Nested
    @DisplayName("Tests for getOrdersEfficiencyByRestaurant")
    class GetOrdersEfficiencyByRestaurantTests {

        @Test
        @DisplayName("Should return orders efficiency list")
        void shouldReturnOrdersEfficiencyList() {
            // Given
            OrderEfficiency efficiency = new OrderEfficiency();
            efficiency.setOrderId(ORDER_ID);
            efficiency.setRestaurantId(RESTAURANT_ID);
            efficiency.setDurationInMinutes(45L);
            efficiency.setFinalStatus("DELIVERED");

            List<OrderEfficiency> efficiencies = Arrays.asList(efficiency);
            
            OrderEfficiencyResponseDto responseDto = new OrderEfficiencyResponseDto();
            responseDto.setOrderId(ORDER_ID);
            responseDto.setRestaurantId(RESTAURANT_ID);
            responseDto.setDurationInMinutes(45L);
            
            List<OrderEfficiencyResponseDto> responseDtos = Arrays.asList(responseDto);

            when(traceabilityServicePort.getOrdersEfficiencyByRestaurant(RESTAURANT_ID)).thenReturn(efficiencies);
            when(efficiencyDtoMapper.toOrderEfficiencyDtoList(efficiencies)).thenReturn(responseDtos);

            // When
            List<OrderEfficiencyResponseDto> result = traceabilityHandler.getOrdersEfficiencyByRestaurant(RESTAURANT_ID);

            // Then
            assertEquals(1, result.size());
            assertEquals(ORDER_ID, result.get(0).getOrderId());
            assertEquals(45L, result.get(0).getDurationInMinutes());
            verify(traceabilityServicePort, times(1)).getOrdersEfficiencyByRestaurant(RESTAURANT_ID);
            verify(efficiencyDtoMapper, times(1)).toOrderEfficiencyDtoList(efficiencies);
        }

        @Test
        @DisplayName("Should return empty list when no orders")
        void shouldReturnEmptyListWhenNoOrders() {
            // Given
            when(traceabilityServicePort.getOrdersEfficiencyByRestaurant(RESTAURANT_ID))
                    .thenReturn(Collections.emptyList());
            when(efficiencyDtoMapper.toOrderEfficiencyDtoList(Collections.emptyList()))
                    .thenReturn(Collections.emptyList());

            // When
            List<OrderEfficiencyResponseDto> result = traceabilityHandler.getOrdersEfficiencyByRestaurant(RESTAURANT_ID);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for getEmployeeRankingByRestaurant")
    class GetEmployeeRankingByRestaurantTests {

        @Test
        @DisplayName("Should return employee ranking list")
        void shouldReturnEmployeeRankingList() {
            // Given
            EmployeeRanking ranking = new EmployeeRanking();
            ranking.setEmployeeId(EMPLOYEE_ID);
            ranking.setRestaurantId(RESTAURANT_ID);
            ranking.setTotalOrdersCompleted(5L);
            ranking.setAverageDurationInMinutes(25.5);
            ranking.setRankingPosition(1);

            List<EmployeeRanking> rankings = Arrays.asList(ranking);

            EmployeeRankingResponseDto responseDto = new EmployeeRankingResponseDto();
            responseDto.setEmployeeId(EMPLOYEE_ID);
            responseDto.setRestaurantId(RESTAURANT_ID);
            responseDto.setTotalOrdersCompleted(5L);
            responseDto.setAverageDurationInMinutes(25.5);
            responseDto.setRankingPosition(1);

            List<EmployeeRankingResponseDto> responseDtos = Arrays.asList(responseDto);

            when(traceabilityServicePort.getEmployeeRankingByRestaurant(RESTAURANT_ID)).thenReturn(rankings);
            when(efficiencyDtoMapper.toEmployeeRankingDtoList(rankings)).thenReturn(responseDtos);

            // When
            List<EmployeeRankingResponseDto> result = traceabilityHandler.getEmployeeRankingByRestaurant(RESTAURANT_ID);

            // Then
            assertEquals(1, result.size());
            assertEquals(EMPLOYEE_ID, result.get(0).getEmployeeId());
            assertEquals(1, result.get(0).getRankingPosition());
            assertEquals(25.5, result.get(0).getAverageDurationInMinutes());
            verify(traceabilityServicePort, times(1)).getEmployeeRankingByRestaurant(RESTAURANT_ID);
            verify(efficiencyDtoMapper, times(1)).toEmployeeRankingDtoList(rankings);
        }

        @Test
        @DisplayName("Should return empty list when no employees")
        void shouldReturnEmptyListWhenNoEmployees() {
            // Given
            when(traceabilityServicePort.getEmployeeRankingByRestaurant(RESTAURANT_ID))
                    .thenReturn(Collections.emptyList());
            when(efficiencyDtoMapper.toEmployeeRankingDtoList(Collections.emptyList()))
                    .thenReturn(Collections.emptyList());

            // When
            List<EmployeeRankingResponseDto> result = traceabilityHandler.getEmployeeRankingByRestaurant(RESTAURANT_ID);

            // Then
            assertTrue(result.isEmpty());
        }
    }
}
