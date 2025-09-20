package com.example.dataservice.service;


import com.example.dataservice.entity.Producto;
import com.example.dataservice.exception.ProductoNoEncontradoException;
import com.example.dataservice.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService{
    private ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> findbyPrecioBetween(BigDecimal min, BigDecimal max){
        return productoRepository.findbyPrecioBetween(min, max);
    }

    @Override
    public List<Producto> findProductosWithLowStock(){
        return productoRepository.findProductosWithLowStock();
    }

    @Override
    public Producto save(Producto producto){
        return productoRepository.save(producto);
    }

    @Override
    public Producto update(Long id, Producto producto){
        if(!productoRepository.existsById(id)){
            throw new ProductoNoEncontradoException(id);
        }
        producto.setId(id);
        return productoRepository.save(producto);
    }

    @Override
    public void delete(Long id){
        if(!productoRepository.existsById(id)){
            throw new ProductoNoEncontradoException(id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> findAll(){
        return productoRepository.findAll();
    }
}
