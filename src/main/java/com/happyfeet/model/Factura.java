package com.happyfeet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private int id;
    private int duenoId;
    private LocalDateTime fechaEmision;
    private BigDecimal total;

    // Campos adicionales para mostrar información relacionada
    private String nombreDueno;
    private String documentoDueno;
    private List<ItemFactura> items;

    public Factura() {
        this.items = new ArrayList<>();
    }

    public Factura(int id, int duenoId, LocalDateTime fechaEmision, BigDecimal total) {
        this.id = id;
        this.duenoId = duenoId;
        this.fechaEmision = fechaEmision;
        this.total = total;
        this.items = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDuenoId() { return duenoId; }
    public void setDuenoId(int duenoId) { this.duenoId = duenoId; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getNombreDueno() { return nombreDueno; }
    public void setNombreDueno(String nombreDueno) { this.nombreDueno = nombreDueno; }

    public String getDocumentoDueno() { return documentoDueno; }
    public void setDocumentoDueno(String documentoDueno) { this.documentoDueno = documentoDueno; }

    public List<ItemFactura> getItems() { return items; }
    public void setItems(List<ItemFactura> items) { this.items = items; }

    public void agregarItem(ItemFactura item) {
        this.items.add(item);
    }

    public BigDecimal calcularTotal() {
        return items.stream()
                .map(ItemFactura::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Factura [id=" + id +
                ", cliente=" + nombreDueno +
                ", fecha=" + fechaEmision +
                ", total=$" + total + "]";
    }
}