package com.joseaguilar.webapp.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joseaguilar.webapp.biblioteca.model.Libro;

public interface LibroRepository extends JpaRepository<Libro,Long>{

}
