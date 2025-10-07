package com.happyfeet.model;

import java.time.LocalDate;

public class HistorialMedico {
    private int id;
    private int mascotaId;
    private LocalDate fechaEvento;
    private int eventoTipoId;
    private String descripcion;
    private String diagnostico;
    private String tratamientoRecomendado;

    // Campos adicionales para mostrar información relacionada
    private String nombreMascota;
    private String nombreDueno;
    private String tipoEvento;

    public HistorialMedico() {}

    public HistorialMedico(int id, int mascotaId, LocalDate fechaEvento, int eventoTipoId,
                           String descripcion, String diagnostico, String tratamientoRecomendado) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.fechaEvento = fechaEvento;
        this.eventoTipoId = eventoTipoId;
        this.descripcion = descripcion;
        this.diagnostico = diagnostico;
        this.tratamientoRecomendado = tratamientoRecomendado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMascotaId() { return mascotaId; }
    public void setMascotaId(int mascotaId) { this.mascotaId = mascotaId; }

    public LocalDate getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(LocalDate fechaEvento) { this.fechaEvento = fechaEvento; }

    public int getEventoTipoId() { return eventoTipoId; }
    public void setEventoTipoId(int eventoTipoId) { this.eventoTipoId = eventoTipoId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamientoRecomendado() { return tratamientoRecomendado; }
    public void setTratamientoRecomendado(String tratamientoRecomendado) { this.tratamientoRecomendado = tratamientoRecomendado; }

    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }

    public String getNombreDueno() { return nombreDueno; }
    public void setNombreDueno(String nombreDueno) { this.nombreDueno = nombreDueno; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    @Override
    public String toString() {
        return "Historial [id=" + id +
                ", mascota=" + nombreMascota +
                ", dueño=" + nombreDueno +
                ", fecha=" + fechaEvento +
                ", tipo=" + tipoEvento +
                ", diagnóstico=" + diagnostico + "]";
    }
}