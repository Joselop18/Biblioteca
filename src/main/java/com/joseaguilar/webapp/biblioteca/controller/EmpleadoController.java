package com.joseaguilar.webapp.biblioteca.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joseaguilar.webapp.biblioteca.model.Empleado;
import com.joseaguilar.webapp.biblioteca.service.EmpleadoService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RestController
@RequestMapping(value = "")
public class EmpleadoController {
    
    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/empleados")
    public List<Empleado> listarEmpleados() {
        return empleadoService.listarEmpleados();
    }

    @GetMapping("/empleado")
    public ResponseEntity<Empleado> buscarEmpleadoPorId(@RequestParam long id) {
        try {
            return ResponseEntity.ok(empleadoService.buscarEmpleadoPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/empleado")
    public ResponseEntity<Map<String, String>> guardarEmpleado(@RequestBody Empleado empleado) {
        Map<String, String> response = new HashMap<>();
        try {
            if (empleadoService.guardarEmpleado(empleado)) {
                response.put("message", "Empleado agregado con éxito");
            } else {
                response.put("err", "DPI duplicado");
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err", "No se ha podido agregar el Empleado");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/empleado")
    public ResponseEntity<Map<String, String>> editarEmpleado(@RequestParam Long id,
            @RequestBody Empleado newEmpleado) {
        Map<String, String> response = new HashMap<>();
        try {
            Empleado oldEmpleado = empleadoService.buscarEmpleadoPorId(id);
            oldEmpleado.setNombre(newEmpleado.getNombre());
            oldEmpleado.setApellido(newEmpleado.getApellido());
            oldEmpleado.setTelefono(newEmpleado.getTelefono());
            oldEmpleado.setDireccion(newEmpleado.getDireccion());
            oldEmpleado.setDpi(newEmpleado.getDpi());
            if (empleadoService.guardarEmpleado(oldEmpleado)) {
                response.put("message", "El empleado se edito con éxito");
                return ResponseEntity.ok(response);
            } else {
                response.put("err", "No se pudo editar el empleado");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            response.put("err", "No se pudo editar el empleado");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/empleado")
    public ResponseEntity<Map<String, String>> eliminarEmpleado(@RequestParam Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            Empleado empleado = empleadoService.buscarEmpleadoPorId(id);
            empleadoService.eliminarEmpleado(empleado);
            response.put("message", "Empleado eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err", "no se pudo eliminar el empleado");
            return ResponseEntity.badRequest().body(response);
        }

    }
}
