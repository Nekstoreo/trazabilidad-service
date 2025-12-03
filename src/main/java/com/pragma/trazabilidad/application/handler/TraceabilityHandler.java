package com.pragma.trazabilidad.application.handler;

import com.pragma.trazabilidad.application.mapper.TraceabilityDtoMapper;
import com.pragma.trazabilidad.domain.api.ITraceabilityServicePort;
import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityRequestDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TraceabilityHandler {

    private final ITraceabilityServicePort traceabilityServicePort;
    private final TraceabilityDtoMapper traceabilityDtoMapper;

    public void saveTraceability(TraceabilityRequestDto traceabilityRequestDto) {
        Traceability traceability = traceabilityDtoMapper.toModel(traceabilityRequestDto);
        traceabilityServicePort.saveTraceability(traceability);
    }

    public List<TraceabilityResponseDto> getTraceabilityByOrderId(Long orderId) {
        return traceabilityDtoMapper.toResponseList(traceabilityServicePort.getTraceabilityByOrderId(orderId));
    }

    public List<TraceabilityResponseDto> getTraceabilityByClientId(Long clientId) {
        return traceabilityDtoMapper.toResponseList(traceabilityServicePort.getTraceabilityByClientId(clientId));
    }
}
