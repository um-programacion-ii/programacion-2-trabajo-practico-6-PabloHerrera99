package com.example.buisines_service.client;

import com.example.buisines_service.dto.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "data-service", url = "${data.service.url")
public interface DataServiceClient {

// Producto

    @GetMapping("/data/productos")
    List<ProductoDTO> obtenerTodosLosProductos();

    @GetMapping("/data/productos/{id}")
    ProductoDTO obtenerProductoPorId(@PathVariable Long id);

    @PostMapping("/data/productos")
    ProductoDTO crearProducto(@RequestBody ProductoRequest request);

    @PutMapping("/data/productos/{id}")
    ProductoDTO actualizarProducto(@PathVariable Long id, @RequestBody ProductoRequest request);

    @DeleteMapping("/data/productos/{id}")
    void eliminarProducto(@PathVariable Long id);

    @GetMapping("/data/productos/categoria/{nombre}")
    List<ProductoDTO> obtenerProductosPorCategoria(@PathVariable String nombre);

// Categoria

    @GetMapping("/data/categorias")
    List<CategoriaDTO> obtenerTodasLasCategorias();

    @GetMapping("/data/categorias/{id}")
    CategoriaDTO obtenerCategoriaPorId(@PathVariable Long id);

    @PostMapping("/data/categoria")
    CategoriaDTO crearCategoria(@RequestBody  CategoriaDTO categoriaDTO);

    @PutMapping("data/categorias/{id}")
    CategoriaDTO actualizarCategoria(@PathVariable Long id,  @RequestBody CategoriaDTO categoriaDTO);

    @GetMapping("/data/categoria/con-productos")
    List<CategoriaDTO> obtenerCategoriasConProductos();

    @DeleteMapping("data/categorias/{id}")
    void eliminarCategoria(@PathVariable Long id);

// Inventario

    @GetMapping("/data/inventario")
    List<InventarioDTO> obtenerTodosLosInventarios();

    @GetMapping("/data/invetario/{id}")
    InventarioDTO obtenerInventarioPorId(@PathVariable Long id);

    @GetMapping("/data/inventario/stock-bajo")
    List<ProductoDTO> obtenerProductosConStockBajo();

    @PostMapping("/data/inventario")
    InventarioDTO crearInventario(@RequestBody InventarioDTO inventarioDTO);

    @PutMapping("/data/inventario/{id}")
    InventarioDTO actualizarInventario(@PathVariable Long id, @RequestBody InventarioDTO inventarioDTO);

    @DeleteMapping("/data/inventario/{id}")
    void eliminarInventario(@PathVariable Long id);

}
