package com.example.buisines_service.service;

import com.example.buisines_service.Exception.MicroserviceCommunicationException;
import com.example.buisines_service.client.DataServiceClient;
import com.example.buisines_service.dto.CategoriaDTO;
import com.example.buisines_service.dto.ProductoDTO;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoriaBuisnessService {
    private final DataServiceClient dataServiceClient;

    public CategoriaBuisnessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    public List<CategoriaDTO> obtenerTodasLasCategorias() {
        try {
            return dataServiceClient.obtenerTodasLasCategorias();
        } catch (FeignException e) {
            log.error("Error al obtener categoria del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        try {
            return dataServiceClient.actualizarCategoria(id, categoriaDTO);
        } catch (FeignException e) {
            log.error("Error al actualizar categoria del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public void eliminarCategoria(Long id) {
        try {
            dataServiceClient.eliminarCategoria(id);
        } catch (FeignException e) {
            log.error("Error al eliminar categoria del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }
}
