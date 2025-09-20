package com.example.dataservice.exception;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException(Long id) {
        super("NO se encontro el producto con el id: " + id);
    }
}
