package com.pragma.trazabilidad.infrastructure.output.mongodb.repository;

import com.pragma.trazabilidad.infrastructure.output.mongodb.document.TraceabilityDocument;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ITraceabilityRepository extends MongoRepository<TraceabilityDocument, String> {
    List<TraceabilityDocument> findAllByOrderId(Long orderId);
    List<TraceabilityDocument> findAllByClientId(Long clientId);

    List<TraceabilityDocument> findAllByRestaurantId(Long restaurantId);

    @Aggregation(pipeline = {
            "{ $match: { restaurantId: ?0 } }",
            "{ $group: { _id: '$orderId' } }",
            "{ $project: { _id: 0, orderId: '$_id' } }"
    })
    List<Long> findDistinctOrderIdsByRestaurantId(Long restaurantId);
}
