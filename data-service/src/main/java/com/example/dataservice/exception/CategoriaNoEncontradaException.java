package com.example.dataservice.exception;

public class CategoriaNoEncontradaException extends RuntimeException {
    public CategoriaNoEncontradaException(Long id) {
        super("NO se encontro el categoria con el id: " + id);
    }
}
