package com.example.dataservice.service;

import com.example.dataservice.entity.Categoria;

import java.util.List;

public interface CategoriaService {
    Categoria guardar(Categoria categoria);
    Categoria actualizar(Long id, Categoria categoria);
    void eliminar(Long id);
    Categoria buscarPorId(Long id);
    List<Categoria> buscarCategoriasConProductos();
    List<Categoria> obtenerTodos();
}
