package com.example.dataservice.repository;

import com.example.dataservice.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query("SELECT c FROM Categoria c WHERE SIZE(c.productos) > 0")
    List<Categoria> findCategoriasConProductos();
}
