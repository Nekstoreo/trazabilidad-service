package com.pragma.trazabilidad.domain.usecase;

import com.pragma.trazabilidad.domain.api.ITraceabilityServicePort;
import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.domain.spi.ITraceabilityPersistencePort;

import java.util.List;

public class TraceabilityUseCase implements ITraceabilityServicePort {

    private final ITraceabilityPersistencePort traceabilityPersistencePort;

    public TraceabilityUseCase(ITraceabilityPersistencePort traceabilityPersistencePort) {
        this.traceabilityPersistencePort = traceabilityPersistencePort;
    }

    @Override
    public void saveTraceability(Traceability traceability) {
        traceabilityPersistencePort.saveTraceability(traceability);
    }

    @Override
    public List<Traceability> getTraceabilityByOrderId(Long orderId) {
        return traceabilityPersistencePort.getTraceabilityByOrderId(orderId);
    }

    @Override
    public List<Traceability> getTraceabilityByClientId(Long clientId) {
        return traceabilityPersistencePort.getTraceabilityByClientId(clientId);
    }
}
