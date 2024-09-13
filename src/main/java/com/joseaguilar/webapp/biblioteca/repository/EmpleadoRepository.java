package com.joseaguilar.webapp.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joseaguilar.webapp.biblioteca.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado,Long>{

}
