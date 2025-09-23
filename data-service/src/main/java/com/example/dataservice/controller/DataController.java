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

    // Obtener todos los productos
    // Método: GET /productos
    // Retorna una lista con todos los productos registrados
    @GetMapping("/productos")
    public List<Producto> obtenerTodosLosProductos() {
        return productoService.obtenerTodos();
    }

    // Obtener producto por ID
    // Método: GET /productos/{id}
    // Retorna un producto específico según su ID
    @GetMapping("/productos/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }

    // Crear producto
    // Método: POST /productos
    // Crea un nuevo producto
    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    // Actualizar producto
    // Método: PUT /productos/{id}
    // Actualiza un producto existente según su ID
    @PutMapping("/productos/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizar(id, producto);
    }

    // Eliminar producto
    // Método: DELETE /productos/{id}
    // Elimina un producto existente según su ID
    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
    }

    // Categoria

    // Obtener todas las categorías
    // Método: GET /categorias
    // Retorna una lista con todas las categorías registradas
    @GetMapping("/categorias")
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaService.obtenerTodos();
    }

    // Obtener categoría por ID
    // Método: GET /categorias/id/{id}
    // Retorna una categoría específica según su ID
    @GetMapping("/categorias/id/{id}")
    public Categoria obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    // Obtener categorías con productos
    // Método: GET /categorias/con-productos
    // Retorna todas las categorías junto con sus productos asociados
    @GetMapping("/categorias/con-productos")
    public List<Categoria> obtenerCategoriasConProductos() {
        return categoriaService.buscarCategoriasConProductos();
    }

    // Crear categoría
    // Método: POST /categorias
    // Crea una nueva categoría
    @PostMapping("/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        return categoriaService.guardar(categoria);
    }

    // Actualizar categoría
    // Método: PUT /categorias/{id}
    // Actualiza una categoría existente según su ID
    @PutMapping("/categorias/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.actualizar(id, categoria);
    }

    // Eliminar categoría
    // Método: DELETE /categorias/{id}
    // Elimina una categoría existente según su ID
    @DeleteMapping("/categorias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }

    // Inventario


    // Obtener todos los inventarios
    // Método: GET /inventario
    // Retorna una lista con todos los inventarios registrados
    @GetMapping("/inventario")
    public List<Inventario> obtenerTodosLosInventarios() {
        return inventarioService.obtenerTodos();
    }

    // Obtener inventario por ID
    // Método: GET /inventario/{id}
    // Retorna un inventario específico según su ID
    @GetMapping("/inventario/{id}")
    public Inventario obtenerInventarioPorId(@PathVariable Long id) {
        return inventarioService.buscarPorId(id);
    }


    // Crear inventario
    // Método: POST /inventario
    // Crea un nuevo registro de inventario
    @PostMapping("/inventario")
    @ResponseStatus(HttpStatus.CREATED)
    public Inventario crearInventario( @RequestBody Inventario inventario) {
        return inventarioService.guardar(inventario);
    }

    // Actualizar inventario
    // Método: PUT /inventario/{id}
    // Actualiza un inventario existente según su ID
    @PutMapping("/inventario/{id}")
    public Inventario actualizarInventario(@PathVariable Long id, @RequestBody Inventario inventario) {
        return inventarioService.actualizar(id, inventario);
    }

    // Eliminar inventario
    // Método: DELETE /inventario/{id}
    // Elimina un inventario existente según su ID
    @DeleteMapping("/inventario/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminar(id);
    }

    // Obtener productos con stock bajo
    // Método: GET /inventario/stock-bajo
    // Retorna los productos cuyo stock está por debajo del mínimo configurado
    @GetMapping("/inventario/stock-bajo")
    public List<Inventario> obtenerProductosConStockBajo() {
        return inventarioService.obtenerProductosConStockBajo();
    }


    // Calcular valor total del inventario
    // Método: GET /inventario/Valor-Total
    // Retorna el valor monetario total de todos los productos en inventario
    @GetMapping("inventario/Valor-Total")
    public BigDecimal calcularValorTotal() {
        return inventarioService.calcularValorTotal();
    }
}