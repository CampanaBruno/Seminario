package com.ford;

import com.ford.models.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RegistrarPedido {

    @FXML
    private Button aceptarButton;

    @FXML
    private TextField anioField;

    @FXML
    private TextField cantidadField;

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
    private void registrarPedido() {
        String nombre = nombreField.getText().trim();
        String modelo = modeloField.getText().trim();
        String anioStr = anioField.getText().trim();
        String cantidadStr = cantidadField.getText().trim();

        if (nombre.isEmpty() || modelo.isEmpty() || anioStr.isEmpty() || cantidadStr.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos deben estar completos.");
            return;
        }

        int anio;
        try {
            anio = Integer.parseInt(anioStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El año debe ser un número válido.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La cantidad debe ser un número válido.");
            return;
        }

        Producto producto = buscarProducto(nombre, modelo, anio);
        if (producto != null) {
            if (producto.getCantidad() >= cantidad) {
                int nuevaCantidad = producto.getCantidad() - cantidad;
                producto.setCantidad(nuevaCantidad);

                actualizarCantidadProductoEnBD(producto.getId(), nuevaCantidad);
                
                int pedidoId = registrarPedidoEnBD(1); 
                if (pedidoId != -1) {
                    registrarDetallePedidoEnBD(pedidoId, producto.getId(), cantidad);
                    mostrarAlertaDeExito("Pedido registrado con éxito.", pedidoId);
                } else {
                    mostrarAlerta("Error", "No se pudo registrar el pedido en la base de datos.");
                }
            } else {
                mostrarAlerta("Error", "Stock insuficiente. Solo hay " + producto.getCantidad() + " disponible.");
            }
        } else {
            mostrarAlerta("Error", "No se encontró el producto.");
        }
    }

    private Producto buscarProducto(String nombre, String modelo, int anio) {
        String consulta = "SELECT * FROM productos WHERE nombre = ? AND modelo = ? AND anio = ?";

        try {
            dbUtil.conectar(); 
            try (PreparedStatement pstmt = dbUtil.obtenerConexion().prepareStatement(consulta)) { 
                pstmt.setString(1, nombre);
                pstmt.setString(2, modelo);
                pstmt.setInt(3, anio);
                ResultSet rs = pstmt.executeQuery();

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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dbUtil.cerrar(); 
            } catch (SQLException e) {
                mostrarAlerta("Error", "Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return null;
    }

    private int registrarPedidoEnBD(int usuarioId) {
        String consulta = "INSERT INTO pedidos (usuario_id, fecha) VALUES (?, CURDATE())";
        try {
            dbUtil.conectar(); 
            try (PreparedStatement pstmt = dbUtil.obtenerConexion().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS)) { 
                pstmt.setInt(1, usuarioId);
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dbUtil.cerrar(); 
            } catch (SQLException e) {
                mostrarAlerta("Error", "Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return -1;
    }

    private void registrarDetallePedidoEnBD(int pedidoId, int productoId, int cantidad) {
        String consulta = "INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad) VALUES (?, ?, ?)";

        try {
            dbUtil.conectar();
            try (PreparedStatement pstmt = dbUtil.obtenerConexion().prepareStatement(consulta)) { 
                pstmt.setInt(1, pedidoId);
                pstmt.setInt(2, productoId);
                pstmt.setInt(3, cantidad);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo registrar el detalle del pedido.");
        } finally {
            try {
                dbUtil.cerrar();
            } catch (SQLException e) {
                mostrarAlerta("Error", "Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    private void actualizarCantidadProductoEnBD(int productoId, int nuevaCantidad) {
        String consulta = "UPDATE productos SET cantidad = ? WHERE id = ?";
        
        try {
            dbUtil.conectar();
            try (PreparedStatement pstmt = dbUtil.obtenerConexion().prepareStatement(consulta)) {
                pstmt.setInt(1, nuevaCantidad);
                pstmt.setInt(2, productoId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo actualizar la cantidad del producto en la base de datos.");
        } finally {
            try {
                dbUtil.cerrar();
            } catch (SQLException e) {
                mostrarAlerta("Error", "Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    private void mostrarAlertaDeExito(String mensaje, Integer numeroPedido) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Pedido registrado");
        alerta.setHeaderText(null);
        if (numeroPedido != null) {
            alerta.setContentText(mensaje + " (Número de pedido: " + numeroPedido + ")");
        } else {
            alerta.setContentText(mensaje);
        }

        alerta.getButtonTypes().setAll(ButtonType.OK);
        Optional<ButtonType> resultado = alerta.showAndWait();
        
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                pantallaInicio();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
