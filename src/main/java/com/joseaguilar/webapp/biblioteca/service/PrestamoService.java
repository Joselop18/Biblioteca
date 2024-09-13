package com.joseaguilar.webapp.biblioteca.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joseaguilar.webapp.biblioteca.model.Libro;
import com.joseaguilar.webapp.biblioteca.model.Prestamo;
import com.joseaguilar.webapp.biblioteca.repository.PrestamoRepository;
import com.joseaguilar.webapp.biblioteca.util.MethodType;

@Service
public class PrestamoService implements IPrestamoService{

    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    LibroService libroService;

    @Override
    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }

    @Override
    public Prestamo buscarPrestamoPorId(Long id) {
        return prestamoRepository.findById(id).orElse(null);
    }

    @Override
    public Integer guardarPrestamo(Prestamo prestamo, MethodType methodType) {
        if (methodType == MethodType.POST) {
            if (!verificarCliente(prestamo)) {
                if (verificarLibro(prestamo, null)) {
                    if (verificarCantidad(prestamo)) {
                        for (Libro libro : prestamo.getLibros()) {
                            Libro libroCompleto = libroService.buscarLibroPorId(libro.getId());
                            libroService.actualizarDisponibilidad(libroCompleto, false);
                        }
                        prestamoRepository.save(prestamo);
                        return 1;
                    } else {
                        return 4;
                    }
                } else {
                    return 3;
                }

            } else {
                return 2;
            }
        } else if (methodType == MethodType.PUT) {
            if (verificarCantidad(prestamo)) {
                prestamoRepository.save(prestamo);
                return 1;
            } else {
                return 3;
            }
        }else{
            return 0;
        }

    }

    @Override
    public void librosRegresados(Prestamo prestamo, Prestamo newPrestamo){
        List<Libro> librosRegresados = new ArrayList<>();
        for (Libro libro : prestamo.getLibros()) {
            Libro   libroCompleto = libroService.buscarLibroPorId(libro.getId());
            if (!newPrestamo.getLibros().contains(libroCompleto)) {
                librosRegresados.add(libroCompleto);
            }
        }
        cambiarDisponibilidadLibro(librosRegresados, true);
        cambiarDisponibilidadLibro(newPrestamo.getLibros(), false);
    }

    @Override
    public void cambiarDisponibilidadLibro(List<Libro> libros, Boolean disponibilidad) {
        for (Libro libro : libros) {
            Libro libroCompleto = libroService.buscarLibroPorId(libro.getId());
            libroService.actualizarDisponibilidad(libroCompleto, disponibilidad);
        }
    }

    @Override
    public void eliminarPrestamo(Prestamo prestamo) {
        for (Libro libro : prestamo.getLibros()) {
            Libro libroCompleto = libroService.buscarLibroPorId(libro.getId());
            libroService.actualizarDisponibilidad(libroCompleto, true);
        }
        prestamoRepository.delete(prestamo);
    }

    @Override
    public Boolean verificarCliente(Prestamo newPrestamo) {
        List<Prestamo> prestamos = listarPrestamos();
        Boolean flag = false;
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getCliente().getDpi().equals(newPrestamo.getCliente().getDpi())) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public Boolean verificarLibro(Prestamo newPrestamo, Prestamo prestamo) {
        Boolean flag = true;
        List<Libro> libros = new ArrayList<>();
        if (prestamo != null) {
            for (Libro libro : newPrestamo.getLibros()) {
                Libro libroCompleto = libroService.buscarLibroPorId(libro.getId());
                if (!prestamo.getLibros().contains(libro)) {
                    if (!libroCompleto.getDisponibilidad()) {
                        flag = false;
                        break;
                    }
                }
            }

        } else {
            for (Libro libro : newPrestamo.getLibros()) {
                Libro libroCompleto = libroService.buscarLibroPorId(libro.getId());
                libros.add(libroCompleto);
            }

            for (Libro libro : libros) {
                if (!libro.getDisponibilidad()) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }

    @Override
    public Boolean verificarCantidad(Prestamo newPrestamo) {
        List<Libro> libros = new ArrayList<>();

        for (Libro libro : newPrestamo.getLibros()) {
            Libro libroCompleto = libroService.buscarLibroPorId(libro.getId());
            libros.add(libroCompleto);
        }
        if (libros.size() <= 3) {
            return true;
        } else {
            return false;
        }
    }
    
}
