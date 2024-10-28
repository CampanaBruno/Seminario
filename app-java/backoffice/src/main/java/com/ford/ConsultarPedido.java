package com.ford;

import com.ford.models.DetallePedido;
import com.ford.models.Pedido;
import com.ford.models.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsultarPedido {

    private final String url = "jdbc:mysql://127.0.0.1:5432/ford_motor_company";
    private final String user = "root";
    private final String password = "admin$34";

    @FXML
    private Button aceptarButton;

    @FXML
    private TextField pedidoField;

    @FXML
    private Button volverButton;

    @FXML
    private void aceptar() {
        String pedidoStr = pedidoField.getText().trim();
        int numeroPedido;

        if (pedidoStr.isEmpty()) {
            mostrarAlerta("Error", "Por favor, ingrese un número de pedido.", Alert.AlertType.ERROR);
            return;
        }

        try {
            numeroPedido = Integer.parseInt(pedidoStr);
            Pedido pedido = buscarPedido(numeroPedido);

            if (pedido == null) {
                mostrarAlerta("No se encontraron pedidos", "No se encontró el pedido con el número: " + numeroPedido, Alert.AlertType.ERROR);
            } else {
                StringBuilder mensaje = new StringBuilder("Pedido número: " + pedido.getId() + "\n" +
                        "Fecha: " + pedido.getFecha() + "\n" +
                        "Total de vehículos: " + pedido.getTotal() + "\n" +
                        "Detalles:\n");

                for (DetallePedido detalle : pedido.getDetalles()) {
                    mensaje.append("- Producto: ").append(detalle.getProducto().getNombre()).append("\n")
                            .append("  - Cantidad: ").append(detalle.getCantidad()).append("\n");
                }
                mostrarAlertaYRegresarAMenu("Pedido encontrado", mensaje.toString(), Alert.AlertType.INFORMATION);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El número de pedido debe ser un número válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error inesperado", "Ocurrió un error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Pedido buscarPedido(int numeroPedido) {
        Pedido pedido = null;
        List<DetallePedido> detalles = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT p.id, p.fecha, p.usuario_id FROM pedidos p WHERE p.id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, numeroPedido);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                Date fecha = rs.getDate("fecha");
                String cliente = obtenerClientePorId(rs.getInt("usuario_id"));

                String detalleSql = "SELECT dp.cantidad, dp.producto_id FROM detalle_pedido dp WHERE dp.pedido_id = ?";
                PreparedStatement detalleStmt = conn.prepareStatement(detalleSql);
                detalleStmt.setInt(1, id);
                ResultSet detalleRs = detalleStmt.executeQuery();

                int totalVehiculos = 0;
                while (detalleRs.next()) {
                    int cantidad = detalleRs.getInt("cantidad");
                    int productoId = detalleRs.getInt("producto_id");
                    Producto producto = obtenerProductoPorId(productoId);
                    DetallePedido detalle = new DetallePedido(cantidad, producto, pedido);
                    detalles.add(detalle);
                    totalVehiculos += cantidad;
                }

                pedido = new Pedido(id, fecha, totalVehiculos, cliente, detalles);
            }
        } catch (SQLException e) {
            mostrarAlerta("Error de base de datos", "Error al buscar el pedido: " + e.getMessage(), Alert.AlertType.ERROR);
        }

        return pedido;
    }

    private String obtenerClientePorId(int usuarioId) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT nombre FROM usuarios WHERE id = ?")) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nombre");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error de base de datos", "Error al obtener el cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return "Cliente no encontrado";
    }

    private Producto obtenerProductoPorId(int productoId) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM productos WHERE id = ?")) {
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("modelo"),
                        rs.getString("descripcion"),
                        rs.getInt("anio"),
                        rs.getInt("cantidad")
                );
            }
        } catch (SQLException e) {
            mostrarAlerta("Error de base de datos", "Error al obtener el producto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return null;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaYRegresarAMenu(String titulo, String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();

        try {
            App.setRoot("PantallaDeInicio", 500, 700);
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al regresar a la pantalla de inicio: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void pantallaInicio() {
        try {
            App.setRoot("PantallaDeInicio", 500, 700);
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al regresar a la pantalla de inicio: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
