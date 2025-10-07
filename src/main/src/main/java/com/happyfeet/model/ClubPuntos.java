package com.happyfeet.model;

import java.time.LocalDate;

public class ClubPuntos {
    private int id;
    private int duenoId;
    private int puntosAcumulados;
    private LocalDate fechaUltimaActividad;

    // Campo adicional para mostrar información del dueño
    private String nombreDueno;

    public ClubPuntos() {}

    public ClubPuntos(int id, int duenoId, int puntosAcumulados, LocalDate fechaUltimaActividad) {
        this.id = id;
        this.duenoId = duenoId;
        this.puntosAcumulados = puntosAcumulados;
        this.fechaUltimaActividad = fechaUltimaActividad;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDuenoId() { return duenoId; }
    public void setDuenoId(int duenoId) { this.duenoId = duenoId; }

    public int getPuntosAcumulados() { return puntosAcumulados; }
    public void setPuntosAcumulados(int puntosAcumulados) { this.puntosAcumulados = puntosAcumulados; }

    public LocalDate getFechaUltimaActividad() { return fechaUltimaActividad; }
    public void setFechaUltimaActividad(LocalDate fechaUltimaActividad) { this.fechaUltimaActividad = fechaUltimaActividad; }

    public String getNombreDueno() { return nombreDueno; }
    public void setNombreDueno(String nombreDueno) { this.nombreDueno = nombreDueno; }

    public String getNivelMembresía() {
        if (puntosAcumulados >= 1000) return "PLATINO";
        if (puntosAcumulados >= 500) return "ORO";
        if (puntosAcumulados >= 100) return "PLATA";
        return "BRONCE";
    }

    public boolean puedeCanjenar(int puntosRequeridos) {
        return puntosAcumulados >= puntosRequeridos;
    }

    @Override
    public String toString() {
        return "ClubPuntos [id=" + id +
                ", cliente=" + nombreDueno +
                ", puntos=" + puntosAcumulados +
                ", nivel=" + getNivelMembresía() +
                ", última actividad=" + fechaUltimaActividad + "]";
    }
}