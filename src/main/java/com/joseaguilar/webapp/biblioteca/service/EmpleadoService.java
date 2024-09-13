package com.joseaguilar.webapp.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joseaguilar.webapp.biblioteca.model.Empleado;
import com.joseaguilar.webapp.biblioteca.repository.EmpleadoRepository;

@Service
public class EmpleadoService implements IEmpleadoService{

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public List<Empleado> listarEmpleados(){
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado buscarEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean guardarEmpleado(Empleado empleado) {
        if(!verificarpDpiDuplicado(empleado)){
            empleadoRepository.save(empleado);
            return true;
        }
        return false;
    }

    @Override
    public void eliminarEmpleado(Empleado empleado) {
        empleadoRepository.delete(empleado);
    }

    @Override
    public Boolean verificarpDpiDuplicado(Empleado newEmpleado) {
        List<Empleado> empleados = listarEmpleados();
        Boolean flag = false;
        for (Empleado empleado : empleados) {
            if(empleado.getDpi().equals(newEmpleado.getDpi()) && !empleado.getId().equals(newEmpleado.getId())){
                flag = true;
            }
        }
        return flag;
    }
    
}
