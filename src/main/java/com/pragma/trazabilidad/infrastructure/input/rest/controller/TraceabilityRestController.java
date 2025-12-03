package com.pragma.trazabilidad.infrastructure.input.rest.controller;

import com.pragma.trazabilidad.application.handler.TraceabilityHandler;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityRequestDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traceability")
@RequiredArgsConstructor
public class TraceabilityRestController {

    private final TraceabilityHandler traceabilityHandler;

    @Operation(summary = "Save traceability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Traceability created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> saveTraceability(@RequestBody TraceabilityRequestDto traceabilityRequestDto) {
        traceabilityHandler.saveTraceability(traceabilityRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get traceability by order id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Traceability found", content = @Content(schema = @Schema(implementation = TraceabilityResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Traceability not found", content = @Content)
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<List<TraceabilityResponseDto>> getTraceabilityByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(traceabilityHandler.getTraceabilityByOrderId(orderId));
    }

    @Operation(summary = "Get traceability by client id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Traceability found", content = @Content(schema = @Schema(implementation = TraceabilityResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Traceability not found", content = @Content)
    })
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<TraceabilityResponseDto>> getTraceabilityByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(traceabilityHandler.getTraceabilityByClientId(clientId));
    }
}
