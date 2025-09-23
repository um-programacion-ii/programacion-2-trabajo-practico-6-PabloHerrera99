package com.example.dataservice.service;


import com.example.dataservice.entity.Producto;
import com.example.dataservice.exception.ProductoNoEncontradoException;
import com.example.dataservice.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService{
    private ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> encontrarPorPrecioEntre(BigDecimal min, BigDecimal max){
        return productoRepository.findByPrecioBetween(min, max);
    }

    @Override
    public Producto guardar(Producto producto){
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizar(Long id, Producto producto){
        if(!productoRepository.existsById(id)){
            throw new ProductoNoEncontradoException(id);
        }
        producto.setId(id);
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long id){
        if(!productoRepository.existsById(id)){
            throw new ProductoNoEncontradoException(id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> obtenerTodos(){
        return productoRepository.findAll();
    }

    @Override
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }
}
