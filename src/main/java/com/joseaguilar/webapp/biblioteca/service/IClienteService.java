package com.joseaguilar.webapp.biblioteca.service;

import java.util.List;

import com.joseaguilar.webapp.biblioteca.model.Cliente;

public interface IClienteService {
    public List<Cliente> listarClientes();

    public Cliente buscarClientePorId(Long id);

    public Cliente guardarCliente(Cliente cliente);

    public void eliminarCliente(Cliente cliente);
}
