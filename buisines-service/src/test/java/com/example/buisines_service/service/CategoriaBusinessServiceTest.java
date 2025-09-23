package com.example.buisines_service.service;
import com.example.buisines_service.Exception.CategoriaNoEncontradaException;
import com.example.buisines_service.Exception.MicroserviceCommunicationException;
import com.example.buisines_service.Exception.ValidacionNegocioException;
import com.example.buisines_service.client.DataServiceClient;
import com.example.buisines_service.dto.CategoriaDTO;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient;

    @InjectMocks
    private CategoriaBusinessService categoriaService;

    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setNombre("Electrónica");
    }

    @Test
    void testObtenerTodasLasCategorias() {
        when(dataServiceClient.obtenerTodasLasCategorias()).thenReturn(Arrays.asList(categoriaDTO));

        List<CategoriaDTO> result = categoriaService.obtenerTodasLasCategorias();

        assertEquals(1, result.size());
        assertEquals("Electrónica", result.get(0).getNombre());
    }

    @Test
    void testObtenerCategoriaPorIdExistente() {
        when(dataServiceClient.obtenerCategoriaPorId(1L)).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.obtenerCategoriaPorId(1L);

        assertEquals("Electrónica", result.getNombre());
    }

    @Test
    void testObtenerCategoriaPorIdNoExistente() {
        when(dataServiceClient.obtenerCategoriaPorId(2L)).thenThrow(FeignException.NotFound.class);

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.obtenerCategoriaPorId(2L));
    }

    @Test
    void testCrearCategoriaValida() {
        when(dataServiceClient.crearCategoria(categoriaDTO)).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.crearCategoria(categoriaDTO);

        assertEquals("Electrónica", result.getNombre());
    }

    @Test
    void testCrearCategoriaNombreVacio() {
        categoriaDTO.setNombre("");

        assertThrows(ValidacionNegocioException.class, () -> categoriaService.crearCategoria(categoriaDTO));
    }

    @Test
    void testActualizarCategoriaExistente() {
        when(dataServiceClient.actualizarCategoria(1L, categoriaDTO)).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.actualizarCategoria(1L, categoriaDTO);

        assertEquals("Electrónica", result.getNombre());
    }

    @Test
    void testActualizarCategoriaNoExistente() {
        when(dataServiceClient.actualizarCategoria(2L, categoriaDTO)).thenThrow(FeignException.NotFound.class);

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.actualizarCategoria(2L, categoriaDTO));
    }

    @Test
    void testEliminarCategoriaExistente() {
        doNothing().when(dataServiceClient).eliminarCategoria(1L);

        assertDoesNotThrow(() -> categoriaService.eliminarCategoria(1L));
        verify(dataServiceClient).eliminarCategoria(1L);
    }

    @Test
    void testEliminarCategoriaNoExistente() {
        doThrow(FeignException.NotFound.class).when(dataServiceClient).eliminarCategoria(2L);

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.eliminarCategoria(2L));
    }

    @Test
    void testObtenerCategoriasConProductos() {
        when(dataServiceClient.obtenerCategoriasConProductos()).thenReturn(Arrays.asList(categoriaDTO));

        List<CategoriaDTO> result = categoriaService.obtenerCategoriasConProductos();

        assertEquals(1, result.size());
    }

    @Test
    void testFeignExceptionGeneral() {
        when(dataServiceClient.obtenerTodasLasCategorias()).thenThrow(FeignException.class);

        assertThrows(MicroserviceCommunicationException.class, () -> categoriaService.obtenerTodasLasCategorias());
    }
}

