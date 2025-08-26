package com.inditex.priceservice.infrastructure.adapter.input.web;

import com.inditex.priceservice.domain.exception.PriceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "";
        String actualValue = ex.getValue() != null ? ex.getValue().toString() : "";

        String errorMessage = String.format("El parametro '%s' debe ser de tipo '%s' pero el valor fue: '%s'",
                paramName, requiredType, actualValue);

        log.error("Bad request received. Parameter type mismatch: {}", errorMessage, ex);

        return new ResponseEntity<>(Map.of("error", errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<Void> handlePriceNotFound(PriceNotFoundException ex) {
        log.warn("Price not found exception handled", ex);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        log.error("Unhandled exception caught in GlobalExceptionHandler", ex);
        return new ResponseEntity<>(Map.of("error", "An unexpected internal server error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

