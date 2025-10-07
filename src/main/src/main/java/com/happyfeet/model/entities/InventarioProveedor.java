package com.happyfeet.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa la relación entre inventario y proveedores
 */
public class InventarioProveedor {
    private int id;
    private int inventarioId;
    private int proveedorId;
    private boolean esProveedorPrincipal;
    private BigDecimal precioCompra;
    private Integer tiempoEntregaDias;
    private LocalDateTime fechaRegistro;

    // Información adicional
    private String nombreProducto;
    private String nombreProveedor;

    public InventarioProveedor() {
        this.fechaRegistro = LocalDateTime.now();
        this.esProveedorPrincipal = false;
    }

    public InventarioProveedor(int inventarioId, int proveedorId, boolean esProveedorPrincipal,
                              BigDecimal precioCompra, Integer tiempoEntregaDias) {
        this();
        this.inventarioId = inventarioId;
        this.proveedorId = proveedorId;
        this.esProveedorPrincipal = esProveedorPrincipal;
        this.precioCompra = precioCompra;
        this.tiempoEntregaDias = tiempoEntregaDias;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getInventarioId() { return inventarioId; }
    public void setInventarioId(int inventarioId) { this.inventarioId = inventarioId; }

    public int getProveedorId() { return proveedorId; }
    public void setProveedorId(int proveedorId) { this.proveedorId = proveedorId; }

    public boolean isEsProveedorPrincipal() { return esProveedorPrincipal; }
    public void setEsProveedorPrincipal(boolean esProveedorPrincipal) { this.esProveedorPrincipal = esProveedorPrincipal; }

    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal precioCompra) { this.precioCompra = precioCompra; }

    public Integer getTiempoEntregaDias() { return tiempoEntregaDias; }
    public void setTiempoEntregaDias(Integer tiempoEntregaDias) { this.tiempoEntregaDias = tiempoEntregaDias; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }

    @Override
    public String toString() {
        return String.format(
            "InventarioProveedor [producto=%s, proveedor=%s, principal=%s, precio=%s, entrega=%d días]",
            nombreProducto != null ? nombreProducto : "ID:" + inventarioId,
            nombreProveedor != null ? nombreProveedor : "ID:" + proveedorId,
            esProveedorPrincipal ? "Sí" : "No",
            precioCompra != null ? "$" + precioCompra : "N/A",
            tiempoEntregaDias != null ? tiempoEntregaDias : 0
        );
    }
}