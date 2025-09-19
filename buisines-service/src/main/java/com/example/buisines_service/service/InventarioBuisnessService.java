package com.example.buisines_service.service;

import com.example.buisines_service.Exception.MicroserviceCommunicationException;
import com.example.buisines_service.client.DataServiceClient;
import com.example.buisines_service.dto.InventarioDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InventarioBuisnessService {
    private final DataServiceClient dataServiceClient;

    public InventarioBuisnessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    public List<InventarioDTO> obtenerProductoConStockBajo(){
        try {
            return dataServiceClient.obtenerProductosConStockBajo();
        }catch (MicroserviceCommunicationException e){
            log.error("Error al obtener el inventario", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }
}
