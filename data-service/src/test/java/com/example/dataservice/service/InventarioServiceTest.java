package com.example.dataservice.service;
import com.example.dataservice.entity.Inventario;
import com.example.dataservice.exception.InventarioNoEncontradoException;
import com.example.dataservice.repository.InventarioRepository;
import com.example.dataservice.service.InventarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCantidad(10);
        inventario.setStockMinimo(5);
    }

    @Test
    void testCalcularValorTotal() {
        when(inventarioRepository.calculateTotalValue()).thenReturn(BigDecimal.valueOf(5000));

        BigDecimal total = inventarioService.calcularValorTotal();

        assertEquals(BigDecimal.valueOf(5000), total);
        verify(inventarioRepository, times(1)).calculateTotalValue();
    }

    @Test
    void testObtenerProductosConStockBajo() {
        when(inventarioRepository.obtenerProductosConStockBajo()).thenReturn(Arrays.asList(inventario));

        List<Inventario> result = inventarioService.obtenerProductosConStockBajo();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testGuardar() {
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario result = inventarioService.guardar(inventario);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void testActualizarExistente() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        Inventario actualizado = new Inventario();
        actualizado.setCantidad(20);

        Inventario result = inventarioService.actualizar(1L, actualizado);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(inventarioRepository).save(actualizado);
    }

    @Test
    void testActualizarNoExistente() {
        when(inventarioRepository.existsById(99L)).thenReturn(false);

        Inventario nuevo = new Inventario();
        nuevo.setCantidad(5);

        assertThrows(InventarioNoEncontradoException.class, () -> {
            inventarioService.actualizar(99L, nuevo);
        });
    }

    @Test
    void testEliminarExistente() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);

        inventarioService.eliminar(1L);

        verify(inventarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarNoExistente() {
        when(inventarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(InventarioNoEncontradoException.class, () -> {
            inventarioService.eliminar(99L);
        });
    }

    @Test
    void testBuscarPorIdExistente() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        Inventario result = inventarioService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testBuscarPorIdNoExistente() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InventarioNoEncontradoException.class, () -> {
            inventarioService.buscarPorId(99L);
        });
    }

    @Test
    void testObtenerTodos() {
        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(inventario));

        List<Inventario> result = inventarioService.obtenerTodos();

        assertEquals(1, result.size());
    }
}

