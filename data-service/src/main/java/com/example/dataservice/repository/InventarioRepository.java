package com.example.dataservice.repository;

import com.example.dataservice.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    @Query("SELECT SUM(i.cantidad * p.precio) FROM Inventario i JOIN i.producto p")
    BigDecimal calculateTotalValue();
}
