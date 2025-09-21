package com.example.buisines_service.service;

import com.example.buisines_service.Exception.InventarioNoEncontradoException;
import com.example.buisines_service.Exception.MicroserviceCommunicationException;
import com.example.buisines_service.Exception.ValidacionNegocioException;
import com.example.buisines_service.client.DataServiceClient;
import com.example.buisines_service.dto.InventarioDTO;
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

class InventarioBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient;

    @InjectMocks
    private InventarioBusinessService inventarioService;

    private InventarioDTO inventarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        inventarioDTO = new InventarioDTO();
        inventarioDTO.setId(1L);
        inventarioDTO.setCantidad(10);
        inventarioDTO.setStockMinimo(5);
    }

    @Test
    void testObtenerTodosLosInventarios() {
        when(dataServiceClient.obtenerTodosLosInventarios()).thenReturn(Arrays.asList(inventarioDTO));

        List<InventarioDTO> result = inventarioService.obtenerTodosLosInventarios();

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getCantidad());
    }

    @Test
    void testObtenerInventarioPorIdExistente() {
        when(dataServiceClient.obtenerInventarioPorId(1L)).thenReturn(inventarioDTO);

        InventarioDTO result = inventarioService.obtenerInventarioPorId(1L);

        assertEquals(10, result.getCantidad());
    }

    @Test
    void testObtenerInventarioPorIdNoExistente() {
        when(dataServiceClient.obtenerInventarioPorId(2L)).thenThrow(FeignException.NotFound.class);

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.obtenerInventarioPorId(2L));
    }

    @Test
    void testCrearInventarioValido() {
        when(dataServiceClient.crearInventario(inventarioDTO)).thenReturn(inventarioDTO);

        InventarioDTO result = inventarioService.crearInventario(inventarioDTO);

        assertEquals(10, result.getCantidad());
    }

    @Test
    void testCrearInventarioCantidadNegativa() {
        inventarioDTO.setCantidad(-1);

        assertThrows(ValidacionNegocioException.class, () -> inventarioService.crearInventario(inventarioDTO));
    }

    @Test
    void testActualizarInventarioExistente() {
        when(dataServiceClient.actualizarInventario(1L, inventarioDTO)).thenReturn(inventarioDTO);

        InventarioDTO result = inventarioService.actualizarInventario(1L, inventarioDTO);

        assertEquals(10, result.getCantidad());
    }

    @Test
    void testActualizarInventarioNoExistente() {
        when(dataServiceClient.actualizarInventario(2L, inventarioDTO)).thenThrow(FeignException.NotFound.class);

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.actualizarInventario(2L, inventarioDTO));
    }

    @Test
    void testEliminarInventarioExistente() {
        doNothing().when(dataServiceClient).eliminarInventario(1L);

        assertDoesNotThrow(() -> inventarioService.eliminarInventario(1L));
        verify(dataServiceClient).eliminarInventario(1L);
    }

    @Test
    void testEliminarInventarioNoExistente() {
        doThrow(FeignException.NotFound.class).when(dataServiceClient).eliminarInventario(2L);

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.eliminarInventario(2L));
    }

    @Test
    void testObtenerTodosInventariosFeignException() {
        when(dataServiceClient.obtenerTodosLosInventarios()).thenThrow(FeignException.class);

        assertThrows(MicroserviceCommunicationException.class, () -> inventarioService.obtenerTodosLosInventarios());
    }
}

