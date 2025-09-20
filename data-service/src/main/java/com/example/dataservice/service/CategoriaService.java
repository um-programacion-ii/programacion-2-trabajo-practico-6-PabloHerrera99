package com.example.dataservice.service;

import com.example.dataservice.entity.Categoria;

import java.util.List;

public interface CategoriaService {
    Categoria save(Categoria categoria);
    Categoria update(Long id, Categoria categoria);
    void delete(Long id);
    Categoria findById(Long id);
    List<Categoria> findAll();
}
