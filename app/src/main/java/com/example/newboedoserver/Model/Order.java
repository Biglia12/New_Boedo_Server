package com.example.newboedoserver.Model;

public class Order {
        private String ProductoId;
        private String ProductoNombre;
        private String Cantidad;
        private String Precio;
        private String Descuento;

        public Order() {
        }

        public Order(String productoId, String productoNombre, String cantidad, String precio, String descuento) {
            ProductoId = productoId;
            ProductoNombre = productoNombre;
            Cantidad = cantidad;
            Precio = precio;
            Descuento = descuento;
        }

        public String getProductId() {
            return ProductoId;
        }

        public void setProductoId(String productId) {
            ProductoId = productId;
        }

        public String getProductoNombre() {
            return ProductoNombre;
        }

        public void setProductoNombre(String productName) {
            ProductoNombre = productName;
        }

        public String getCantidad() {
            return Cantidad;
        }

        public void setCantidad(String quantity) {
            Cantidad = quantity;
        }

        public String getPrecio() {
            return Precio;
        }

        public void setPrecio(String price) {
            Precio = price;
        }

        public String getDescuento() {
            return Descuento;
        }

        public void setDescuento(String discount) {
            Descuento = discount;
        }
    }


