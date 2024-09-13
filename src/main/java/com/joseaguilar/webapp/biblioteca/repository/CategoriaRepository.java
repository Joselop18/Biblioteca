package com.joseaguilar.webapp.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joseaguilar.webapp.biblioteca.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria,Long>{

}
