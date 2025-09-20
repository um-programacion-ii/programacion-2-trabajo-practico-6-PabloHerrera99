package com.example.dataservice.repository;

import com.example.dataservice.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto>  findbyPrecioBetween(BigDecimal min, BigDecimal max);

    @Query("SELECT p FROM Producto p JOIN p.inventario i WHERE i.cantidad < i.stockMinimo")
    List<Producto> findProductosWithLowStock();
}
