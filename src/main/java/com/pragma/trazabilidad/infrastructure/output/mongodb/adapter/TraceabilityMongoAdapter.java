package com.pragma.trazabilidad.infrastructure.output.mongodb.adapter;

import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.domain.spi.ITraceabilityPersistencePort;
import com.pragma.trazabilidad.infrastructure.output.mongodb.document.TraceabilityDocument;
import com.pragma.trazabilidad.infrastructure.output.mongodb.mapper.TraceabilityEntityMapper;
import com.pragma.trazabilidad.infrastructure.output.mongodb.repository.ITraceabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TraceabilityMongoAdapter implements ITraceabilityPersistencePort {

    private final ITraceabilityRepository traceabilityRepository;
    private final TraceabilityEntityMapper traceabilityEntityMapper;

    @Override
    public Traceability saveTraceability(Traceability traceability) {
        TraceabilityDocument document = traceabilityEntityMapper.toDocument(traceability);
        return traceabilityEntityMapper.toModel(traceabilityRepository.save(document));
    }

    @Override
    public List<Traceability> getTraceabilityByOrderId(Long orderId) {
        return traceabilityEntityMapper.toModelList(traceabilityRepository.findAllByOrderId(orderId));
    }

    @Override
    public List<Traceability> getTraceabilityByClientId(Long clientId) {
        return traceabilityEntityMapper.toModelList(traceabilityRepository.findAllByClientId(clientId));
    }

    @Override
    public List<Traceability> getTraceabilityByRestaurantId(Long restaurantId) {
        return traceabilityEntityMapper.toModelList(traceabilityRepository.findAllByRestaurantId(restaurantId));
    }

    @Override
    public List<Long> getDistinctOrderIdsByRestaurantId(Long restaurantId) {
        return traceabilityRepository.findDistinctOrderIdsByRestaurantId(restaurantId);
    }
}
