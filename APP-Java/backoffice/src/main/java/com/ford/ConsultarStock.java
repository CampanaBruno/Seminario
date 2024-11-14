package com.ford;

import com.ford.models.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConsultarStock {

    @FXML
    private Button aceptarButton;

    @FXML
    private TextField anioField;

    @FXML
    private TextField modeloField;

    @FXML
    private TextField nombreField;

    @FXML
    private Button volverButton;

    private UtilidadesBD dbUtil = new UtilidadesBD();

    @FXML
    private void pantallaInicio() throws IOException {
        App.setRoot("PantallaDeInicio", 500, 700);
    }

    @FXML
    private void mostrarStock() throws IOException {
        String nombre = nombreField.getText().trim();
        String modelo = modeloField.getText().trim();
        String anioStr = anioField.getText().trim();

        if (nombre.isEmpty() || modelo.isEmpty() || anioStr.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos deben estar completos.", Alert.AlertType.ERROR);
            return;
        }

        int anio;
        try {
            anio = Integer.parseInt(anioStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El año debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        List<Producto> productosEncontrados = new ArrayList<>();
        try {
            dbUtil.conectar();
            try (PreparedStatement stmt = dbUtil.obtenerConexion().prepareStatement("SELECT * FROM productos WHERE nombre = ? AND modelo = ? AND anio = ?")) {
                stmt.setString(1, nombre);
                stmt.setString(2, modelo);
                stmt.setInt(3, anio);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Producto producto = new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("modelo"),
                            rs.getString("descripcion"),
                            rs.getInt("anio"),
                            rs.getInt("cantidad")
                    );
                    productosEncontrados.add(producto);
                }
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al conectar a la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        } finally {
            try {
                dbUtil.cerrar();
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al cerrar la conexión: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }

        if (!productosEncontrados.isEmpty()) {
            for (Producto producto : productosEncontrados) {
                if (producto.getCantidad() <= 0) {
                    mostrarAlerta("Stock no disponible", "Producto: " + producto.getNombre() + "\nNo hay stock.", Alert.AlertType.WARNING);
                    return;
                }
            }
            mostrarProductosEnTabla(productosEncontrados);
        } else {
            mostrarAlerta("Error", "No se encontró el producto.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarProductosEnTabla(List<Producto> productos) {
        StringBuilder contenido = new StringBuilder("Productos Disponibles:\n\n");
        for (Producto producto : productos) {
            contenido.append("Nombre: ").append(producto.getNombre()).append("\n")
                    .append("Modelo: ").append(producto.getModelo()).append("\n")
                    .append("Descripción: ").append(producto.getDescripcion()).append("\n")
                    .append("Año: ").append(producto.getAnio()).append("\n")
                    .append("Cantidad: ").append(producto.getCantidad()).append("\n");
        }

        mostrarAlerta("Productos Disponibles", contenido.toString(), Alert.AlertType.INFORMATION);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
