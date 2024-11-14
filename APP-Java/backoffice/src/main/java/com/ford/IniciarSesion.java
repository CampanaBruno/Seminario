package com.ford;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.SQLException;

public class IniciarSesion {

    @FXML
    private Button boton;

    @FXML
    private PasswordField contraseniaField;

    @FXML
    private TextField usuarioField; 

    private UtilidadesBD dbUtil = new UtilidadesBD();

    @FXML
    private void iniciarSesion() throws IOException {
        String correo = usuarioField.getText().trim(); 
        String contrasenia = contraseniaField.getText().trim();

        if (correo.isEmpty() || contrasenia.isEmpty()) {
            mostrarAlerta("Error", "Por favor, ingrese usuario y contraseña.");
            return;
        }

        try {
            if (autenticar(correo, contrasenia)) {
                App.setRoot("PantallaDeInicio", 500, 700);
            } else {
            }
        } catch (SQLException e) {
            mostrarAlerta("Error de base de datos", "Ocurrió un error al autenticar: " + e.getMessage());
        }
    }

    private boolean autenticar(String correo, String clave) throws SQLException {
        String consulta = "SELECT * FROM usuarios WHERE correo = ?";
        dbUtil.conectar(); 

        try (var stmt = dbUtil.obtenerConexion().prepareStatement(consulta)) { 
            stmt.setString(1, correo); 
            var rs = stmt.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                if (!"cliente".equals(rol)) {
                    mostrarAlerta("Acceso denegado", "El usuario ingresado debe ser un cliente.");
                    return false; 
                }

                String claveGuardada = rs.getString("clave");
                if (!clave.equals(claveGuardada)) {
                    mostrarAlerta("Error", "Contraseña incorrecta.");
                    return false;
                }
                return true;
            } else {
                mostrarAlerta("Acceso denegado", "El usuario ingresado debe ser un cliente.");
                return false;
            }
        } finally {
            dbUtil.cerrar();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
