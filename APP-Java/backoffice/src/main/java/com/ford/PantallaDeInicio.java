package com.ford;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

public class PantallaDeInicio {

    @FXML
    private Button consultarPedidoButton;

    @FXML
    private Button consultarStockButton;

    @FXML
    private Button registrarNuevoPedidoButton;

    @FXML
    private Button salirButton;

    @FXML
    private void registrarPedido() throws IOException {
        App.setRoot("RegistrarPedido", 500, 750);
    }

    @FXML
    private void consultarStock() throws IOException {
        App.setRoot("ConsultarStock", 500, 700);
    }

    @FXML
    private void consultarPedido() throws IOException {
        App.setRoot("ConsultarPedido", 500, 700);
    }

    @FXML
    private void salir() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Salir");
        alert.setHeaderText("¿Qué deseas hacer?");
        alert.setContentText("Selecciona una opción:");

        ButtonType botonCerrarSesion = new ButtonType("Cerrar Sesión");
        ButtonType botonSalir = new ButtonType("Salir");
        ButtonType botonCancelar = new ButtonType("Cancelar");

        alert.getButtonTypes().setAll(botonCerrarSesion, botonSalir, botonCancelar);

        Optional<ButtonType> resultado = alert.showAndWait();
        
        if (resultado.isPresent()) {
            if (resultado.get() == botonCerrarSesion) {
                try {
                    App.setRoot("IniciarSesion", 500, 450);
                } catch (IOException e) {
                    mostrarAlerta("Error", "Error al regresar al login: " + e.getMessage());
                }
            } else if (resultado.get() == botonSalir) {
                System.exit(0); 
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
