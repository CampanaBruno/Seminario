package com.ford.models;

import java.util.Date;
import java.util.List;


public class Pedido {
    private int id;
    private Date fecha;
    private int total; 
    private String cliente;
    private List<DetallePedido> detalles;


    public Pedido(int id, Date fecha, int total, String cliente, List<DetallePedido> detalles) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.cliente = cliente;
        this.detalles = detalles;
    }


    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public int getTotal() {
        return total; 
    }

    public String getCliente() {
        return cliente;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
}