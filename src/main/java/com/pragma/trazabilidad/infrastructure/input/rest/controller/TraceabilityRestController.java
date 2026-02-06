package com.pragma.trazabilidad.infrastructure.input.rest.controller;

import com.pragma.trazabilidad.infrastructure.input.rest.adapter.TraceabilityRestInputAdapter;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.EmployeeRankingResponseDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.OrderEfficiencyResponseDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityRequestDto;
import com.pragma.trazabilidad.infrastructure.input.rest.dto.TraceabilityResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traceability")
@RequiredArgsConstructor
@Tag(name = "Traceability", description = "API para gestión de trazabilidad y eficiencia de pedidos")
public class TraceabilityRestController {

    private final TraceabilityRestInputAdapter traceabilityRestInputAdapter;

    @Operation(summary = "Save traceability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Traceability created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> saveTraceability(@RequestBody TraceabilityRequestDto traceabilityRequestDto) {
        traceabilityRestInputAdapter.saveTraceability(traceabilityRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get traceability by order id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Traceability found", content = @Content(schema = @Schema(implementation = TraceabilityResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Traceability not found", content = @Content)
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<List<TraceabilityResponseDto>> getTraceabilityByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(traceabilityRestInputAdapter.getTraceabilityByOrderId(orderId));
    }

    @Operation(summary = "Get traceability by client id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Traceability found", content = @Content(schema = @Schema(implementation = TraceabilityResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Traceability not found", content = @Content)
    })
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<TraceabilityResponseDto>> getTraceabilityByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(traceabilityRestInputAdapter.getTraceabilityByClientId(clientId));
    }

    @Operation(summary = "Get orders efficiency by restaurant",
            description = "Retrieves the efficiency of all orders for a specific restaurant. " +
                    "Shows the time between order creation and completion for each order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders efficiency retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderEfficiencyResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @GetMapping("/efficiency/restaurant/{restaurantId}/orders")
    public ResponseEntity<List<OrderEfficiencyResponseDto>> getOrdersEfficiencyByRestaurant(
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(traceabilityRestInputAdapter.getOrdersEfficiencyByRestaurant(restaurantId));
    }

    @Operation(summary = "Get employee ranking by restaurant",
            description = "Retrieves the ranking of employees by average order completion time. " +
                    "Employees are ranked from fastest (position 1) to slowest.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee ranking retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeRankingResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    })
    @GetMapping("/efficiency/restaurant/{restaurantId}/employees")
    public ResponseEntity<List<EmployeeRankingResponseDto>> getEmployeeRankingByRestaurant(
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(traceabilityRestInputAdapter.getEmployeeRankingByRestaurant(restaurantId));
    }
}
