package com.example.dataservice.service;

import com.example.dataservice.entity.Producto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {
    List<Producto> findbyPrecioBetween(BigDecimal min, BigDecimal max);
    List<Producto> findProductosWithLowStock();
    Producto save(Producto producto);
    Producto update(Long id, Producto producto);
    void delete(Long id);
    List<Producto>findAll();
}
