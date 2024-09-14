package com.joseaguilar.webapp.biblioteca.system;

import java.io.IOException;
import java.io.InputStream;
 
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
 
import com.joseaguilar.webapp.biblioteca.BibliotecaApplication;
import com.joseaguilar.webapp.biblioteca.controller.FXController.CategoriaFXController;
import com.joseaguilar.webapp.biblioteca.controller.FXController.ClienteFXController;
import com.joseaguilar.webapp.biblioteca.controller.FXController.EmpleadoFXController;
import com.joseaguilar.webapp.biblioteca.controller.FXController.IndexController;
import com.joseaguilar.webapp.biblioteca.controller.FXController.LibroFXController;
import com.joseaguilar.webapp.biblioteca.controller.FXController.PrestamoFXController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
 
public class Main extends Application {
 
    private ConfigurableApplicationContext applicationContext;
    private Stage stage;
    private Scene scene;
 
    @Override
    public void init(){
        this.applicationContext = new SpringApplicationBuilder(BibliotecaApplication.class).run();
    }
 
    @Override
    public void start(Stage primaryStage) throws Exception {
       this.stage = primaryStage;
       stage.setTitle("Biblioteca Pro");
       indexView();
       stage.show();
    }
 
    public Initializable switchScene(String fxmlName, int width, int height) throws IOException{
        Initializable resultado = null;
        FXMLLoader loader = new FXMLLoader();
 
        loader.setControllerFactory(applicationContext:: getBean);
        InputStream archivo  =  Main.class.getResourceAsStream("/templates/" + fxmlName);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource("/templates/" + fxmlName));
 
        scene = new Scene((AnchorPane) loader.load(archivo),width,height);
        stage.setScene(scene);
        stage.sizeToScene();
 
        resultado = (Initializable)loader.getController();
        return resultado;
    }
 
    public void indexView(){
        try {
            IndexController indexView = (IndexController)switchScene("index.fxml", 650, 500);
            indexView.setStage(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CategoriaView(){
        try {
            CategoriaFXController categoriaView = (CategoriaFXController)switchScene("CategoriaView.fxml", 1200, 800);
            categoriaView.setStage(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ClienteView(){
        try {
            ClienteFXController clienteView = (ClienteFXController)switchScene("ClienteView.fxml", 1200, 800);
            clienteView.setStage(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LibroView(){
        try {
            LibroFXController libroView = (LibroFXController)switchScene("LibroView.fxml", 1200,800);
            libroView.setStage(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EmpleadoView(){
        try {
            EmpleadoFXController empleadoView = (EmpleadoFXController)switchScene("EmpleadoView.fxml", 1200,800);
            empleadoView.setStage(this );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PrestamoView(){
        try {
            PrestamoFXController prestamoView = (PrestamoFXController)switchScene("PrestamoView.fxml", 1200,800);
            prestamoView.setStage(this );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}