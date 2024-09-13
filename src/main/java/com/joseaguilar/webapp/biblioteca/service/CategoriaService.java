package com.joseaguilar.webapp.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joseaguilar.webapp.biblioteca.model.Categoria;
import com.joseaguilar.webapp.biblioteca.repository.CategoriaRepository;

@Service
public class CategoriaService implements ICategoriaService{
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria buscarCategoriaPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean guardarCategoria(Categoria categoria) {
        if(!verificarCategoriaDuplicado(categoria)){
           categoriaRepository.save(categoria);
           return true;
        }
        return false;
    }

    @Override
    public void eliminarCategoria(Categoria categoria) {
        categoriaRepository.delete(categoria);
    }

    @Override
    public Boolean verificarCategoriaDuplicado(Categoria newCategoria) {
        List<Categoria> categorias = listarCategorias();
        Boolean flag = false;
        for (Categoria categoria : categorias) {
            if(newCategoria.getNombreCategoria().trim().equalsIgnoreCase(categoria.getNombreCategoria()) && !newCategoria.getId().equals(categoria.getId())){
               flag = true;
            }
        }
        return flag;
    }
}
