package com.pragma.trazabilidad.infrastructure.output.mongodb.mapper;

import com.pragma.trazabilidad.domain.model.Traceability;
import com.pragma.trazabilidad.domain.model.TraceabilityOrderItem;
import com.pragma.trazabilidad.infrastructure.output.mongodb.document.TraceabilityDocument;
import com.pragma.trazabilidad.infrastructure.output.mongodb.document.TraceabilityOrderItemDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TraceabilityEntityMapper {
    TraceabilityDocument toDocument(Traceability traceability);
    Traceability toModel(TraceabilityDocument traceabilityDocument);
    List<Traceability> toModelList(List<TraceabilityDocument> traceabilityDocuments);
    TraceabilityOrderItemDocument toOrderItemDocument(TraceabilityOrderItem traceabilityOrderItem);
    TraceabilityOrderItem toOrderItemModel(TraceabilityOrderItemDocument traceabilityOrderItemDocument);
    List<TraceabilityOrderItem> toOrderItemModelList(List<TraceabilityOrderItemDocument> traceabilityOrderItemDocuments);
    List<TraceabilityOrderItemDocument> toOrderItemDocumentList(List<TraceabilityOrderItem> traceabilityOrderItems);
}
