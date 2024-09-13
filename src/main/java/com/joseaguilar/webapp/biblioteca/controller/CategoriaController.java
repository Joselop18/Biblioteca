package com.joseaguilar.webapp.biblioteca.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joseaguilar.webapp.biblioteca.model.Categoria;
import com.joseaguilar.webapp.biblioteca.service.CategoriaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RestController
@RequestMapping(value = "")

public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/categorias")
    public List<Categoria> listaCategorias() {
        return categoriaService.listarCategorias();
    }

    @GetMapping("/categoria")
    public ResponseEntity<Categoria> buscarCategoriaPorId(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(categoriaService.buscarCategoriaPorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/categoria")
    public ResponseEntity<Map<String, String>> agregarCategoria(@RequestBody Categoria categoria) {
        Map<String, String> response = new HashMap<>();
        try {
            if (categoriaService.guardarCategoria(categoria)) {
                response.put("message", "Categoria agregada con extio");
                return ResponseEntity.ok(response);
            }else{
                response.put("err", "Error al agregar la categoria");
            return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            response.put("err", "Error al agregar la categoria");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/categoria")
    public ResponseEntity<Map<String, String>> editarCategoria(@RequestParam Long id,
            @RequestBody Categoria newCategoria) {
        Map<String, String> response = new HashMap<>();
        try {
            Categoria oldCategoria = categoriaService.buscarCategoriaPorId(id);
            oldCategoria.setNombreCategoria(newCategoria.getNombreCategoria());
            categoriaService.guardarCategoria(oldCategoria);
            response.put("message", "La categoria se edito con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err", "La categoria no se pudo editar");
            return ResponseEntity.badRequest().body(response);
        }

    }

    @DeleteMapping("/categoria")
    public ResponseEntity<Map<String, String>> eliminarCategoria(@RequestParam Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            Categoria categoria = categoriaService.buscarCategoriaPorId(id);
            categoriaService.eliminarCategoria(categoria);
            response.put("message", "eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err", "No se pudo eliminar la categoria");
            return ResponseEntity.badRequest().body(response);
        }

    }
    
}
