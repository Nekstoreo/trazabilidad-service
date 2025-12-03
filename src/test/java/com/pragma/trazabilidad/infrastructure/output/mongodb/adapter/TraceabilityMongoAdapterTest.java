package com.pragma.trazabilidad.infrastructure.output.mongodb.adapter;

import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.infrastructure.output.mongodb.document.TraceabilityDocument;
import com.pragma.trazabilidad.infrastructure.output.mongodb.mapper.TraceabilityEntityMapper;
import com.pragma.trazabilidad.infrastructure.output.mongodb.repository.ITraceabilityRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraceabilityMongoAdapterTest {

    @Mock
    private ITraceabilityRepository traceabilityRepository;

    @Mock
    private TraceabilityEntityMapper traceabilityEntityMapper;

    @InjectMocks
    private TraceabilityMongoAdapter traceabilityMongoAdapter;

    private static final Long ORDER_ID = 100L;
    private static final Long CLIENT_ID = 10L;
    private static final Long RESTAURANT_ID = 1L;
    private static final String DOCUMENT_ID = "doc-id-1";

    @Nested
    @DisplayName("Tests for saveTraceability")
    class SaveTraceabilityTests {

        @Test
        @DisplayName("Should save traceability successfully")
        void shouldSaveTraceabilitySuccessfully() {
            // Given
            Traceability traceability = createTraceability();
            TraceabilityDocument document = createDocument();
            TraceabilityDocument savedDocument = createDocument();
            savedDocument.setId(DOCUMENT_ID);
            Traceability savedTraceability = createTraceability();
            savedTraceability.setId(DOCUMENT_ID);

            when(traceabilityEntityMapper.toDocument(traceability)).thenReturn(document);
            when(traceabilityRepository.save(document)).thenReturn(savedDocument);
            when(traceabilityEntityMapper.toModel(savedDocument)).thenReturn(savedTraceability);

            // When
            Traceability result = traceabilityMongoAdapter.saveTraceability(traceability);

            // Then
            assertNotNull(result);
            assertEquals(DOCUMENT_ID, result.getId());
            verify(traceabilityEntityMapper, times(1)).toDocument(traceability);
            verify(traceabilityRepository, times(1)).save(document);
            verify(traceabilityEntityMapper, times(1)).toModel(savedDocument);
        }
    }

    @Nested
    @DisplayName("Tests for getTraceabilityByOrderId")
    class GetTraceabilityByOrderIdTests {

        @Test
        @DisplayName("Should return traceability list by order id")
        void shouldReturnTraceabilityListByOrderId() {
            // Given
            List<TraceabilityDocument> documents = Arrays.asList(createDocument(), createDocument());
            List<Traceability> traceabilities = Arrays.asList(createTraceability(), createTraceability());

            when(traceabilityRepository.findAllByOrderId(ORDER_ID)).thenReturn(documents);
            when(traceabilityEntityMapper.toModelList(documents)).thenReturn(traceabilities);

            // When
            List<Traceability> result = traceabilityMongoAdapter.getTraceabilityByOrderId(ORDER_ID);

            // Then
            assertEquals(2, result.size());
            verify(traceabilityRepository, times(1)).findAllByOrderId(ORDER_ID);
            verify(traceabilityEntityMapper, times(1)).toModelList(documents);
        }

        @Test
        @DisplayName("Should return empty list when no documents found")
        void shouldReturnEmptyListWhenNoDocumentsFound() {
            // Given
            when(traceabilityRepository.findAllByOrderId(ORDER_ID)).thenReturn(Collections.emptyList());
            when(traceabilityEntityMapper.toModelList(Collections.emptyList())).thenReturn(Collections.emptyList());

            // When
            List<Traceability> result = traceabilityMongoAdapter.getTraceabilityByOrderId(ORDER_ID);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for getTraceabilityByClientId")
    class GetTraceabilityByClientIdTests {

        @Test
        @DisplayName("Should return traceability list by client id")
        void shouldReturnTraceabilityListByClientId() {
            // Given
            List<TraceabilityDocument> documents = Arrays.asList(createDocument());
            List<Traceability> traceabilities = Arrays.asList(createTraceability());

            when(traceabilityRepository.findAllByClientId(CLIENT_ID)).thenReturn(documents);
            when(traceabilityEntityMapper.toModelList(documents)).thenReturn(traceabilities);

            // When
            List<Traceability> result = traceabilityMongoAdapter.getTraceabilityByClientId(CLIENT_ID);

            // Then
            assertEquals(1, result.size());
            verify(traceabilityRepository, times(1)).findAllByClientId(CLIENT_ID);
        }
    }

    @Nested
    @DisplayName("Tests for getTraceabilityByRestaurantId")
    class GetTraceabilityByRestaurantIdTests {

        @Test
        @DisplayName("Should return traceability list by restaurant id")
        void shouldReturnTraceabilityListByRestaurantId() {
            // Given
            List<TraceabilityDocument> documents = Arrays.asList(createDocument(), createDocument());
            List<Traceability> traceabilities = Arrays.asList(createTraceability(), createTraceability());

            when(traceabilityRepository.findAllByRestaurantId(RESTAURANT_ID)).thenReturn(documents);
            when(traceabilityEntityMapper.toModelList(documents)).thenReturn(traceabilities);

            // When
            List<Traceability> result = traceabilityMongoAdapter.getTraceabilityByRestaurantId(RESTAURANT_ID);

            // Then
            assertEquals(2, result.size());
            verify(traceabilityRepository, times(1)).findAllByRestaurantId(RESTAURANT_ID);
            verify(traceabilityEntityMapper, times(1)).toModelList(documents);
        }
    }

    @Nested
    @DisplayName("Tests for getDistinctOrderIdsByRestaurantId")
    class GetDistinctOrderIdsByRestaurantIdTests {

        @Test
        @DisplayName("Should return distinct order ids by restaurant id")
        void shouldReturnDistinctOrderIdsByRestaurantId() {
            // Given
            List<Long> orderIds = Arrays.asList(100L, 101L, 102L);
            when(traceabilityRepository.findDistinctOrderIdsByRestaurantId(RESTAURANT_ID)).thenReturn(orderIds);

            // When
            List<Long> result = traceabilityMongoAdapter.getDistinctOrderIdsByRestaurantId(RESTAURANT_ID);

            // Then
            assertEquals(3, result.size());
            assertTrue(result.containsAll(orderIds));
            verify(traceabilityRepository, times(1)).findDistinctOrderIdsByRestaurantId(RESTAURANT_ID);
        }

        @Test
        @DisplayName("Should return empty list when no orders found")
        void shouldReturnEmptyListWhenNoOrdersFound() {
            // Given
            when(traceabilityRepository.findDistinctOrderIdsByRestaurantId(RESTAURANT_ID))
                    .thenReturn(Collections.emptyList());

            // When
            List<Long> result = traceabilityMongoAdapter.getDistinctOrderIdsByRestaurantId(RESTAURANT_ID);

            // Then
            assertTrue(result.isEmpty());
        }
    }

    // Helper methods
    private Traceability createTraceability() {
        Traceability traceability = new Traceability();
        traceability.setOrderId(ORDER_ID);
        traceability.setClientId(CLIENT_ID);
        traceability.setClientEmail("client@test.com");
        traceability.setNewStatus("PENDING");
        traceability.setRestaurantId(RESTAURANT_ID);
        traceability.setDate(LocalDateTime.now());
        return traceability;
    }

    private TraceabilityDocument createDocument() {
        TraceabilityDocument document = new TraceabilityDocument();
        document.setOrderId(ORDER_ID);
        document.setClientId(CLIENT_ID);
        document.setClientEmail("client@test.com");
        document.setNewStatus("PENDING");
        document.setRestaurantId(RESTAURANT_ID);
        document.setDate(LocalDateTime.now());
        return document;
    }
}
