package com.example.dataservice.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.dataservice.entity.Producto;
import com.example.dataservice.exception.ProductoNoEncontradoException;
import com.example.dataservice.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setPrecio(BigDecimal.valueOf(1000));
    }

    @Test
    void testGuardar() {
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto result = productoService.guardar(producto);

        assertNotNull(result);
        assertEquals("Laptop", result.getNombre());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testBuscarPorIdExistente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto result = productoService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testBuscarPorIdNoExistente() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class, () -> {
            productoService.buscarPorId(99L);
        });
    }

    @Test
    void testActualizarExistente() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // ✅ devuelve el actualizado

        Producto actualizado = new Producto();
        actualizado.setNombre("Tablet");
        actualizado.setPrecio(BigDecimal.valueOf(500));

        Producto result = productoService.actualizar(1L, actualizado);

        assertNotNull(result);
        assertEquals("Tablet", result.getNombre());
        verify(productoRepository).save(actualizado);
    }

    @Test
    void testActualizarNoExistente() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        Producto nuevo = new Producto();
        nuevo.setNombre("Celular");

        assertThrows(ProductoNoEncontradoException.class, () -> {
            productoService.actualizar(99L, nuevo);
        });
    }

    @Test
    void testEliminarExistente() {
        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.eliminar(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarNoExistente() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProductoNoEncontradoException.class, () -> {
            productoService.eliminar(99L);
        });
    }

    @Test
    void testEncontrarPorPrecioEntre() {
        when(productoRepository.findByPrecioBetween(BigDecimal.valueOf(500), BigDecimal.valueOf(1500)))
                .thenReturn(Arrays.asList(producto));

        List<Producto> resultados = productoService.encontrarPorPrecioEntre(
                BigDecimal.valueOf(500), BigDecimal.valueOf(1500));

        assertEquals(1, resultados.size());
        assertEquals("Laptop", resultados.get(0).getNombre());
    }

    @Test
    void testObtenerTodos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));

        List<Producto> resultados = productoService.obtenerTodos();

        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
    }
}
