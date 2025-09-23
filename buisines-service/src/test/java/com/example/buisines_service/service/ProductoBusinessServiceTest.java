package com.example.buisines_service.service;

import com.example.buisines_service.Exception.MicroserviceCommunicationException;
import com.example.buisines_service.Exception.ProductoNoEncontradoException;
import com.example.buisines_service.Exception.ValidacionNegocioException;
import com.example.buisines_service.client.DataServiceClient;
import com.example.buisines_service.dto.ProductoDTO;
import com.example.buisines_service.dto.ProductoRequest;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient;

    @InjectMocks
    private ProductoBusinessService businessService;

    private ProductoDTO productoDTO;
    private ProductoRequest productoRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Laptop");
        productoDTO.setPrecio(BigDecimal.valueOf(1000));
        productoDTO.setStock(5);

        productoRequest = new ProductoRequest();
        productoRequest.setNombre("Laptop");
        productoRequest.setPrecio(BigDecimal.valueOf(1000));
        productoRequest.setStock(5);
    }

    @Test
    void testObtenerTodosLosProductos() {
        when(dataServiceClient.obtenerTodosLosProductos()).thenReturn(Arrays.asList(productoDTO));

        List<ProductoDTO> result = businessService.obtenerTodosLosProductos();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getNombre());
    }

    @Test
    void testObtenerProductoPorIdExistente() {
        when(dataServiceClient.obtenerProductoPorId(1L)).thenReturn(productoDTO);

        ProductoDTO result = businessService.obtenerProductoPorId(1L);

        assertEquals("Laptop", result.getNombre());
    }

    @Test
    void testObtenerProductoPorIdNoExistente() {
        when(dataServiceClient.obtenerProductoPorId(2L)).thenThrow(FeignException.NotFound.class);

        assertThrows(ProductoNoEncontradoException.class, () -> businessService.obtenerProductoPorId(2L));
    }

    @Test
    void testCrearProductoValido() {
        when(dataServiceClient.crearProducto(productoRequest)).thenReturn(productoDTO);

        ProductoDTO result = businessService.crearProducto(productoRequest);

        assertEquals("Laptop", result.getNombre());
    }

    @Test
    void testCrearProductoPrecioInvalido() {
        productoRequest.setPrecio(BigDecimal.valueOf(-10));

        assertThrows(ValidacionNegocioException.class, () -> businessService.crearProducto(productoRequest));
    }

    @Test
    void testActualizarProducto() {
        when(dataServiceClient.actualizarProducto(1L, productoRequest)).thenReturn(productoDTO);

        ProductoDTO result = businessService.actualizarProducto(1L, productoRequest);

        assertEquals("Laptop", result.getNombre());
    }

    @Test
    void testEliminarProducto() {
        doNothing().when(dataServiceClient).eliminarProducto(1L);

        assertDoesNotThrow(() -> businessService.eliminarProducto(1L));
        verify(dataServiceClient).eliminarProducto(1L);
    }

    @Test
    void testCalcularValorTotalInventario() {
        when(dataServiceClient.obtenerTodosLosProductos()).thenReturn(Arrays.asList(productoDTO));

        BigDecimal total = businessService.calcularValorTotalInventario();

        assertEquals(BigDecimal.valueOf(5000), total); // 1000 * 5
    }

    @Test
    void testObtenerProductosConStockBajo() {
        when(dataServiceClient.obtenerProductosConStockBajo()).thenReturn(Arrays.asList(productoDTO));

        List<ProductoDTO> result = businessService.obtenerProductosConStockBajo();

        assertEquals(1, result.size());
    }

    @Test
    void testObtenerProductosPorCategoria() {
        when(dataServiceClient.obtenerProductosPorCategoria("Electrónica")).thenReturn(Arrays.asList(productoDTO));

        List<ProductoDTO> result = businessService.obtenerProductosPorCategoria("Electrónica");

        assertEquals(1, result.size());
    }

    @Test
    void testDataServiceFeignException() {
        when(dataServiceClient.obtenerTodosLosProductos()).thenThrow(FeignException.class);

        assertThrows(MicroserviceCommunicationException.class, () -> businessService.obtenerTodosLosProductos());
    }
}

