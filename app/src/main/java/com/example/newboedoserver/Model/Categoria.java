package com.example.newboedoserver.Model;

public class Categoria {
    private String Nombre;
    private String Imagen;

    public Categoria() {
    }

    public Categoria(String nombre, String imagen) {
        Nombre = nombre;
        Imagen = imagen;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }
}
