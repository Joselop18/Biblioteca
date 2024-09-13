package com.joseaguilar.webapp.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joseaguilar.webapp.biblioteca.model.Libro;
import com.joseaguilar.webapp.biblioteca.repository.LibroRepository;

@Service
public class LibroService implements ILibroService{

    @Autowired
    LibroRepository libroRepository;

    @Override
    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    @Override
    public Libro guardarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public Libro buscarLibroPorId(Long id) {
        return libroRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarLibro(Libro libro) {
        libroRepository.delete(libro);
    }

    @Override
    public void actualizarDisponibilidad(Libro libro, Boolean disponibilidad) {
        libro.setDisponibilidad(disponibilidad);
        guardarLibro(libro);
    }
    
}
