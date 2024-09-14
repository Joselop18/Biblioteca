package com.joseaguilar.webapp.biblioteca.controller.FXController;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joseaguilar.webapp.biblioteca.model.Cliente;
import com.joseaguilar.webapp.biblioteca.model.Empleado;
import com.joseaguilar.webapp.biblioteca.model.Libro;
import com.joseaguilar.webapp.biblioteca.model.Prestamo;
import com.joseaguilar.webapp.biblioteca.service.EmpleadoService;
import com.joseaguilar.webapp.biblioteca.service.ClienteService;
import com.joseaguilar.webapp.biblioteca.service.LibroService;
import com.joseaguilar.webapp.biblioteca.service.PrestamoService;
import com.joseaguilar.webapp.biblioteca.system.Main;
import com.joseaguilar.webapp.biblioteca.util.MethodType;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import lombok.Setter;

@Component
public class PrestamoFXController implements Initializable {

    @FXML
    TextField tfId, tfVigencia, tfBuscar;

    @FXML
    Button btnGuardar, btnVaciar, btnRegresar, btnEliminar, btnBuscar, btnAgregar;

    @FXML
    ComboBox cmbCliente, cmbEmpleado, cmbLibro1, cmbLibro2, cmbLibro3;

    @FXML
    DatePicker dpPrestamo, dpDevolucion;

    @FXML
    TableView tblPrestamos;

    @FXML
    TableColumn colId, colCliente, colEmpleado, colVigencia, colLibros, colFechaPrestamo, colFechaDevolucion;

    @Setter
    private Main stage;

    private List<Libro> librosSeleccionados = new ArrayList<>();
    private int estado = 0;

    @Autowired
    PrestamoService prestamoService;

    @Autowired
    EmpleadoService empleadoService;

    @Autowired
    ClienteService clienteService;

    @Autowired
    LibroService libroService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        llenarCmbs();
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnGuardar) {
            if (tfId.getText().isBlank()) {
                agregarPrestamo();
            } else {
                editarPrestamo();
            }
        } else if (event.getSource() == btnVaciar) {
            vaciarForm();
        } else if (event.getSource() == btnRegresar) {
            stage.indexView();
        } else if (event.getSource() == btnEliminar) {
            eliminarPrestamo();
        } else if (event.getSource() == btnBuscar) {
            tblPrestamos.getItems().clear();
            if (tfBuscar.getText().isBlank()) {
                cargarDatos();
            } else {
                tblPrestamos.getItems().add(buscarPrestamo());
                colId.setCellValueFactory(new PropertyValueFactory<Prestamo, String>("id"));
                colCliente.setCellValueFactory(new PropertyValueFactory<Prestamo, Cliente>("cliente"));
                colEmpleado.setCellValueFactory(new PropertyValueFactory<Prestamo, Empleado>("empleado"));
                colLibros.setCellValueFactory(
                        new Callback<TableColumn.CellDataFeatures<Prestamo, String>, ObservableValue<String>>() {
                            @Override
                            public ObservableValue<String> call(
                                    TableColumn.CellDataFeatures<Prestamo, String> cellData) {
                                Prestamo prestamo = cellData.getValue();
                                return new SimpleStringProperty(prestamo.formatoLibros());
                            }
                        });
                colVigencia.setCellValueFactory(new PropertyValueFactory<Prestamo, Boolean>("vigencia"));
                colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("fechaDePrestamo"));
                colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("fechaDeDevolucion"));
            }
        }else if (event.getSource() == btnAgregar) {
            if (estado == 0) {
                cmbLibro2.setDisable(false);
                estado = 1;
            } else if (estado == 1) {
                cmbLibro3.setDisable(false);
                estado = 2;
            }
        }
    }

    public void cargarDatos() {
        tblPrestamos.setItems(listaPrestamos());
        colId.setCellValueFactory(new PropertyValueFactory<Prestamo, String>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Prestamo, Cliente>("cliente"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<Prestamo, Empleado>("empleado"));
        colLibros.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Prestamo, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Prestamo, String> cellData) {
                        Prestamo prestamo = cellData.getValue();
                        return new SimpleStringProperty(prestamo.formatoLibros());
                    }
                });
        colVigencia.setCellValueFactory(new PropertyValueFactory<Prestamo, Boolean>("vigencia"));
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("fechaDePrestamo"));
        colFechaDevolucion.setCellValueFactory(new PropertyValueFactory<Prestamo, Date>("fechaDeDevolucion"));
    }

    public ObservableList<Prestamo> listaPrestamos() {
        return FXCollections.observableList(prestamoService.listarPrestamos());
    }

    public void agregarPrestamo() {
        agregarLibrosSeleccionados();
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaDeDevolucion(Date.valueOf(dpDevolucion.getValue()));
        prestamo.setFechaDePrestamo(Date.valueOf(dpPrestamo.getValue()));
        prestamo.setCliente((Cliente) cmbCliente.getSelectionModel().getSelectedItem());
        prestamo.setEmpleado((Empleado) cmbEmpleado.getSelectionModel().getSelectedItem());
        prestamo.setVigencia(Boolean.valueOf(tfVigencia.getText()));
        prestamo.setLibros(librosSeleccionados);
        cmbLibro2.setDisable(true);
        cmbLibro3.setDisable(true);
        prestamoService.guardarPrestamo(prestamo,MethodType.POST);
        cargarDatos();
    }

    public void editarPrestamo() {
        Prestamo prestamo = prestamoService.buscarPrestamoPorId(Long.parseLong(tfId.getText()));
        agregarLibrosSeleccionados();
        prestamo.setFechaDeDevolucion(Date.valueOf(dpDevolucion.getValue()));
        prestamo.setFechaDePrestamo(Date.valueOf(dpPrestamo.getValue()));
        prestamo.setCliente((Cliente) cmbCliente.getSelectionModel().getSelectedItem());
        prestamo.setEmpleado((Empleado) cmbEmpleado.getSelectionModel().getSelectedItem());
        prestamo.setVigencia(Boolean.valueOf(tfVigencia.getText()));
        prestamo.setLibros(librosSeleccionados);
        prestamoService.guardarPrestamo(prestamo,MethodType.PUT);
        cargarDatos();
    }

    public void llenarCmbs() {
        cmbCliente.setItems(FXCollections.observableList(clienteService.listarClientes()));
        cmbEmpleado.setItems(FXCollections.observableList(empleadoService.listarEmpleados()));
        cmbLibro1.setItems(FXCollections.observableList(libroService.listarLibros()));
        cmbLibro2.setItems(FXCollections.observableList(libroService.listarLibros()));
        cmbLibro3.setItems(FXCollections.observableList(libroService.listarLibros()));
    }

    public void cargarForm() {
        Prestamo prestamo = (Prestamo) tblPrestamos.getSelectionModel().getSelectedItem();
        if (prestamo != null) {
            tfId.setText(prestamo.getId().toString());
            tfVigencia.setText(prestamo.getVigencia().toString());
            dpDevolucion.setValue(prestamo.getFechaDeDevolucion().toLocalDate());
            dpPrestamo.setValue(prestamo.getFechaDePrestamo().toLocalDate());
            cmbCliente.getSelectionModel().select(obtenerIndexCliente());
            cmbEmpleado.getSelectionModel().select(obtenerIndexEmpleado());
            int libros = prestamo.getLibros().size();
            if (libros == 1) {
                cmbLibro1.getSelectionModel().select(obtenerIndexLibro1());
                cmbLibro2.setDisable(true);
                cmbLibro3.setDisable(true);
            } else if (libros == 2) {
                cmbLibro2.setDisable(false);
                cmbLibro2.getSelectionModel().select(obtenerIndexLibro2());
                cmbLibro3.setDisable(true);
            } else {
                cmbLibro3.setDisable(false);
                cmbLibro3.getSelectionModel().select(obtenerIndexLibro3());
            }
        }
    }

    public void vaciarForm() {
        tfId.clear();
        tfVigencia.clear();
        dpDevolucion.setValue(null);
        dpPrestamo.setValue(null);
        cmbCliente.getSelectionModel().clearSelection();
        cmbEmpleado.getSelectionModel().clearSelection();
        cmbLibro1.getSelectionModel().clearSelection();
        cmbLibro2.getSelectionModel().clearSelection();
        cmbLibro3.getSelectionModel().clearSelection();
        cmbLibro2.setDisable(true);
        cmbLibro3.setDisable(true);
    }

    public void eliminarPrestamo() {
        Prestamo prestamo = prestamoService.buscarPrestamoPorId(Long.parseLong(tfId.getText()));
        prestamoService.eliminarPrestamo(prestamo);
        cargarDatos();
    }

    public Prestamo buscarPrestamo() {
        return prestamoService.buscarPrestamoPorId(Long.parseLong(tfBuscar.getText()));
    }

    public void agregarLibrosSeleccionados() {
        librosSeleccionados.clear();
        if (cmbLibro1.getValue() != null) {
            librosSeleccionados.add((Libro) cmbLibro1.getValue());
            System.out.println(librosSeleccionados);
        }
        if (cmbLibro2.getValue() != null) {
            librosSeleccionados.add((Libro) cmbLibro2.getValue());
            System.out.println(librosSeleccionados);
        }
        if (cmbLibro3.getValue() != null) {
            librosSeleccionados.add((Libro) cmbLibro3.getValue());
            System.out.println(librosSeleccionados);
        }
    }

    public int obtenerIndexCliente() {
        int index = 0;
        for (int i = 0; i < cmbCliente.getItems().size(); i++) {
            String clienteCmb = cmbCliente.getItems().get(i).toString();
            String clienteTbl = ((Prestamo) tblPrestamos.getSelectionModel().getSelectedItem()).getCliente().toString();
            if (clienteCmb.equals(clienteTbl)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int obtenerIndexEmpleado() {
        int index = 0;
        for (int i = 0; i < cmbEmpleado.getItems().size(); i++) {
            String EmpleadoCmb = cmbEmpleado.getItems().get(i).toString();
            String EmpleadoTbl = ((Prestamo) tblPrestamos.getSelectionModel().getSelectedItem()).getEmpleado()
                    .toString();
            if (EmpleadoCmb.equals(EmpleadoTbl)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int obtenerIndexLibro1() {
        Prestamo prestamo = (Prestamo) tblPrestamos.getSelectionModel().getSelectedItem();
        if (prestamo == null) {
            return -1;
        }

        Libro libroSeleccionado = prestamo.getLibros().get(0);
        for (int i = 0; i < cmbLibro1.getItems().size(); i++) {
            String libroCmb = cmbLibro1.getItems().get(i).toString();
            String libroTbl = libroSeleccionado.toString();
            if (libroCmb.equals(libroTbl)) {
                return i;
            }
        }
        return -1;
    }

    public int obtenerIndexLibro2() {
        Prestamo prestamo = (Prestamo) tblPrestamos.getSelectionModel().getSelectedItem();
        if (prestamo == null || prestamo.getLibros().size() < 2) {
            return -1;
        }

        Libro libroSeleccionado = prestamo.getLibros().get(1);
        for (int i = 0; i < cmbLibro2.getItems().size(); i++) {
            String libroCmb = cmbLibro2.getItems().get(i).toString();
            String libroTbl = libroSeleccionado.toString();
            if (libroCmb.equals(libroTbl)) {
                return i;
            }
        }
        return -1;
    }

    public int obtenerIndexLibro3() {
        Prestamo prestamo = (Prestamo) tblPrestamos.getSelectionModel().getSelectedItem();
        if (prestamo == null || prestamo.getLibros().size() < 3) {
            return -1;
        }

        Libro libroSeleccionado = prestamo.getLibros().get(2);
        for (int i = 0; i < cmbLibro3.getItems().size(); i++) {
            String libroCmb = cmbLibro3.getItems().get(i).toString();
            String libroTbl = libroSeleccionado.toString();
            if (libroCmb.equals(libroTbl)) {
                return i;
            }
        }
        return -1;
    }

}