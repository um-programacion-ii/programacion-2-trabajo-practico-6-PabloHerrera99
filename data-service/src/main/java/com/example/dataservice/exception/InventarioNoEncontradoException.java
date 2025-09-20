package com.example.dataservice.exception;

public class InventarioNoEncontradoException extends RuntimeException {
    public InventarioNoEncontradoException(Long id) {
        super("NO se encontro el inventario con el id: " + id);
    }
}
