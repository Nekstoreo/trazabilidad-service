package com.pragma.trazabilidad.infrastructure.input.rest.adapter;

import com.pragma.trazabilidad.domain.api.ITraceabilityServicePort;
import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.EmployeeRankingResponseDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.OrderEfficiencyResponseDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityRequestDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityResponseDto;
import com.pragma.trazabilidad.application.mapper.EfficiencyDtoMapper;
import com.pragma.trazabilidad.application.mapper.TraceabilityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TraceabilityRestInputAdapter {

    private final ITraceabilityServicePort traceabilityServicePort;
    private final TraceabilityDtoMapper traceabilityDtoMapper;
    private final EfficiencyDtoMapper efficiencyDtoMapper;

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

    public List<OrderEfficiencyResponseDto> getOrdersEfficiencyByRestaurant(Long restaurantId) {
        return efficiencyDtoMapper.toOrderEfficiencyDtoList(
                traceabilityServicePort.getOrdersEfficiencyByRestaurant(restaurantId)
        );
    }

    public List<EmployeeRankingResponseDto> getEmployeeRankingByRestaurant(Long restaurantId) {
        return efficiencyDtoMapper.toEmployeeRankingDtoList(
                traceabilityServicePort.getEmployeeRankingByRestaurant(restaurantId)
        );
    }
}
