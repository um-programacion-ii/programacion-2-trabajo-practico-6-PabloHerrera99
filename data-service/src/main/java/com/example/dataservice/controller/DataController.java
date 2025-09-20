package com.example.dataservice.controller;

import com.example.dataservice.entity.Categoria;
import com.example.dataservice.entity.Inventario;
import com.example.dataservice.entity.Producto;
import com.example.dataservice.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/data")
@Validated
public class DataController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final InventarioService inventarioService;

    public DataController(ProductoService productoService,
                          CategoriaService categoriaService,
                          InventarioService inventarioService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.inventarioService = inventarioService;
    }

    // Productos

    @GetMapping("/productos")
    public List<Producto> obtenerTodosLosProductos() {
        return productoService.obtenerTodos();
    }

    @GetMapping("/productos/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }


    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    @PutMapping("/productos/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizar(id, producto);
    }

    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
    }

    // Categoria

    @GetMapping("/categorias")
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaService.obtenerTodos();
    }

    @GetMapping("/categorias/id/{id}")
    public Categoria obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @GetMapping("/categorias/con-productos")
    public List<Categoria> obtenerCategoriasConProductos() {
        return categoriaService.buscarCategoriasConProductos();
    }

    @PostMapping("/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        return categoriaService.guardar(categoria);
    }

    @PutMapping("/categorias/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.actualizar(id, categoria);
    }

    @DeleteMapping("/categorias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }

    // Inventario

    @GetMapping("/inventario")
    public List<Inventario> obtenerTodosLosInventarios() {
        return inventarioService.obtenerTodos();
    }

    @GetMapping("/inventario/{id}")
    public Inventario obtenerInventarioPorId(@PathVariable Long id) {
        return inventarioService.buscarPorId(id);
    }

    @PostMapping("/inventario")
    @ResponseStatus(HttpStatus.CREATED)
    public Inventario crearInventario( @RequestBody Inventario inventario) {
        return inventarioService.guardar(inventario);
    }

    @PutMapping("/inventario/{id}")
    public Inventario actualizarInventario(@PathVariable Long id, @RequestBody Inventario inventario) {
        return inventarioService.actualizar(id, inventario);
    }

    @DeleteMapping("/inventario/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminar(id);
    }

    @GetMapping("/inventario/stock-bajo")
    public List<Inventario> obtenerProductosConStockBajo() {
        return inventarioService.obtenerProductosConStockBajo();
    }

    @GetMapping("Inventario/Valor-Total")
    public BigDecimal calcularValorTotal() {
        return inventarioService.calcularValorTotal();
    }
}