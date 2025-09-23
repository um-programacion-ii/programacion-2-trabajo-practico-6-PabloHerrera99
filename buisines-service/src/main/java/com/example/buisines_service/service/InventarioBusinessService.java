package com.example.buisines_service.service;

import com.example.buisines_service.Exception.InventarioNoEncontradoException;
import com.example.buisines_service.Exception.MicroserviceCommunicationException;
import com.example.buisines_service.Exception.ValidacionNegocioException;
import com.example.buisines_service.client.DataServiceClient;
import com.example.buisines_service.dto.InventarioDTO;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InventarioBusinessService {
    private final DataServiceClient dataServiceClient;

    public InventarioBusinessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    public List<InventarioDTO> obtenerTodosLosInventarios() {
        try {
            return dataServiceClient.obtenerTodosLosInventarios();
        } catch (FeignException e) {
            log.error("Error al obtener inventarios del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public InventarioDTO obtenerInventarioPorId(Long id) {
        try {
            return dataServiceClient.obtenerInventarioPorId(id);
        } catch (FeignException.NotFound e) {
            throw new InventarioNoEncontradoException("Inventario no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al obtener inventario del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }


    public InventarioDTO crearInventario(InventarioDTO inventarioDTO) {
        validarInventario(inventarioDTO);
        try {
            return dataServiceClient.crearInventario(inventarioDTO);
        } catch (FeignException e) {
            log.error("Error al crear inventario en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public InventarioDTO actualizarInventario(Long id, InventarioDTO inventarioDTO) {
        validarInventario(inventarioDTO);
        try {
            return dataServiceClient.actualizarInventario(id, inventarioDTO);
        } catch (FeignException.NotFound e) {
            throw new InventarioNoEncontradoException("Inventario no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al actualizar inventario en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public void eliminarInventario(Long id) {
        try {
            dataServiceClient.eliminarInventario(id);
        } catch (FeignException.NotFound e) {
            throw new InventarioNoEncontradoException("Inventario no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al eliminar inventario en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    private void validarInventario(InventarioDTO inventarioDTO) {
        if (inventarioDTO.getCantidad() != null && inventarioDTO.getCantidad() < 0) {
            throw new ValidacionNegocioException("La cantidad no puede ser negativa");
        }
        if (inventarioDTO.getStockMinimo() != null && inventarioDTO.getStockMinimo() < 0) {
            throw new ValidacionNegocioException("El stock mínimo no puede ser negativo");
        }
    }
}
