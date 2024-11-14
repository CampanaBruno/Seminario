package com.ford.models;


public class DetallePedido {   
    private int cantidad;
    private Producto producto;
    private Pedido pedido;



    public DetallePedido(int cantidad, Producto producto, Pedido pedido) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.pedido = pedido;
    }


    public int getCantidad() {
        return cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}


