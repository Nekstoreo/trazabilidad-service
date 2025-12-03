package com.pragma.trazabilidad.application.mapper;

import com.pragma.trazabilidad.domain.model.EmployeeRanking;
import com.pragma.trazabilidad.domain.model.OrderEfficiency;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.EmployeeRankingResponseDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.OrderEfficiencyResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EfficiencyDtoMapper {
    
    OrderEfficiencyResponseDto toOrderEfficiencyDto(OrderEfficiency orderEfficiency);
    List<OrderEfficiencyResponseDto> toOrderEfficiencyDtoList(List<OrderEfficiency> orderEfficiencyList);
    
    EmployeeRankingResponseDto toEmployeeRankingDto(EmployeeRanking employeeRanking);
    List<EmployeeRankingResponseDto> toEmployeeRankingDtoList(List<EmployeeRanking> employeeRankingList);
}
