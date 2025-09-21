package com.example.buisines_service.service;

import com.example.buisines_service.Exception.CategoriaNoEncontradaException;
import com.example.buisines_service.Exception.MicroserviceCommunicationException;
import com.example.buisines_service.Exception.ValidacionNegocioException;
import com.example.buisines_service.client.DataServiceClient;
import com.example.buisines_service.dto.CategoriaDTO;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoriaBusinessService {
    private final DataServiceClient dataServiceClient;

    public CategoriaBusinessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    public List<CategoriaDTO> obtenerTodasLasCategorias() {
        try {
            return dataServiceClient.obtenerTodasLasCategorias();
        } catch (FeignException e) {
            log.error("Error al obtener categorías del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        try {
            return dataServiceClient.obtenerCategoriaPorId(id);
        } catch (FeignException.NotFound e) {
            throw new CategoriaNoEncontradaException("Categoría no encontrada con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al obtener categoría del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        validarCategoria(categoriaDTO);

        try {
            return dataServiceClient.crearCategoria(categoriaDTO);
        } catch (FeignException e) {
            log.error("Error al crear categoría en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        validarCategoria(categoriaDTO);

        try {
            return dataServiceClient.actualizarCategoria(id, categoriaDTO);
        } catch (FeignException.NotFound e) {
            throw new CategoriaNoEncontradaException("Categoría no encontrada con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al actualizar categoría en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public void eliminarCategoria(Long id) {
        try {
            dataServiceClient.eliminarCategoria(id);
        } catch (FeignException.NotFound e) {
            throw new CategoriaNoEncontradaException("Categoría no encontrada con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al eliminar categoría en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public List<CategoriaDTO> obtenerCategoriasConProductos() {
        try {
            return dataServiceClient.obtenerCategoriasConProductos();
        } catch (FeignException e) {
            log.error("Error al obtener categorías con productos en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    private void validarCategoria(CategoriaDTO categoriaDTO) {
        if (categoriaDTO.getNombre() == null || categoriaDTO.getNombre().isBlank()) {
            throw new ValidacionNegocioException("El nombre de la categoría no puede estar vacío");
        }
    }
}
