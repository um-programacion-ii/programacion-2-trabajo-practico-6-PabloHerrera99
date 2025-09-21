package com.example.dataservice.controller;

import com.example.dataservice.controller.DataController;
import com.example.dataservice.entity.Producto;
import com.example.dataservice.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DataController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    @Test
    void testObtenerTodosLosProductos() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setPrecio(BigDecimal.valueOf(1000));

        when(productoService.obtenerTodos()).thenReturn(Collections.singletonList(producto));

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
}

