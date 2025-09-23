package com.example.buisines_service.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<Object> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<Object> handleCategoriaNoEncontrada(CategoriaNoEncontradaException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InventarioNoEncontradoException.class)
    public ResponseEntity<Object> handleInventarioNoEncontrado(InventarioNoEncontradoException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ValidacionNegocioException.class)
    public ResponseEntity<Object> handleValidacionNegocio(ValidacionNegocioException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MicroserviceCommunicationException.class)
    public ResponseEntity<Object> handleMicroserviceCommunication(MicroserviceCommunicationException ex) {
        return buildResponseEntity(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + ex.getMessage());
    }
}