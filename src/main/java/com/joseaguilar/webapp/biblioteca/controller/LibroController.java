package com.joseaguilar.webapp.biblioteca.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joseaguilar.webapp.biblioteca.model.Libro;
import com.joseaguilar.webapp.biblioteca.service.LibroService;


@Controller
@RestController
@RequestMapping(value = "")

public class LibroController {
    
    @Autowired
    LibroService libroService;

    @GetMapping("/libros")
    public List<Libro> listarLibros(){
        return libroService.listarLibros();
    }

    @GetMapping("/libro")
    public ResponseEntity<Libro> buscarLibroPorId(@RequestParam Long id){
        try {
            return ResponseEntity.ok(libroService.buscarLibroPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/libro")
    public ResponseEntity<Map<String,String>> guardarLibro(@RequestBody Libro libro){
        Map<String,String> response =  new HashMap<>();
        try {
            libroService.guardarLibro(libro);
            response.put("message", "Libro agregado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err", "No se ha podido agregar el Libro");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/libro")
    public ResponseEntity<Map<String,String>> editarLibro(@RequestParam Long id, @RequestBody Libro newLibro) {
        Map<String,String> response = new HashMap<>();
        try {
            Libro oldLibro = libroService.buscarLibroPorId(id);
            oldLibro.setAutor(newLibro.getAutor());
            oldLibro.setCategoria(newLibro.getCategoria());
            oldLibro.setCluster(newLibro.getCluster());
            oldLibro.setDisponibilidad(newLibro.getDisponibilidad());
            oldLibro.setEditorial(newLibro.getEditorial());
            oldLibro.setNombre(newLibro.getNombre());
            oldLibro.setIsbn(newLibro.getIsbn());
            oldLibro.setNumeroEstanteria(newLibro.getNumeroEstanteria());
            oldLibro.setSinopsis(newLibro.getSinopsis());
            libroService.guardarLibro(oldLibro);
            response.put("message", "libro editado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err", "no se pudo editar el Libro");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/libro")
    public ResponseEntity<Map<String,String>> eliminarLibro(@RequestParam Long id){
        Map<String,String> response = new HashMap<>();
        try {
           Libro libro = libroService.buscarLibroPorId(id);
           libroService.eliminarLibro(libro);
           response.put("message", "libro eliminado con éxito");
           return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err", "no se pudo eliminar el Libro");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
