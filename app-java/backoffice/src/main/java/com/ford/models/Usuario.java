package com.ford.models;


public class Usuario { 
    private int id;
    private String nombre;
    private String correo;
    private String rol;
    private String clave;


    public Usuario(int id, String nombre, String correo, String rol, String clave) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.clave = clave;
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }

    public String getClave() {
        return clave;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
