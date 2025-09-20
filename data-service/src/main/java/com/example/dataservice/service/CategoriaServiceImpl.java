package com.example.dataservice.service;

import com.example.dataservice.entity.Categoria;
import com.example.dataservice.exception.CategoriaNoEncontradaException;
import com.example.dataservice.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService{
    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria save(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria update(Long id, Categoria categoria){
        if(!categoriaRepository.existsById(id)){
            throw new CategoriaNoEncontradaException(id);
        }
        categoria.setId(id);
        return categoriaRepository.save(categoria);
    }

    @Override
    public void delete(Long id){
        if (!categoriaRepository.existsById(id)){
            throw new CategoriaNoEncontradaException(id);
        }
        categoriaRepository.deleteById(id);
    }

    @Override
    public Categoria findById(Long id){
        return categoriaRepository.findById(id).orElseThrow(()->new CategoriaNoEncontradaException(id));
    }

    @Override
    public List<Categoria> findAll(){
        return categoriaRepository.findAll();
    }
}