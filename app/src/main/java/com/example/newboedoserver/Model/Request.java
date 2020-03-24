package com.example.newboedoserver.Model;

import java.util.List;

public class Request {
    private String telefono;
    private String nombre;
    private String apellido;
    private String direccion;
    private String pisoydepartamento;
    private String localidad;
    private String total;
    private String entrecalles;
    private String estados;
    private String comment;
    private List<Order>comidas; // lista de comida ordenada

    public Request() {
    }

    public Request(String telefono, String nombre,String apellido, String direccion,String entrecalles,String pisoydepartamento,String localidad, String total, String estados, String comment, List<Order> comidas) {
        this.telefono = telefono;
        this.nombre = nombre;
        this.apellido=apellido;
        this.direccion = direccion;
        this.pisoydepartamento = pisoydepartamento;
        this.localidad = localidad;
        this.entrecalles=entrecalles;
        this.total = total;
        this.estados = estados;
        this.comment = comment;
        this.comidas = comidas;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPisoydepartamento() {
        return pisoydepartamento;
    }

    public void setPisoydepartamento(String pisoydepartamento) {
        this.pisoydepartamento = pisoydepartamento;
    }
    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getEntrecalles() {
        return entrecalles;
    }

    public void setEntrecalles(String entrecalles) {
        this.entrecalles = entrecalles;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getEstados() {
        return estados;
    }

    public void setEstados(String estados) {
        this.estados = estados;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Order> getComidas() {
        return comidas;
    }

    public void setComidas(List<Order> comidas) {
        this.comidas = comidas;
    }
}
