package com.example.newboedoserver.Model;

public class Food {

    private String Nombre, Imagen , Descripcion, Precio, Descuento, MenuId;

    public Food() {
    }

    public Food (String nombre, String imagen, String descripcion, String precio, String descuento, String menuId) {
        this.Nombre = nombre;
        this.Imagen= imagen;
        this.Descripcion = descripcion;
        this.Precio = precio;
        this.Descuento = descuento;
        this.MenuId = menuId;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        this.Imagen = imagen;
    }

    public String getDescripcion() { return Descripcion;
    }

    public void setDescripcion(String description) {
        this.Descripcion = description;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        this.Precio = precio;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        this.Descuento = descuento;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        this.MenuId = menuId;
    }
}
