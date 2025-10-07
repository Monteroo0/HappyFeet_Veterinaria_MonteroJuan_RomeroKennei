package com.happyfeet.model;

import java.time.LocalDateTime;

public class Cita {
    private int id;
    private int mascotaId;
    private LocalDateTime fechaHora;
    private String motivo;
    private int estadoId;

    // Campos adicionales para mostrar información relacionada
    private String nombreMascota;
    private String nombreDueno;
    private String estadoNombre;

    public Cita() {}

    public Cita(int id, int mascotaId, LocalDateTime fechaHora, String motivo, int estadoId) {
        this.id = id;
        this.mascotaId = mascotaId;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estadoId = estadoId;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMascotaId() { return mascotaId; }
    public void setMascotaId(int mascotaId) { this.mascotaId = mascotaId; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public int getEstadoId() { return estadoId; }
    public void setEstadoId(int estadoId) { this.estadoId = estadoId; }

    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }

    public String getNombreDueno() { return nombreDueno; }
    public void setNombreDueno(String nombreDueno) { this.nombreDueno = nombreDueno; }

    public String getEstadoNombre() { return estadoNombre; }
    public void setEstadoNombre(String estadoNombre) { this.estadoNombre = estadoNombre; }

    @Override
    public String toString() {
        return "Cita [id=" + id +
                ", mascota=" + nombreMascota +
                ", dueño=" + nombreDueno +
                ", fecha=" + fechaHora +
                ", motivo=" + motivo +
                ", estado=" + estadoNombre + "]";
    }
}