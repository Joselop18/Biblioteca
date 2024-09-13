package com.joseaguilar.webapp.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joseaguilar.webapp.biblioteca.model.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo,Long>{

}
