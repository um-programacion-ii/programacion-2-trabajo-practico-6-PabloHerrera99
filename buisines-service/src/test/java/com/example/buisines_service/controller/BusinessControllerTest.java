package com.example.buisines_service.controller;

import com.example.buisines_service.Exception.ValidacionNegocioException;
import com.example.buisines_service.dto.CategoriaDTO;
import com.example.buisines_service.dto.InventarioDTO;
import com.example.buisines_service.dto.ProductoDTO;
import com.example.buisines_service.dto.ProductoRequest;
import com.example.buisines_service.service.CategoriaBusinessService;
import com.example.buisines_service.service.InventarioBusinessService;
import com.example.buisines_service.service.ProductoBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoBusinessService productoService;

    @MockitoBean
    private CategoriaBusinessService categoriaService;

    @MockitoBean
    private InventarioBusinessService inventarioService;

// Producto

    @Test
    void testObtenerTodosLosProductos() throws Exception {
        ProductoDTO producto = new ProductoDTO();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setPrecio(BigDecimal.valueOf(1000));

        when(productoService.obtenerTodosLosProductos()).thenReturn(Collections.singletonList(producto));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Laptop"))
                .andExpect(jsonPath("$[0].precio").value(1000));
    }

    @Test
    void testObtenerProductoPorId() throws Exception {
        ProductoDTO producto = new ProductoDTO();
        producto.setId(1L);
        producto.setNombre("Laptop");

        when(productoService.obtenerProductoPorId(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCrearProducto() throws Exception {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Tablet");
        request.setPrecio(BigDecimal.TEN);
        request.setStock(5);
        request.setCategoriaId(1L);

        ProductoDTO response = new ProductoDTO();
        response.setId(1L);
        response.setNombre("Tablet");

        when(productoService.crearProducto(any(ProductoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Tablet"));
    }

    @Test
    void testActualizarProducto() throws Exception {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("TabletPro");
        request.setPrecio(BigDecimal.TEN);
        request.setStock(10);
        request.setCategoriaId(1L);

        ProductoDTO response = new ProductoDTO();
        response.setId(1L);
        response.setNombre("TabletPro");

        when(productoService.actualizarProducto(eq(1L), any(ProductoRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("TabletPro"));
    }

    @Test
    void testEliminarProducto() throws Exception {
        doNothing().when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

//Categoria

    @Test
    void testObtenerTodasLasCategorias() throws Exception {
        CategoriaDTO cat = new CategoriaDTO();
        cat.setId(1L);
        cat.setNombre("Electrónica");

        when(categoriaService.obtenerTodasLasCategorias()).thenReturn(Collections.singletonList(cat));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Electrónica"));
    }

    @Test
    void testObtenerCategoriaPorId() throws Exception {
        CategoriaDTO cat = new CategoriaDTO();
        cat.setId(1L);
        cat.setNombre("Electrónica");

        when(categoriaService.obtenerCategoriaPorId(1L)).thenReturn(cat);

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Electrónica"));
    }

    @Test
    void testCrearCategoria() throws Exception {
        CategoriaDTO cat = new CategoriaDTO();
        cat.setNombre("Hogar");

        when(categoriaService.crearCategoria(any(CategoriaDTO.class))).thenAnswer(invocation -> {
            CategoriaDTO c = invocation.getArgument(0);
            c.setId(2L);
            return c;
        });

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Hogar"));
    }

    @Test
    void testActualizarCategoria() throws Exception {
        CategoriaDTO cat = new CategoriaDTO();
        cat.setNombre("Hogar y Cocina");

        when(categoriaService.actualizarCategoria(eq(1L), any(CategoriaDTO.class))).thenAnswer(invocation -> {
            CategoriaDTO c = invocation.getArgument(1);
            c.setId(1L);
            return c;
        });

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Hogar y Cocina"));
    }

    @Test
    void testEliminarCategoria() throws Exception {
        doNothing().when(categoriaService).eliminarCategoria(1L);

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

//Inventario

    @Test
    void testObtenerTodosLosInventarios() throws Exception {
        InventarioDTO inv = new InventarioDTO();
        inv.setId(1L);
        inv.setCantidad(10);

        when(inventarioService.obtenerTodosLosInventarios()).thenReturn(Collections.singletonList(inv));

        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cantidad").value(10));
    }

    @Test
    void testObtenerInventarioPorId() throws Exception {
        InventarioDTO inv = new InventarioDTO();
        inv.setId(1L);
        inv.setCantidad(5);

        when(inventarioService.obtenerInventarioPorId(1L)).thenReturn(inv);

        mockMvc.perform(get("/api/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(5));
    }

    @Test
    void testCrearInventario() throws Exception {
        InventarioDTO inv = new InventarioDTO();
        inv.setCantidad(20);

        when(inventarioService.crearInventario(any(InventarioDTO.class))).thenAnswer(invocation -> {
            InventarioDTO i = invocation.getArgument(0);
            i.setId(2L);
            return i;
        });

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inv)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.cantidad").value(20));
    }

    @Test
    void testActualizarInventario() throws Exception {
        InventarioDTO inv = new InventarioDTO();
        inv.setCantidad(50);

        when(inventarioService.actualizarInventario(eq(1L), any(InventarioDTO.class))).thenAnswer(invocation -> {
            InventarioDTO i = invocation.getArgument(1);
            i.setId(1L);
            return i;
        });

        mockMvc.perform(put("/api/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inv)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(50));
    }

    @Test
    void testEliminarInventario() throws Exception {
        doNothing().when(inventarioService).eliminarInventario(1L);

        mockMvc.perform(delete("/api/inventario/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerProductosConStockBajo() throws Exception {
        ProductoDTO producto = new ProductoDTO();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setPrecio(BigDecimal.valueOf(1000));
        producto.setStock(2);

        when(productoService.obtenerProductosConStockBajo()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/reportes/stock-bajo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].stock").value(2));
    }

    @Test
    void testCalcularValorTotal() throws Exception {
        when(productoService.calcularValorTotalInventario()).thenReturn(BigDecimal.valueOf(1500));

        mockMvc.perform(get("/api/reportes/valor-inventario"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500"));
    }
}
