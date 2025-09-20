package com.example.dataservice.service;

import com.example.dataservice.entity.Inventario;

import java.math.BigDecimal;
import java.util.List;

public interface InventarioService {
    BigDecimal calcularValorTotal();
    List<Inventario> obtenerProductosConStockBajo();
    Inventario guardar(Inventario inventario);
    Inventario actualizar(Long id, Inventario inventario);
    void eliminar(Long id);
    Inventario buscarPorId(Long id);
    List<Inventario> obtenerTodos();
}
