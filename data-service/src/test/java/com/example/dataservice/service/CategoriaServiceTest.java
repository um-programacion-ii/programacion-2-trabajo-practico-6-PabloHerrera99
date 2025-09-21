package com.example.dataservice.service;

import com.example.dataservice.entity.Categoria;
import com.example.dataservice.exception.CategoriaNoEncontradaException;
import com.example.dataservice.repository.CategoriaRepository;
import com.example.dataservice.service.CategoriaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Electrónica");
    }

    @Test
    void testGuardar() {
        when(categoriaRepository.save(categoria)).thenAnswer(inv -> inv.getArgument(0));

        Categoria result = categoriaService.guardar(categoria);

        assertNotNull(result);
        assertEquals("Electrónica", result.getNombre());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void testActualizarExistente() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(categoriaRepository.save(any(Categoria.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Categoria nueva = new Categoria();
        nueva.setNombre("Hogar");

        Categoria result = categoriaService.actualizar(1L, nueva);

        assertNotNull(result);
        assertEquals("Hogar", result.getNombre());
        assertEquals(1L, result.getId());
        verify(categoriaRepository).save(nueva);
    }

    @Test
    void testActualizarNoExistente() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        Categoria nueva = new Categoria();
        nueva.setNombre("Ropa");

        assertThrows(CategoriaNoEncontradaException.class, () -> {
            categoriaService.actualizar(99L, nueva);
        });
    }

    @Test
    void testEliminarExistente() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        categoriaService.eliminar(1L);

        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void testEliminarNoExistente() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        assertThrows(CategoriaNoEncontradaException.class, () -> {
            categoriaService.eliminar(99L);
        });
    }

    @Test
    void testBuscarPorIdExistente() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria result = categoriaService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals("Electrónica", result.getNombre());
    }

    @Test
    void testBuscarPorIdNoExistente() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontradaException.class, () -> {
            categoriaService.buscarPorId(99L);
        });
    }

    @Test
    void testBuscarCategoriasConProductos() {
        when(categoriaRepository.findCategoriasConProductos()).thenReturn(Arrays.asList(categoria));

        List<Categoria> result = categoriaService.buscarCategoriasConProductos();

        assertEquals(1, result.size());
        assertEquals("Electrónica", result.get(0).getNombre());
    }

    @Test
    void testObtenerTodos() {
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoria));

        List<Categoria> result = categoriaService.obtenerTodos();

        assertEquals(1, result.size());
    }
}

