package com.ford;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseDeDatos {

    public abstract void conectar() throws SQLException;

    public abstract void cerrar() throws SQLException;

    public abstract Connection obtenerConexion();

    public abstract List<String[]> ejecutarConsulta(String consulta) throws SQLException;
}
