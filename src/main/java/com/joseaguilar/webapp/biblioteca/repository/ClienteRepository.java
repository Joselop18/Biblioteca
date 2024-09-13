package com.joseaguilar.webapp.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joseaguilar.webapp.biblioteca.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente,Long>{

}
