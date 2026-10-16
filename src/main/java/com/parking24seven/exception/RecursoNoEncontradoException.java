package com.parking24seven.exception;

/**
 * Excepción de negocio para recursos que no existen.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
