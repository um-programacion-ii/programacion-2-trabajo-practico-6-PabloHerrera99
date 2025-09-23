package com.example.dataservice.service;

import com.example.dataservice.entity.Producto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {
    List<Producto> encontrarPorPrecioEntre(BigDecimal min, BigDecimal max);
    Producto guardar(Producto producto);
    Producto actualizar(Long id, Producto producto);
    void eliminar(Long id);
    List<Producto>obtenerTodos();
    Producto buscarPorId(Long id);
}
