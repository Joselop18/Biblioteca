package com.joseaguilar.webapp.biblioteca.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joseaguilar.webapp.biblioteca.model.Prestamo;
import com.joseaguilar.webapp.biblioteca.service.PrestamoService;
import com.joseaguilar.webapp.biblioteca.util.MethodType;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RestController
@RequestMapping(value = "")
public class PrestamoController {
    
    @Autowired
    PrestamoService prestamoService;

    @GetMapping("/prestamos")
    public List<Prestamo> listarPrestamos() {
        return prestamoService.listarPrestamos();
    }

    @GetMapping("/prestamo")
    public ResponseEntity<Prestamo> buscarPrestamoPorId(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(prestamoService.buscarPrestamoPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/prestamo")
    public ResponseEntity<Map<String, String>> guardarPrestamo(@RequestBody Prestamo prestamo) {
        Map<String, String> response = new HashMap<>();
        try {
            int mensaje = prestamoService.guardarPrestamo(prestamo, MethodType.POST);
            switch (mensaje) {
                case 1:
                    response.put("message", "Préstamo agregado con éxito");
                    return ResponseEntity.ok(response);
                case 2:
                    response.put("err", "Este usuario ya tiene un préstamo activo");
                    return ResponseEntity.badRequest().body(response);
                case 3:
                    response.put("err", "Libro no disponible");
                    return ResponseEntity.badRequest().body(response);
                case 4:
                    response.put("err", "No se pueden prestar más de 3 libros");
                    return ResponseEntity.badRequest().body(response);
                default:
                    response.put("err", "No se pudo agregar el préstamo");
                    return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("err", "Error al procesar el préstamo: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/prestamo")
    public ResponseEntity<Map<String, String>> editarPrestamo(@RequestParam Long id,
            @RequestBody Prestamo newPrestamo) {
        Map<String, String> response = new HashMap<>();
        try {

            Prestamo prestamo = prestamoService.buscarPrestamoPorId(id);
            prestamo.setFechaDePrestamo(newPrestamo.getFechaDePrestamo());
            prestamo.setFechaDeDevolucion(newPrestamo.getFechaDeDevolucion());
            prestamo.setVigencia(newPrestamo.getVigencia());
            prestamo.setEmpleado(newPrestamo.getEmpleado());
            prestamo.setCliente(newPrestamo.getCliente());
            prestamoService.librosRegresados(prestamo, newPrestamo);
            prestamo.setLibros(newPrestamo.getLibros());
            int mensaje = prestamoService.guardarPrestamo(prestamo, MethodType.PUT);
            if (mensaje == 1) {
                response.put("message", "Prestamo editado con exito");
                return ResponseEntity.ok(response);
            } else if (mensaje == 2) {
                response.put("err", "Libro no disponible");
                return ResponseEntity.badRequest().body(response);
            } else if (mensaje == 3) {
                response.put("err", "No se pueden prestar más de 3 libros");
                return ResponseEntity.badRequest().body(response);
            }  else {
                response.put("err", "No se pudo editar el prestamo");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            response.put("err", "No se pudo editar el prestamo");
            return ResponseEntity.badRequest().body(response);
        }

    }

    @DeleteMapping("/prestamo")
    public ResponseEntity<Map<String, String>> eliminarPrestamo(@RequestParam Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            Prestamo prestamo = prestamoService.buscarPrestamoPorId(id);
            prestamoService.eliminarPrestamo(prestamo);
            response.put("message", "Prestamo eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err", "No se pudo eliminar el prestamo");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
