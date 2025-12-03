package com.pragma.trazabilidad.infrastructure.output.mongodb.mapper;

import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.infrastructure.output.mongodb.document.TraceabilityDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TraceabilityEntityMapper {
    TraceabilityDocument toDocument(Traceability traceability);
    Traceability toModel(TraceabilityDocument traceabilityDocument);
    List<Traceability> toModelList(List<TraceabilityDocument> traceabilityDocuments);
}
