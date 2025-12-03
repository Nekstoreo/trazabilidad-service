package com.pragma.trazabilidad.domain.api;

import com.pragma.trazabilidad.domain.model.Traceability;
import java.util.List;

public interface ITraceabilityServicePort {
    void saveTraceability(Traceability traceability);
    List<Traceability> getTraceabilityByOrderId(Long orderId);
    List<Traceability> getTraceabilityByClientId(Long clientId);
}
