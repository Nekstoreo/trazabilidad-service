package com.pragma.trazabilidad.infrastructure.configuration;

import com.pragma.trazabilidad.domain.api.ITraceabilityServicePort;
import com.pragma.trazabilidad.domain.spi.ITraceabilityPersistencePort;
import com.pragma.trazabilidad.domain.usecase.TraceabilityUseCase;
import com.pragma.trazabilidad.infrastructure.output.mongodb.adapter.TraceabilityMongoAdapter;
import com.pragma.trazabilidad.infrastructure.output.mongodb.mapper.TraceabilityEntityMapper;
import com.pragma.trazabilidad.infrastructure.output.mongodb.repository.ITraceabilityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ITraceabilityPersistencePort traceabilityPersistencePort(ITraceabilityRepository traceabilityRepository, TraceabilityEntityMapper traceabilityEntityMapper) {
        return new TraceabilityMongoAdapter(traceabilityRepository, traceabilityEntityMapper);
    }

    @Bean
    public ITraceabilityServicePort traceabilityServicePort(ITraceabilityPersistencePort traceabilityPersistencePort) {
        return new TraceabilityUseCase(traceabilityPersistencePort);
    }
}
