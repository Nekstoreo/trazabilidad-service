package com.pragma.trazabilidad.domain.spi;

import com.pragma.trazabilidad.domain.model.Traceability;
import java.util.List;

public interface ITraceabilityPersistencePort {
    Traceability saveTraceability(Traceability traceability);
    List<Traceability> getTraceabilityByOrderId(Long orderId);
    List<Traceability> getTraceabilityByClientId(Long clientId);
}
