package com.joseaguilar.webapp.biblioteca.controller.FXController;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joseaguilar.webapp.biblioteca.model.Categoria;
import com.joseaguilar.webapp.biblioteca.model.Libro;
import com.joseaguilar.webapp.biblioteca.service.CategoriaService;
import com.joseaguilar.webapp.biblioteca.service.LibroService;
import com.joseaguilar.webapp.biblioteca.system.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Setter;

@Component
public class LibroFXController implements Initializable{
    
    @FXML
    TextField tfId, tfNombre, tfIsbn, tfAutor, tfEditorial, tfDisponibilidad, tfNumEs, tfCluster, tfBuscar;

    @FXML
    TextArea taSinopsis;
    @FXML
    Button btnGuardar, btnVaciar, btnRegresar, btnEliminar, btnBuscar;

    @FXML
    ComboBox cmbCategoria;

    @FXML
    TableView tblLibros;

    @FXML
    TableColumn colId, colIsbn, colSinopsis, colAutor, colEditorial, colDisponibilidad, colNumEs, colCluster, colNombre, colCategoria;

    @Setter
    private Main stage;

    @Autowired
    LibroService libroService;

    @Autowired
    CategoriaService categoriaService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbCategoria.setItems(FXCollections.observableList(categoriaService.listarCategorias()));
        cargarDatos();
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnGuardar) {
            if (tfId.getText().isBlank()) {
                agregarLibro();
            } else {
                editarLibro();
            }
        } else if (event.getSource() == btnVaciar) {
            vaciarForm();
        } else if (event.getSource() == btnRegresar) {
            stage.indexView();
        } else if (event.getSource() == btnEliminar) {
            eliminarLibro();
        } else if (event.getSource() == btnBuscar) {
            tblLibros.getItems().clear();
            if (tfBuscar.getText().isBlank()) {
                cargarDatos();
            } else {
                tblLibros.getItems().add(buscarLibro());
                colId.setCellValueFactory(new PropertyValueFactory<Libro, Long>("id"));
                colNombre.setCellValueFactory(new PropertyValueFactory<Libro, String>("nombre"));
                colCategoria.setCellValueFactory(new PropertyValueFactory<Libro, String>("categoria"));
                colIsbn.setCellValueFactory(new PropertyValueFactory<Libro, String>("isbn"));
                colSinopsis.setCellValueFactory(new PropertyValueFactory<Libro, String>("sinopsis"));
                colAutor.setCellValueFactory(new PropertyValueFactory<Libro, String>("autor"));
                colEditorial.setCellValueFactory(new PropertyValueFactory<Libro, String>("editorial"));
                colNumEs.setCellValueFactory(new PropertyValueFactory<Libro, String>("numeroEstanteria"));
                colCluster.setCellValueFactory(new PropertyValueFactory<Libro, String>("cluster"));
                colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Libro, Boolean>("disponibilidad"));
            }

        }
    }

    public void cargarDatos() {
        tblLibros.setItems(listarLibros());
        colId.setCellValueFactory(new PropertyValueFactory<Libro, Long>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Libro, String>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Libro, String>("categoria"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<Libro, String>("isbn"));
        colSinopsis.setCellValueFactory(new PropertyValueFactory<Libro, String>("sinopsis"));
        colAutor.setCellValueFactory(new PropertyValueFactory<Libro, String>("autor"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<Libro, String>("editorial"));
        colNumEs.setCellValueFactory(new PropertyValueFactory<Libro, String>("numeroEstanteria"));
        colCluster.setCellValueFactory(new PropertyValueFactory<Libro, String>("cluster"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Libro, Boolean>("disponibilidad"));
    }

    public ObservableList<Libro> listarLibros() {
        return FXCollections.observableList(libroService.listarLibros());
    }

    public void cargarForm() {
        Libro libro = (Libro) tblLibros.getSelectionModel().getSelectedItem();
        if (libro != null) {
            tfId.setText(libro.getId().toString());
            tfNombre.setText(libro.getNombre());
            tfAutor.setText(libro.getAutor());
            taSinopsis.setText(libro.getSinopsis());
            tfCluster.setText(libro.getCluster());
            tfDisponibilidad.setText(libro.getDisponibilidad().toString());
            tfEditorial.setText(libro.getEditorial());
            tfNumEs.setText(libro.getNumeroEstanteria());
            tfIsbn.setText(libro.getIsbn());
            cmbCategoria.getSelectionModel().select(obtenerIndexCategoria());
        }
    }

    public void vaciarForm() {
        tfId.clear();
        tfNombre.clear();
        tfAutor.clear();
        tfDisponibilidad.clear();
        tfCluster.clear();
        tfEditorial.clear();
        tfIsbn.clear();
        tfNumEs.clear();
        taSinopsis.clear();
        cmbCategoria.getSelectionModel().clearSelection();

    }

    public void agregarLibro() {
        Libro libro = new Libro();
        libro.setNombre(tfNombre.getText());
        libro.setAutor(tfAutor.getText());
        libro.setSinopsis(taSinopsis.getText());
        libro.setIsbn(tfIsbn.getText());
        libro.setEditorial(tfEditorial.getText());
        libro.setNumeroEstanteria(tfNumEs.getText());
        libro.setCluster(tfCluster.getText());
        libro.setCategoria((Categoria) cmbCategoria.getSelectionModel().getSelectedItem());
        libro.setDisponibilidad(true);
        libroService.guardarLibro(libro);
        cargarDatos();
    }

    public void editarLibro() {
        Libro libro = libroService.buscarLibroPorId(Long.parseLong(tfId.getText()));
        libro.setNombre(tfNombre.getText());
        libro.setAutor(tfAutor.getText());
        libro.setSinopsis(taSinopsis.getText());
        libro.setIsbn(tfIsbn.getText());
        libro.setEditorial(tfEditorial.getText());
        libro.setNumeroEstanteria(tfNumEs.getText());
        libro.setCluster(tfCluster.getText());
        libro.setCategoria((Categoria) cmbCategoria.getSelectionModel().getSelectedItem());
        libroService.guardarLibro(libro);
        cargarDatos();
    }

    public void eliminarLibro() {
        Libro libro = libroService.buscarLibroPorId(Long.parseLong(tfId.getText()));
        libroService.eliminarLibro(libro);
        cargarDatos();
    }

    public Libro buscarLibro() {
        return libroService.buscarLibroPorId(Long.parseLong(tfBuscar.getText()));
    }

    public int obtenerIndexCategoria() {
        int index = 0;
        for (int i = 0; i < cmbCategoria.getItems().size(); i++) {
            String categoriaCmb = cmbCategoria.getItems().get(i).toString();
            String categoriaTbl = ((Libro) tblLibros.getSelectionModel().getSelectedItem()).getCategoria().toString();
            System.out.println(categoriaCmb);
            System.out.println(categoriaTbl);
            if (categoriaCmb.equals(categoriaTbl)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
