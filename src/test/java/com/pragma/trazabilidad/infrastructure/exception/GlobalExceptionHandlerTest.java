package com.pragma.trazabilidad.infrastructure.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidationExceptions_ShouldReturnBadRequestWithSortedMessage() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");

        bindingResult.addError(new FieldError("request", "name", "z-error"));
        bindingResult.addError(new FieldError("request", "email", "a-error"));
        bindingResult.addError(new FieldError("request", "email", "duplicated-email-error"));

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(request.getRequestURI()).thenReturn("/traceability");

        var response = handler.handleValidationExceptions(ex, request);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Validation Error", response.getBody().getError());
        assertEquals("a-error", response.getBody().getMessage());
        assertEquals("/traceability", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleValidationExceptions_ShouldUseObjectNameForNonFieldErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");

        bindingResult.addError(new ObjectError("traceabilityRequest", "object-level-error"));

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(request.getRequestURI()).thenReturn("/traceability/validate");

        var response = handler.handleValidationExceptions(ex, request);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("object-level-error", response.getBody().getMessage());
    }

    @Test
    void handleValidationExceptions_ShouldReturnDefaultMessageWhenNoErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        BeanPropertyBindingResult bindingResult = mock(BeanPropertyBindingResult.class);

        when(bindingResult.getAllErrors()).thenReturn(List.of());
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(request.getRequestURI()).thenReturn("/traceability/empty-errors");

        var response = handler.handleValidationExceptions(ex, request);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Input data validation error", response.getBody().getMessage());
    }

    @Test
    void handleAccessDeniedException_ShouldReturnForbiddenResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/traceability/secure");

        var response = handler.handleAccessDeniedException(new AccessDeniedException("forbidden"), request);

        assertEquals(403, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Forbidden", response.getBody().getError());
        assertEquals("You do not have permission to access this resource", response.getBody().getMessage());
        assertEquals("/traceability/secure", response.getBody().getPath());
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerErrorResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/traceability/fail");

        var response = handler.handleGlobalException(new RuntimeException("unexpected failure"), request);

        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("unexpected failure", response.getBody().getMessage());
        assertEquals("/traceability/fail", response.getBody().getPath());
    }
}
