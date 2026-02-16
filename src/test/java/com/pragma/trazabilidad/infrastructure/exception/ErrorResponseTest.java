package com.pragma.trazabilidad.infrastructure.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void noArgsConstructorAndSetters_ShouldAssignAllFields() {
        ErrorResponse response = new ErrorResponse();
        LocalDateTime now = LocalDateTime.now();

        response.setStatus(400);
        response.setError("Validation Error");
        response.setMessage("Invalid input");
        response.setPath("/traceability");
        response.setTimestamp(now);

        assertEquals(400, response.getStatus());
        assertEquals("Validation Error", response.getError());
        assertEquals("Invalid input", response.getMessage());
        assertEquals("/traceability", response.getPath());
        assertEquals(now, response.getTimestamp());
    }

    @Test
    void allArgsConstructor_ShouldAssignAllFields() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse response = new ErrorResponse(
                403,
                "Forbidden",
                "No permission",
                "/traceability/secure",
                now
        );

        assertEquals(403, response.getStatus());
        assertEquals("Forbidden", response.getError());
        assertEquals("No permission", response.getMessage());
        assertEquals("/traceability/secure", response.getPath());
        assertEquals(now, response.getTimestamp());
    }

    @Test
    void convenienceConstructor_ShouldInitializeTimestamp() {
        ErrorResponse response = new ErrorResponse(500, "Internal Server Error", "boom", "/traceability/fail");

        assertEquals(500, response.getStatus());
        assertEquals("Internal Server Error", response.getError());
        assertEquals("boom", response.getMessage());
        assertEquals("/traceability/fail", response.getPath());
        assertNotNull(response.getTimestamp());
    }
}
