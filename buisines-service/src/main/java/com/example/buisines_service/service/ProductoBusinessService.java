package com.example.buisines_service.service;

import com.example.buisines_service.Exception.MicroserviceCommunicationException;
import com.example.buisines_service.Exception.ProductoNoEncontradoException;
import com.example.buisines_service.Exception.ValidacionNegocioException;
import com.example.buisines_service.client.DataServiceClient;
import com.example.buisines_service.dto.CategoriaDTO;
import com.example.buisines_service.dto.ProductoDTO;
import com.example.buisines_service.dto.ProductoRequest;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ProductoBusinessService {

    private final DataServiceClient dataServiceClient;

    public ProductoBusinessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    public List<ProductoDTO> obtenerTodosLosProductos() {
        try {
            return dataServiceClient.obtenerTodosLosProductos();
        } catch (FeignException e) {
            log.error("Error al obtener productos del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public ProductoDTO obtenerProductoPorId(Long id) {
        try {
            return dataServiceClient.obtenerProductoPorId(id);
        } catch (FeignException.NotFound e) {
            throw new ProductoNoEncontradoException("Producto no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al obtener producto del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public ProductoDTO crearProducto(ProductoRequest request) {
        validarProducto(request);

        try {
            return dataServiceClient.crearProducto(request);
        } catch (FeignException e) {
            log.error("Error al crear producto en el microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public ProductoDTO actualizarProducto(Long id, ProductoRequest request) {
        validarProducto(request);
        try {
            return dataServiceClient.actualizarProducto(id, request);
        } catch (FeignException e) {
            log.error("Error al actualizar el producto del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public void eliminarProducto(Long id) {
        try {
            dataServiceClient.eliminarProducto(id);
        } catch (FeignException e) {
            log.error("Error al eliminar el producto del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public List<ProductoDTO> obtenerProductosPorCategoria(String nombreCategoria) {
        try {
            return dataServiceClient.obtenerProductosPorCategoria(nombreCategoria);
        } catch (FeignException e) {
            log.error("Error al obtener productos por categoría del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public List<ProductoDTO> obtenerProductosConStockBajo() {
        try {
            return dataServiceClient.obtenerProductosConStockBajo();
        } catch (FeignException e) {
            log.error("Error al obtener productos con stock bajo del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    public BigDecimal calcularValorTotalInventario() {
        try {
            List<ProductoDTO> productos = dataServiceClient.obtenerTodosLosProductos();
            return productos.stream()
                    .map(p -> p.getPrecio().multiply(BigDecimal.valueOf(p.getStock())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (FeignException e) {
            log.error("Error al calcular valor total del inventario desde data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    private void validarProducto(ProductoRequest request) {
        if (request.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacionNegocioException("El precio debe ser mayor a cero");
        }

        if (request.getStock() < 0) {
            throw new ValidacionNegocioException("El stock no puede ser negativo");
        }
    }
}
