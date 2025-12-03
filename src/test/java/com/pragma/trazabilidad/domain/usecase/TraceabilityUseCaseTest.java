package com.pragma.trazabilidad.domain.usecase;

import com.pragma.trazabilidad.domain.model.EmployeeRanking;
import com.pragma.trazabilidad.domain.model.OrderEfficiency;
import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.domain.spi.ITraceabilityPersistencePort;
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
class TraceabilityUseCaseTest {

    @Mock
    private ITraceabilityPersistencePort traceabilityPersistencePort;

    @InjectMocks
    private TraceabilityUseCase traceabilityUseCase;

    private static final Long RESTAURANT_ID = 1L;
    private static final Long ORDER_ID_1 = 100L;
    private static final Long ORDER_ID_2 = 101L;
    private static final Long CLIENT_ID = 10L;
    private static final Long EMPLOYEE_ID_1 = 50L;
    private static final Long EMPLOYEE_ID_2 = 51L;
    private static final String EMPLOYEE_EMAIL_1 = "employee1@test.com";
    private static final String EMPLOYEE_EMAIL_2 = "employee2@test.com";
    private static final String CLIENT_EMAIL = "client@test.com";

    @Nested
    @DisplayName("Tests for saveTraceability")
    class SaveTraceabilityTests {

        @Test
        @DisplayName("Should save traceability successfully")
        void shouldSaveTraceabilitySuccessfully() {
            // Given
            Traceability traceability = createTraceability(ORDER_ID_1, "PENDING", "IN_PREPARATION");

            // When
            traceabilityUseCase.saveTraceability(traceability);

            // Then
            verify(traceabilityPersistencePort, times(1)).saveTraceability(traceability);
        }
    }

    @Nested
    @DisplayName("Tests for getTraceabilityByOrderId")
    class GetTraceabilityByOrderIdTests {

        @Test
        @DisplayName("Should return traceability list for order")
        void shouldReturnTraceabilityListForOrder() {
            // Given
            List<Traceability> expectedList = Arrays.asList(
                    createTraceability(ORDER_ID_1, null, "PENDING"),
                    createTraceability(ORDER_ID_1, "PENDING", "IN_PREPARATION")
            );
            when(traceabilityPersistencePort.getTraceabilityByOrderId(ORDER_ID_1)).thenReturn(expectedList);

            // When
            List<Traceability> result = traceabilityUseCase.getTraceabilityByOrderId(ORDER_ID_1);

            // Then
            assertEquals(2, result.size());
            verify(traceabilityPersistencePort, times(1)).getTraceabilityByOrderId(ORDER_ID_1);
        }

        @Test
        @DisplayName("Should return empty list when no traceability found")
        void shouldReturnEmptyListWhenNoTraceabilityFound() {
            // Given
            when(traceabilityPersistencePort.getTraceabilityByOrderId(ORDER_ID_1)).thenReturn(Collections.emptyList());

            // When
            List<Traceability> result = traceabilityUseCase.getTraceabilityByOrderId(ORDER_ID_1);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for getTraceabilityByClientId")
    class GetTraceabilityByClientIdTests {

        @Test
        @DisplayName("Should return traceability list for client")
        void shouldReturnTraceabilityListForClient() {
            // Given
            List<Traceability> expectedList = Arrays.asList(
                    createTraceability(ORDER_ID_1, null, "PENDING"),
                    createTraceability(ORDER_ID_2, null, "PENDING")
            );
            when(traceabilityPersistencePort.getTraceabilityByClientId(CLIENT_ID)).thenReturn(expectedList);

            // When
            List<Traceability> result = traceabilityUseCase.getTraceabilityByClientId(CLIENT_ID);

            // Then
            assertEquals(2, result.size());
            verify(traceabilityPersistencePort, times(1)).getTraceabilityByClientId(CLIENT_ID);
        }
    }

    @Nested
    @DisplayName("Tests for getOrdersEfficiencyByRestaurant")
    class GetOrdersEfficiencyByRestaurantTests {

        @Test
        @DisplayName("Should return order efficiencies for completed orders")
        void shouldReturnOrderEfficienciesForCompletedOrders() {
            // Given
            LocalDateTime startTime = LocalDateTime.now().minusHours(1);
            LocalDateTime endTime = LocalDateTime.now();

            List<Long> orderIds = Arrays.asList(ORDER_ID_1);
            when(traceabilityPersistencePort.getDistinctOrderIdsByRestaurantId(RESTAURANT_ID)).thenReturn(orderIds);

            List<Traceability> orderTraces = Arrays.asList(
                    createTraceabilityWithDate(ORDER_ID_1, null, "PENDING", startTime, null, null),
                    createTraceabilityWithDate(ORDER_ID_1, "PENDING", "IN_PREPARATION", startTime.plusMinutes(10), EMPLOYEE_ID_1, EMPLOYEE_EMAIL_1),
                    createTraceabilityWithDate(ORDER_ID_1, "IN_PREPARATION", "READY", startTime.plusMinutes(30), EMPLOYEE_ID_1, EMPLOYEE_EMAIL_1),
                    createTraceabilityWithDate(ORDER_ID_1, "READY", "DELIVERED", endTime, EMPLOYEE_ID_1, EMPLOYEE_EMAIL_1)
            );
            when(traceabilityPersistencePort.getTraceabilityByOrderId(ORDER_ID_1)).thenReturn(orderTraces);

            // When
            List<OrderEfficiency> result = traceabilityUseCase.getOrdersEfficiencyByRestaurant(RESTAURANT_ID);

            // Then
            assertEquals(1, result.size());
            OrderEfficiency efficiency = result.get(0);
            assertEquals(ORDER_ID_1, efficiency.getOrderId());
            assertEquals(RESTAURANT_ID, efficiency.getRestaurantId());
            assertEquals(EMPLOYEE_ID_1, efficiency.getEmployeeId());
            assertEquals("DELIVERED", efficiency.getFinalStatus());
            assertTrue(efficiency.getDurationInMinutes() >= 60);
        }

        @Test
        @DisplayName("Should not include pending orders in efficiency")
        void shouldNotIncludePendingOrdersInEfficiency() {
            // Given
            List<Long> orderIds = Arrays.asList(ORDER_ID_1);
            when(traceabilityPersistencePort.getDistinctOrderIdsByRestaurantId(RESTAURANT_ID)).thenReturn(orderIds);

            List<Traceability> orderTraces = Arrays.asList(
                    createTraceabilityWithDate(ORDER_ID_1, null, "PENDING", LocalDateTime.now(), null, null)
            );
            when(traceabilityPersistencePort.getTraceabilityByOrderId(ORDER_ID_1)).thenReturn(orderTraces);

            // When
            List<OrderEfficiency> result = traceabilityUseCase.getOrdersEfficiencyByRestaurant(RESTAURANT_ID);

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should include cancelled orders in efficiency")
        void shouldIncludeCancelledOrdersInEfficiency() {
            // Given
            LocalDateTime startTime = LocalDateTime.now().minusMinutes(15);
            LocalDateTime cancelTime = LocalDateTime.now();

            List<Long> orderIds = Arrays.asList(ORDER_ID_1);
            when(traceabilityPersistencePort.getDistinctOrderIdsByRestaurantId(RESTAURANT_ID)).thenReturn(orderIds);

            List<Traceability> orderTraces = Arrays.asList(
                    createTraceabilityWithDate(ORDER_ID_1, null, "PENDING", startTime, null, null),
                    createTraceabilityWithDate(ORDER_ID_1, "PENDING", "CANCELLED", cancelTime, null, null)
            );
            when(traceabilityPersistencePort.getTraceabilityByOrderId(ORDER_ID_1)).thenReturn(orderTraces);

            // When
            List<OrderEfficiency> result = traceabilityUseCase.getOrdersEfficiencyByRestaurant(RESTAURANT_ID);

            // Then
            assertEquals(1, result.size());
            assertEquals("CANCELLED", result.get(0).getFinalStatus());
        }

        @Test
        @DisplayName("Should return empty list when no orders")
        void shouldReturnEmptyListWhenNoOrders() {
            // Given
            when(traceabilityPersistencePort.getDistinctOrderIdsByRestaurantId(RESTAURANT_ID))
                    .thenReturn(Collections.emptyList());

            // When
            List<OrderEfficiency> result = traceabilityUseCase.getOrdersEfficiencyByRestaurant(RESTAURANT_ID);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for getEmployeeRankingByRestaurant")
    class GetEmployeeRankingByRestaurantTests {

        @Test
        @DisplayName("Should return employee ranking ordered by average time")
        void shouldReturnEmployeeRankingOrderedByAverageTime() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            List<Long> orderIds = Arrays.asList(ORDER_ID_1, ORDER_ID_2);
            when(traceabilityPersistencePort.getDistinctOrderIdsByRestaurantId(RESTAURANT_ID)).thenReturn(orderIds);

            // Employee 1 completes order in 30 minutes
            List<Traceability> order1Traces = Arrays.asList(
                    createTraceabilityWithDate(ORDER_ID_1, null, "PENDING", now.minusMinutes(30), null, null),
                    createTraceabilityWithDate(ORDER_ID_1, "PENDING", "IN_PREPARATION", now.minusMinutes(25), EMPLOYEE_ID_1, EMPLOYEE_EMAIL_1),
                    createTraceabilityWithDate(ORDER_ID_1, "IN_PREPARATION", "DELIVERED", now, EMPLOYEE_ID_1, EMPLOYEE_EMAIL_1)
            );
            when(traceabilityPersistencePort.getTraceabilityByOrderId(ORDER_ID_1)).thenReturn(order1Traces);

            // Employee 2 completes order in 60 minutes
            List<Traceability> order2Traces = Arrays.asList(
                    createTraceabilityWithDate(ORDER_ID_2, null, "PENDING", now.minusMinutes(60), null, null),
                    createTraceabilityWithDate(ORDER_ID_2, "PENDING", "IN_PREPARATION", now.minusMinutes(55), EMPLOYEE_ID_2, EMPLOYEE_EMAIL_2),
                    createTraceabilityWithDate(ORDER_ID_2, "IN_PREPARATION", "DELIVERED", now, EMPLOYEE_ID_2, EMPLOYEE_EMAIL_2)
            );
            when(traceabilityPersistencePort.getTraceabilityByOrderId(ORDER_ID_2)).thenReturn(order2Traces);

            // When
            List<EmployeeRanking> result = traceabilityUseCase.getEmployeeRankingByRestaurant(RESTAURANT_ID);

            // Then
            assertEquals(2, result.size());
            
            // Employee 1 should be first (faster)
            EmployeeRanking firstRanked = result.get(0);
            assertEquals(EMPLOYEE_ID_1, firstRanked.getEmployeeId());
            assertEquals(1, firstRanked.getRankingPosition());
            assertTrue(firstRanked.getAverageDurationInMinutes() < 35);

            // Employee 2 should be second (slower)
            EmployeeRanking secondRanked = result.get(1);
            assertEquals(EMPLOYEE_ID_2, secondRanked.getEmployeeId());
            assertEquals(2, secondRanked.getRankingPosition());
            assertTrue(secondRanked.getAverageDurationInMinutes() > 55);
        }

        @Test
        @DisplayName("Should calculate average for employee with multiple orders")
        void shouldCalculateAverageForEmployeeWithMultipleOrders() {
            // Given
            LocalDateTime now = LocalDateTime.now();

            List<Long> orderIds = Arrays.asList(ORDER_ID_1, ORDER_ID_2);
            when(traceabilityPersistencePort.getDistinctOrderIdsByRestaurantId(RESTAURANT_ID)).thenReturn(orderIds);

            // Same employee handles both orders - 20 minutes and 40 minutes
            List<Traceability> order1Traces = Arrays.asList(
                    createTraceabilityWithDate(ORDER_ID_1, null, "PENDING", now.minusMinutes(20), null, null),
                    createTraceabilityWithDate(ORDER_ID_1, "PENDING", "DELIVERED", now, EMPLOYEE_ID_1, EMPLOYEE_EMAIL_1)
            );
            when(traceabilityPersistencePort.getTraceabilityByOrderId(ORDER_ID_1)).thenReturn(order1Traces);

            List<Traceability> order2Traces = Arrays.asList(
                    createTraceabilityWithDate(ORDER_ID_2, null, "PENDING", now.minusMinutes(40), null, null),
                    createTraceabilityWithDate(ORDER_ID_2, "PENDING", "DELIVERED", now, EMPLOYEE_ID_1, EMPLOYEE_EMAIL_1)
            );
            when(traceabilityPersistencePort.getTraceabilityByOrderId(ORDER_ID_2)).thenReturn(order2Traces);

            // When
            List<EmployeeRanking> result = traceabilityUseCase.getEmployeeRankingByRestaurant(RESTAURANT_ID);

            // Then
            assertEquals(1, result.size());
            EmployeeRanking ranking = result.get(0);
            assertEquals(EMPLOYEE_ID_1, ranking.getEmployeeId());
            assertEquals(2L, ranking.getTotalOrdersCompleted());
            // Average should be approximately 30 minutes (20 + 40) / 2
            assertTrue(ranking.getAverageDurationInMinutes() >= 29 && ranking.getAverageDurationInMinutes() <= 31);
        }

        @Test
        @DisplayName("Should return empty list when no completed orders")
        void shouldReturnEmptyListWhenNoCompletedOrders() {
            // Given
            when(traceabilityPersistencePort.getDistinctOrderIdsByRestaurantId(RESTAURANT_ID))
                    .thenReturn(Collections.emptyList());

            // When
            List<EmployeeRanking> result = traceabilityUseCase.getEmployeeRankingByRestaurant(RESTAURANT_ID);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    // Helper methods
    private Traceability createTraceability(Long orderId, String previousStatus, String newStatus) {
        Traceability traceability = new Traceability();
        traceability.setOrderId(orderId);
        traceability.setClientId(CLIENT_ID);
        traceability.setClientEmail(CLIENT_EMAIL);
        traceability.setPreviousStatus(previousStatus);
        traceability.setNewStatus(newStatus);
        traceability.setRestaurantId(RESTAURANT_ID);
        traceability.setDate(LocalDateTime.now());
        return traceability;
    }

    private Traceability createTraceabilityWithDate(Long orderId, String previousStatus, String newStatus, 
                                                     LocalDateTime date, Long employeeId, String employeeEmail) {
        Traceability traceability = new Traceability();
        traceability.setOrderId(orderId);
        traceability.setClientId(CLIENT_ID);
        traceability.setClientEmail(CLIENT_EMAIL);
        traceability.setPreviousStatus(previousStatus);
        traceability.setNewStatus(newStatus);
        traceability.setRestaurantId(RESTAURANT_ID);
        traceability.setDate(date);
        traceability.setEmployeeId(employeeId);
        traceability.setEmployeeEmail(employeeEmail);
        return traceability;
    }
}
