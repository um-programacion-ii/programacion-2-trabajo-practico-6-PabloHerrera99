package com.example.dataservice.service;

import com.example.dataservice.entity.Inventario;
import com.example.dataservice.exception.InventarioNoEncontradoException;
import com.example.dataservice.repository.InventarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService{
    private final InventarioRepository inventarioRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository){
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public BigDecimal calcularValorTotal(){
        return inventarioRepository.calculateTotalValue();
    }

    @Override
    public List<Inventario> obtenerProductosConStockBajo(){
        return inventarioRepository.obtenerProductosConStockBajo();
    }

    @Override
    public Inventario guardar(Inventario inventario){
        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario actualizar(Long id, Inventario inventario){
        if(!inventarioRepository.existsById(id)){
            throw new InventarioNoEncontradoException(id);
        }
        inventario.setId(id);
        return inventarioRepository.save(inventario);
    }

    @Override
    public void eliminar(Long id){
        if(!inventarioRepository.existsById(id)){
            throw new InventarioNoEncontradoException(id);
        }
        inventarioRepository.deleteById(id);
    }

    @Override
    public Inventario buscarPorId(Long id){
        return inventarioRepository.findById(id).orElseThrow(()-> new InventarioNoEncontradoException(id));
    }

    @Override
    public List<Inventario> obtenerTodos(){
        return inventarioRepository.findAll();
    }

}
