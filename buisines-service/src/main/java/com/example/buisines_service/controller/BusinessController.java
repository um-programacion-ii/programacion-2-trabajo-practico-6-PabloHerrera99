package com.example.buisines_service.controller;

import com.example.buisines_service.dto.*;
import com.example.buisines_service.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class BusinessController {

    private final ProductoBusinessService productoBusinessService;
    private final CategoriaBusinessService categoriaBusinessService;
    private final InventarioBusinessService inventarioBusinessService;

    public BusinessController(ProductoBusinessService productoBusinessService,
                              CategoriaBusinessService categoriaBusinessService,
                              InventarioBusinessService inventarioBusinessService) {
        this.productoBusinessService = productoBusinessService;
        this.categoriaBusinessService = categoriaBusinessService;
        this.inventarioBusinessService = inventarioBusinessService;
    }
// Producto
    @GetMapping("/productos")
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoBusinessService.obtenerTodosLosProductos();
    }

    @GetMapping("/productos/{id}")
    public ProductoDTO obtenerProductoPorId(@PathVariable Long id) {
        return productoBusinessService.obtenerProductoPorId(id);
    }

    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDTO crearProducto( @RequestBody ProductoRequest request) {
        return productoBusinessService.crearProducto(request);
    }

    @PutMapping("/productos/{id}")
    public ProductoDTO actualizarProducto(@PathVariable Long id, @RequestBody ProductoRequest request) {
        return productoBusinessService.actualizarProducto(id, request);
    }

    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id) {
        productoBusinessService.eliminarProducto(id);
    }

    @GetMapping("/productos/categoria/{nombre}")
    public List<ProductoDTO> obtenerProductosPorCategoria(@PathVariable String nombre) {
        return productoBusinessService.obtenerProductosPorCategoria(nombre);
    }

// Categoria

    @GetMapping("/categorias")
    public List<CategoriaDTO> obtenerTodasLasCategorias() {
        return categoriaBusinessService.obtenerTodasLasCategorias();
    }

    @GetMapping("/categorias/{id}")
    public CategoriaDTO obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaBusinessService.obtenerCategoriaPorId(id);
    }

    @GetMapping("/categorias/con-productos")
    public List<CategoriaDTO> obtenerCategoriasConProductos() {
        return categoriaBusinessService.obtenerCategoriasConProductos();
    }

    @PostMapping("/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDTO crearCategoria( @RequestBody CategoriaDTO categoriaDTO) {
        return categoriaBusinessService.crearCategoria(categoriaDTO);
    }

    @PutMapping("/categorias/{id}")
    public CategoriaDTO actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        return categoriaBusinessService.actualizarCategoria(id, categoriaDTO);
    }

    @DeleteMapping("/categorias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaBusinessService.eliminarCategoria(id);
    }

// Inventario

    // Obtener todos los inventarios
    @GetMapping("/inventario")
    public List<InventarioDTO> obtenerTodosLosInventarios() {
        return inventarioBusinessService.obtenerTodosLosInventarios();
    }

    // Obtener inventario por ID
    @GetMapping("/inventario/{id}")
    public InventarioDTO obtenerInventarioPorId(@PathVariable Long id) {
        return inventarioBusinessService.obtenerInventarioPorId(id);
    }

    @PostMapping("/inventario")
    @ResponseStatus(HttpStatus.CREATED)
    public InventarioDTO crearInventario( @RequestBody InventarioDTO inventarioDTO) {
        return inventarioBusinessService.crearInventario(inventarioDTO);
    }

    @PutMapping("/inventario/{id}")
    public InventarioDTO actualizarInventario(@PathVariable Long id, @RequestBody InventarioDTO inventarioDTO) {
        return inventarioBusinessService.actualizarInventario(id, inventarioDTO);
    }

    @DeleteMapping("/inventario/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarInventario(@PathVariable Long id) {
        inventarioBusinessService.eliminarInventario(id);
    }

// Reporte

    @GetMapping("/reportes/stock-bajo")
    public List<ProductoDTO> obtenerProductosConStockBajo() {
        return productoBusinessService.obtenerProductosConStockBajo();
    }

    @GetMapping("/reportes/valor-inventario")
    public BigDecimal obtenerValorTotalInventario() {
        return productoBusinessService.calcularValorTotalInventario();
    }
}