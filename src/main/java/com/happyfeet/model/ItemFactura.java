package com.happyfeet.model;

import java.math.BigDecimal;

public class ItemFactura {
    private int id;
    private int facturaId;
    private Integer productoId; // Puede ser null si es un servicio
    private String servicioDescripcion; // Puede ser null si es un producto
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    // Campo adicional para mostrar el nombre del producto
    private String nombreProducto;

    public ItemFactura() {}

    public ItemFactura(int id, int facturaId, Integer productoId, String servicioDescripcion,
                       int cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
        this.id = id;
        this.facturaId = facturaId;
        this.productoId = productoId;
        this.servicioDescripcion = servicioDescripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFacturaId() { return facturaId; }
    public void setFacturaId(int facturaId) { this.facturaId = facturaId; }

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }

    public String getServicioDescripcion() { return servicioDescripcion; }
    public void setServicioDescripcion(String servicioDescripcion) { this.servicioDescripcion = servicioDescripcion; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getDescripcionCompleta() {
        if (productoId != null && nombreProducto != null) {
            return nombreProducto;
        } else if (servicioDescripcion != null) {
            return servicioDescripcion;
        }
        return "Item sin descripción";
    }

    public boolean esProducto() {
        return productoId != null;
    }

    public boolean esServicio() {
        return servicioDescripcion != null && !servicioDescripcion.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Item [" + getDescripcionCompleta() +
                ", cantidad=" + cantidad +
                ", precio=$" + precioUnitario +
                ", subtotal=$" + subtotal + "]";
    }
}