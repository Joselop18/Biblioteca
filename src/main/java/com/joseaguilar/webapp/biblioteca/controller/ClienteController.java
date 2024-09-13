package com.joseaguilar.webapp.biblioteca.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joseaguilar.webapp.biblioteca.model.Cliente;
import com.joseaguilar.webapp.biblioteca.service.ClienteService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RestController
@RequestMapping(value = "")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/clientes")
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/cliente")
    public ResponseEntity<Cliente> buscarClientePorId(@RequestParam Long id){
        try {
            return ResponseEntity.ok(clienteService.buscarClientePorId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/cliente")
    public ResponseEntity<Map<String,String>> agregarCliente(@RequestBody Cliente cliente){
        Map<String,String> response =  new HashMap<>();
        try {
            clienteService.guardarCliente(cliente);
            response.put("message", "Cliente agregado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err", "No se ha podido agregar el cliente");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/cliente")
    public ResponseEntity<Map<String, String>> editarCliente(@RequestParam Long id,
            @RequestBody Cliente newCliente) {
        Map<String, String> response = new HashMap<>();
        try {
            Cliente oldCliente = clienteService.buscarClientePorId(id);
            oldCliente.setNombre(newCliente.getNombre());
            oldCliente.setApellido(newCliente.getApellido());
            oldCliente.setTelefono(newCliente.getTelefono());
            clienteService.guardarCliente(oldCliente);
            response.put("message","El cliente se edito con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err","El cliente no se pudo editar");
            return ResponseEntity.badRequest().body(response);
        }

    }

    @DeleteMapping("/cliente")
    public ResponseEntity<Map<String,String>> eliminarCliente(@RequestParam Long id){
        Map<String,String> response = new HashMap<>();
        try {
            Cliente cliente = clienteService.buscarClientePorId(id);
            clienteService.eliminarCliente(cliente);
            response.put("message", "Cliente Eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("err", "No se pudo eliminar el cliente");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
