package com.parking24seven.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centraliza las respuestas de error de la API REST.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarSolicitudIncorrecta(IllegalArgumentException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus estado, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("fecha", LocalDateTime.now());
        body.put("estado", estado.value());
        body.put("error", estado.getReasonPhrase());
        body.put("mensaje", mensaje);
        return ResponseEntity.status(estado).body(body);
    }
}
