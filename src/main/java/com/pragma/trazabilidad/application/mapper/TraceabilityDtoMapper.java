package com.pragma.trazabilidad.application.mapper;

import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.domain.model.TraceabilityOrderItem;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityOrderItemDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityRequestDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TraceabilityDtoMapper {
    @Mapping(target = "date", expression = "java(java.time.LocalDateTime.now())")
    Traceability toModel(TraceabilityRequestDto traceabilityRequestDto);
    TraceabilityResponseDto toResponse(Traceability traceability);
    List<TraceabilityResponseDto> toResponseList(List<Traceability> traceabilityList);
    TraceabilityOrderItem toOrderItemModel(TraceabilityOrderItemDto traceabilityOrderItemDto);
    List<TraceabilityOrderItem> toOrderItemModelList(List<TraceabilityOrderItemDto> traceabilityOrderItemDtos);
    TraceabilityOrderItemDto toOrderItemDto(TraceabilityOrderItem traceabilityOrderItem);
    List<TraceabilityOrderItemDto> toOrderItemDtoList(List<TraceabilityOrderItem> traceabilityOrderItems);
}
