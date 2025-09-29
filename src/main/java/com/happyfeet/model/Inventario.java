package com.happyfeet.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Inventario {
    private int id;
    private String nombreProducto;
    private int productoTipoId;
    private String descripcion;
    private String fabricante;
    private String lote;
    private int cantidadStock;
    private int stockMinimo;
    private LocalDate fechaVencimiento;
    private BigDecimal precioVenta;

    // Campo adicional para mostrar el tipo de producto
    private String tipoProducto;

    public Inventario() {}

    public Inventario(int id, String nombreProducto, int productoTipoId, String descripcion,
                      String fabricante, String lote, int cantidadStock, int stockMinimo,
                      LocalDate fechaVencimiento, BigDecimal precioVenta) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.productoTipoId = productoTipoId;
        this.descripcion = descripcion;
        this.fabricante = fabricante;
        this.lote = lote;
        this.cantidadStock = cantidadStock;
        this.stockMinimo = stockMinimo;
        this.fechaVencimiento = fechaVencimiento;
        this.precioVenta = precioVenta;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public int getProductoTipoId() { return productoTipoId; }
    public void setProductoTipoId(int productoTipoId) { this.productoTipoId = productoTipoId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }

    public int getCantidadStock() { return cantidadStock; }
    public void setCantidadStock(int cantidadStock) { this.cantidadStock = cantidadStock; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public String getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(String tipoProducto) { this.tipoProducto = tipoProducto; }

    // Métodos de utilidad
    public boolean estaVencido() {
        return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now());
    }

    public boolean necesitaReabastecimiento() {
        return cantidadStock <= stockMinimo;
    }

    public boolean venceProximamente(int diasAlerta) {
        if (fechaVencimiento == null) return false;
        return fechaVencimiento.isBefore(LocalDate.now().plusDays(diasAlerta));
    }

    @Override
    public String toString() {
        String estado = "";
        if (estaVencido()) {
            estado = " [VENCIDO]";
        } else if (necesitaReabastecimiento()) {
            estado = " [STOCK BAJO]";
        } else if (venceProximamente(30)) {
            estado = " [VENCE PRONTO]";
        }

        return "Producto [id=" + id +
                ", nombre=" + nombreProducto +
                ", tipo=" + tipoProducto +
                ", stock=" + cantidadStock +
                ", precio=$" + precioVenta +
                ", vencimiento=" + fechaVencimiento + estado + "]";
    }
}