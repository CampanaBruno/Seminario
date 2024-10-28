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
        String usuario = usuarioField.getText().trim();
        String contrasenia = contraseniaField.getText().trim();

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            mostrarAlerta("Error", "Por favor, ingrese usuario y contraseña.");
            return;
        }

        try {
            if (autenticar(usuario, contrasenia)) {
                App.setRoot("PantallaDeInicio", 500, 700);
            } else {
                mostrarAlerta("Error", "Verifique usuario y/o contraseña.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error de base de datos", "Ocurrió un error al autenticar: " + e.getMessage());
        }
    }

    private boolean autenticar(String nombre, String clave) throws SQLException {
        String consulta = "SELECT * FROM usuarios WHERE nombre = ? AND clave = ? AND rol = 'cliente'";
        dbUtil.conectar(); 

        try (var stmt = dbUtil.obtenerConexion().prepareStatement(consulta)) { 
            stmt.setString(1, nombre);
            stmt.setString(2, clave);
            var rs = stmt.executeQuery();
            return rs.next();
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
