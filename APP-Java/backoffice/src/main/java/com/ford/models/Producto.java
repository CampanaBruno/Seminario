package com.ford.models;


public class Producto {
    private int id;
    private String nombre;
    private String modelo;
    private String descripcion;
    private int anio;
    private int cantidad;


    public Producto(int id, String nombre, String modelo, String descripcion, int anio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.anio = anio;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getAnio() {
        return anio;
    }

    public int getCantidad() {
        return cantidad;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}

