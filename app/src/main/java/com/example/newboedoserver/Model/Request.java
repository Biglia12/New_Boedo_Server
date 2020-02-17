package com.example.newboedoserver.Model;

import java.util.List;

public class Request {
    private String telefono;
    private String nombre;
    private String direccion;
    private String total;
    private String estados;
    private List<Order>comidas; // lista de comida ordenada

    public Request() {
    }

    public Request(String telefono, String nombre, String direccion, String total, List<Order> comidas) {
        this.telefono = telefono;
        this.nombre = nombre;
        this.direccion = direccion;
        this.total = total;
        this.comidas = comidas;
        this.estados="0";//Default es 0, 0: lugar,1: envio,2: enviado
    }

    public String getEstados() {
        return estados;
    }

    public void setEstados(String estados) {
        this.estados = estados;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getComidas() {
        return comidas;
    }

    public void setComidas(List<Order> comidas) {
        this.comidas = comidas;
    }
}
