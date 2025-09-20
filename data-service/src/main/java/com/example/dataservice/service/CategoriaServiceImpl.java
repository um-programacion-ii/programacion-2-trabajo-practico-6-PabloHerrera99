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
    public Categoria guardar(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria actualizar(Long id, Categoria categoria){
        if(!categoriaRepository.existsById(id)){
            throw new CategoriaNoEncontradaException(id);
        }
        categoria.setId(id);
        return categoriaRepository.save(categoria);
    }

    @Override
    public void eliminar(Long id){
        if (!categoriaRepository.existsById(id)){
            throw new CategoriaNoEncontradaException(id);
        }
        categoriaRepository.deleteById(id);
    }

    @Override
    public Categoria buscarPorId(Long id){
        return categoriaRepository.findById(id).orElseThrow(()->new CategoriaNoEncontradaException(id));
    }

    public List<Categoria> buscarCategoriasConProductos() {
        return categoriaRepository.findCategoriasConProductos();
    }

    @Override
    public List<Categoria> obtenerTodos(){
        return categoriaRepository.findAll();
    }
}