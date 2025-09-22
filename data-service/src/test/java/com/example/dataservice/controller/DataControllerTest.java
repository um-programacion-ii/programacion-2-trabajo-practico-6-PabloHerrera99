package com.example.dataservice.controller;

import com.example.dataservice.controller.DataController;
import com.example.dataservice.entity.Categoria;
import com.example.dataservice.entity.Inventario;
import com.example.dataservice.entity.Producto;
import com.example.dataservice.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DataController.class)
class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoServiceImpl productoService;

    @MockitoBean
    private CategoriaServiceImpl categoriaService;

    @MockitoBean
    private InventarioServiceImpl inventarioService;

    // Producto
    @Test
    void testObtenerTodosLosProductos() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setPrecio(BigDecimal.valueOf(1000));

        when(productoService.obtenerTodos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/data/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Laptop"))
                .andExpect(jsonPath("$[0].precio").value(1000));
    }

    @Test
    void testObtenerProductoPorId() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");

        when(productoService.buscarPorId(1L)).thenReturn(producto);

        mockMvc.perform(get("/data/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCrearProducto() throws Exception {
        Producto producto = new Producto();
        producto.setNombre("Tablet");
        producto.setPrecio(BigDecimal.valueOf(500));

        when(productoService.guardar(any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(0);
            p.setId(2L);
            return p;
        });

        mockMvc.perform(post("/data/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Tablet"));
    }

    @Test
    void testActualizarProducto() throws Exception {
        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Tablet Pro");
        productoActualizado.setPrecio(BigDecimal.valueOf(800));

        when(productoService.actualizar(eq(1L), any(Producto.class))).thenAnswer(invocation -> {
            Producto p = invocation.getArgument(1);
            p.setId(1L);
            return p;
        });

        mockMvc.perform(put("/data/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Tablet Pro"))
                .andExpect(jsonPath("$.precio").value(800));
    }

    @Test
    void testEliminarProducto() throws Exception {
        doNothing().when(productoService).eliminar(1L);

        mockMvc.perform(delete("/data/productos/1"))
                .andExpect(status().isNoContent());
    }

// Categoria

    @Test
    void testObtenerTodasLasCategorias() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Electrónica");
        categoria.setDescripcion("Dispositivos electrónicos");
        when(categoriaService.obtenerTodos()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/data/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Electrónica"))
                .andExpect(jsonPath("$[0].descripcion").value("Dispositivos electrónicos"));
    }


    @Test
    void testObtenerCategoriaPorId() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Electrónica");
        categoria.setDescripcion("Dispositivos electrónicos");

        when(categoriaService.buscarPorId(1L)).thenReturn(categoria);
        mockMvc.perform(get("/data/categorias/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Electrónica"))
                .andExpect(jsonPath("$.descripcion").value("Dispositivos electrónicos"));
    }

    @Test
    void testObtenerCategoriasConProductos() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Electrónica");
        categoria.setDescripcion("Dispositivos electrónicos");
        when(categoriaService.buscarCategoriasConProductos()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/data/categorias/con-productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Electrónica"));
    }

    @Test
    void testCrearCategoria() throws Exception {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setId(null);
        nuevaCategoria.setNombre("Hogar");
        nuevaCategoria.setDescripcion("Artículos para el hogar");
        when(categoriaService.guardar(any(Categoria.class))).thenAnswer(invocation -> {
            Categoria c = invocation.getArgument(0);
            c.setId(2L);
            return c;
        });

        mockMvc.perform(post("/data/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaCategoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Hogar"));
    }

    @Test
    void testActualizarCategoria() throws Exception {
        Categoria categoriaActualizada = new Categoria();
        categoriaActualizada.setNombre("Electrónica Avanzada");
        categoriaActualizada.setDescripcion("Gadgets y dispositivos");
        when(categoriaService.actualizar(anyLong(), any(Categoria.class))).thenAnswer(invocation -> {
            Categoria c = invocation.getArgument(1);
            c.setId(1L);
            return c;
        });

        mockMvc.perform(put("/data/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Electrónica Avanzada"))
                .andExpect(jsonPath("$.descripcion").value("Gadgets y dispositivos"));
    }

    @Test
    void testEliminarCategoria() throws Exception {
        doNothing().when(categoriaService).eliminar(1L);

        mockMvc.perform(delete("/data/categorias/1"))
                .andExpect(status().isNoContent());
    }

// Inventario

    @Test
    void testObtenerTodosLosInventarios() throws Exception {
        Inventario inv = new Inventario();
        inv.setId(1L);
        inv.setCantidad(10);

        when(inventarioService.obtenerTodos()).thenReturn(List.of(inv));

        mockMvc.perform(get("/data/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cantidad").value(10));
    }

    @Test
    void testObtenerInventarioPorId() throws Exception {
        Inventario inv = new Inventario();
        inv.setId(1L);
        inv.setCantidad(5);

        when(inventarioService.buscarPorId(1L)).thenReturn(inv);

        mockMvc.perform(get("/data/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(5));
    }

    @Test
    void testCrearInventario() throws Exception {
        Inventario inv = new Inventario();
        inv.setCantidad(20);

        when(inventarioService.guardar(any(Inventario.class))).thenAnswer(invocation -> {
            Inventario i = invocation.getArgument(0);
            i.setId(2L);
            return i;
        });

        mockMvc.perform(post("/data/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inv)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.cantidad").value(20));
    }

    @Test
    void testActualizarInventario() throws Exception {
        Inventario inv = new Inventario();
        inv.setCantidad(50);

        when(inventarioService.actualizar(eq(1L), any(Inventario.class))).thenAnswer(invocation -> {
            Inventario i = invocation.getArgument(1);
            i.setId(1L);
            return i;
        });

        mockMvc.perform(put("/data/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inv)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(50));
    }

    @Test
    void testEliminarInventario() throws Exception {
        doNothing().when(inventarioService).eliminar(1L);

        mockMvc.perform(delete("/data/inventario/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerProductosConStockBajo() throws Exception {
        Inventario inv = new Inventario();
        inv.setId(1L);
        inv.setCantidad(2);

        when(inventarioService.obtenerProductosConStockBajo()).thenReturn(List.of(inv));

        mockMvc.perform(get("/data/inventario/stock-bajo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cantidad").value(2));
    }

    @Test
    void testCalcularValorTotal() throws Exception {
        when(inventarioService.calcularValorTotal()).thenReturn(BigDecimal.valueOf(1500));

        mockMvc.perform(get("/data/inventario/Valor-Total"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500"));
    }
}