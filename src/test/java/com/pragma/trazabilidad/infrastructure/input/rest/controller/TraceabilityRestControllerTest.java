package com.pragma.trazabilidad.infrastructure.input.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pragma.trazabilidad.infrastructure.input.rest.adapter.TraceabilityRestInputAdapter;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TraceabilityRestControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private TraceabilityRestInputAdapter traceabilityRestInputAdapter;

    @InjectMocks
    private TraceabilityRestController traceabilityRestController;

    private static final Long RESTAURANT_ID = 1L;
    private static final Long ORDER_ID = 100L;
    private static final Long CLIENT_ID = 10L;
    private static final Long EMPLOYEE_ID = 50L;
    private static final String PENDING_STATUS = "PENDING";
    private static final String DELIVERED_STATUS = "DELIVERED";
    private static final String TRACE_ID = "trace-id-1";
    private static final String CLIENT_EMAIL = "client@test.com";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(traceabilityRestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Nested
    @DisplayName("Tests for POST /traceability")
    class SaveTraceabilityTests {

        @Test
        @DisplayName("Should create traceability successfully")
        void shouldCreateTraceabilitySuccessfully() throws Exception {
            // Given
            TraceabilityRequestDto requestDto = new TraceabilityRequestDto();
            requestDto.setOrderId(ORDER_ID);
            requestDto.setClientId(CLIENT_ID);
            requestDto.setClientEmail(CLIENT_EMAIL);
            requestDto.setNewStatus(PENDING_STATUS);
            requestDto.setRestaurantId(RESTAURANT_ID);

            doNothing().when(traceabilityRestInputAdapter).saveTraceability(any(TraceabilityRequestDto.class));

            // When & Then
            mockMvc.perform(post("/traceability")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated());

            verify(traceabilityRestInputAdapter, times(1)).saveTraceability(any(TraceabilityRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Tests for GET /traceability/{orderId}")
    class GetTraceabilityByOrderIdTests {

        @Test
        @DisplayName("Should return traceability by order id")
        void shouldReturnTraceabilityByOrderId() throws Exception {
            // Given
            TraceabilityResponseDto responseDto = new TraceabilityResponseDto();
            responseDto.setId(TRACE_ID);
            responseDto.setOrderId(ORDER_ID);
            responseDto.setNewStatus(PENDING_STATUS);
            responseDto.setRestaurantId(RESTAURANT_ID);

            when(traceabilityRestInputAdapter.getTraceabilityByOrderId(ORDER_ID))
                    .thenReturn(Arrays.asList(responseDto));

            // When & Then
            mockMvc.perform(get("/traceability/{orderId}", ORDER_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].orderId").value(ORDER_ID))
                    .andExpect(jsonPath("$[0].newStatus").value(PENDING_STATUS));

            verify(traceabilityRestInputAdapter, times(1)).getTraceabilityByOrderId(ORDER_ID);
        }

        @Test
        @DisplayName("Should return empty list when no traceability found")
        void shouldReturnEmptyListWhenNoTraceabilityFound() throws Exception {
            // Given
            when(traceabilityRestInputAdapter.getTraceabilityByOrderId(ORDER_ID))
                    .thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/traceability/{orderId}", ORDER_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for GET /traceability/client/{clientId}")
    class GetTraceabilityByClientIdTests {

        @Test
        @DisplayName("Should return traceability by client id")
        void shouldReturnTraceabilityByClientId() throws Exception {
            // Given
            TraceabilityResponseDto responseDto = new TraceabilityResponseDto();
            responseDto.setId(TRACE_ID);
            responseDto.setOrderId(ORDER_ID);
            responseDto.setClientId(CLIENT_ID);
            responseDto.setNewStatus(DELIVERED_STATUS);

            when(traceabilityRestInputAdapter.getTraceabilityByClientId(CLIENT_ID))
                    .thenReturn(Arrays.asList(responseDto));

            // When & Then
            mockMvc.perform(get("/traceability/client/{clientId}", CLIENT_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].clientId").value(CLIENT_ID))
                    .andExpect(jsonPath("$[0].orderId").value(ORDER_ID));

            verify(traceabilityRestInputAdapter, times(1)).getTraceabilityByClientId(CLIENT_ID);
        }
    }

    @Nested
    @DisplayName("Tests for GET /traceability/efficiency/restaurant/{restaurantId}/orders")
    class GetOrdersEfficiencyByRestaurantTests {

        @Test
        @DisplayName("Should return orders efficiency by restaurant")
        void shouldReturnOrdersEfficiencyByRestaurant() throws Exception {
            // Given
            OrderEfficiencyResponseDto responseDto = new OrderEfficiencyResponseDto();
            responseDto.setOrderId(ORDER_ID);
            responseDto.setRestaurantId(RESTAURANT_ID);
            responseDto.setEmployeeId(EMPLOYEE_ID);
            responseDto.setDurationInMinutes(45L);
            responseDto.setFinalStatus(DELIVERED_STATUS);
            responseDto.setStartTime(LocalDateTime.now().minusMinutes(45));
            responseDto.setEndTime(LocalDateTime.now());

            when(traceabilityRestInputAdapter.getOrdersEfficiencyByRestaurant(RESTAURANT_ID))
                    .thenReturn(Arrays.asList(responseDto));

            // When & Then
            mockMvc.perform(get("/traceability/efficiency/restaurant/{restaurantId}/orders", RESTAURANT_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].orderId").value(ORDER_ID))
                    .andExpect(jsonPath("$[0].restaurantId").value(RESTAURANT_ID))
                    .andExpect(jsonPath("$[0].durationInMinutes").value(45))
                    .andExpect(jsonPath("$[0].finalStatus").value(DELIVERED_STATUS));

            verify(traceabilityRestInputAdapter, times(1)).getOrdersEfficiencyByRestaurant(RESTAURANT_ID);
        }

        @Test
        @DisplayName("Should return empty list when no orders")
        void shouldReturnEmptyListWhenNoOrders() throws Exception {
            // Given
            when(traceabilityRestInputAdapter.getOrdersEfficiencyByRestaurant(RESTAURANT_ID))
                    .thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/traceability/efficiency/restaurant/{restaurantId}/orders", RESTAURANT_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for GET /traceability/efficiency/restaurant/{restaurantId}/employees")
    class GetEmployeeRankingByRestaurantTests {

        @Test
        @DisplayName("Should return employee ranking by restaurant")
        void shouldReturnEmployeeRankingByRestaurant() throws Exception {
            // Given
            EmployeeRankingResponseDto ranking1 = new EmployeeRankingResponseDto();
            ranking1.setEmployeeId(EMPLOYEE_ID);
            ranking1.setEmployeeEmail("employee1@test.com");
            ranking1.setRestaurantId(RESTAURANT_ID);
            ranking1.setTotalOrdersCompleted(10L);
            ranking1.setAverageDurationInMinutes(25.5);
            ranking1.setRankingPosition(1);

            EmployeeRankingResponseDto ranking2 = new EmployeeRankingResponseDto();
            ranking2.setEmployeeId(51L);
            ranking2.setEmployeeEmail("employee2@test.com");
            ranking2.setRestaurantId(RESTAURANT_ID);
            ranking2.setTotalOrdersCompleted(8L);
            ranking2.setAverageDurationInMinutes(35.0);
            ranking2.setRankingPosition(2);

            when(traceabilityRestInputAdapter.getEmployeeRankingByRestaurant(RESTAURANT_ID))
                    .thenReturn(Arrays.asList(ranking1, ranking2));

            // When & Then
            mockMvc.perform(get("/traceability/efficiency/restaurant/{restaurantId}/employees", RESTAURANT_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].employeeId").value(EMPLOYEE_ID))
                    .andExpect(jsonPath("$[0].rankingPosition").value(1))
                    .andExpect(jsonPath("$[0].averageDurationInMinutes").value(25.5))
                    .andExpect(jsonPath("$[0].totalOrdersCompleted").value(10))
                    .andExpect(jsonPath("$[1].employeeId").value(51))
                    .andExpect(jsonPath("$[1].rankingPosition").value(2))
                    .andExpect(jsonPath("$[1].averageDurationInMinutes").value(35.0));

            verify(traceabilityRestInputAdapter, times(1)).getEmployeeRankingByRestaurant(RESTAURANT_ID);
        }

        @Test
        @DisplayName("Should return empty list when no employees")
        void shouldReturnEmptyListWhenNoEmployees() throws Exception {
            // Given
            when(traceabilityRestInputAdapter.getEmployeeRankingByRestaurant(RESTAURANT_ID))
                    .thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/traceability/efficiency/restaurant/{restaurantId}/employees", RESTAURANT_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }
}
