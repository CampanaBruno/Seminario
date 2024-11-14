package com.ford;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UtilidadesBD {
    private String url = "jdbc:mysql://127.0.0.1:5432/ford_motor_company"; 
    private String user = "root";
    private String password = "pass123";
    private Connection connection;

    public void conectar() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Conectado a la base de datos.");
    }

    public List<String[]> ejecutarConsulta(String consulta) throws SQLException {
        List<String[]> resultados = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(consulta)) {

            int cantidadColumnas = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                String[] fila = new String[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    fila[i] = resultSet.getString(i + 1);
                }
                resultados.add(fila);
            }
        }
        return resultados;
    }

    public Connection obtenerConexion() {
        return connection;
    }

    public void cerrar() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Conexión cerrada.");
        }
    }
}
