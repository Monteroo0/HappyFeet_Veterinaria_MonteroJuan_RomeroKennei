package com.happyfeet.model;

import java.time.LocalDateTime;

public class RegistroJornada {
    private int id;
    private int jornadaId;
    private String mascotaNombre;
    private String duenoNombre;
    private String duenoTelefono;
    private String vacunaAplicada;
    private LocalDateTime fechaAplicacion;

    // Campo adicional para mostrar información de la jornada
    private String nombreJornada;

    public RegistroJornada() {}

    public RegistroJornada(int id, int jornadaId, String mascotaNombre, String duenoNombre,
                           String duenoTelefono, String vacunaAplicada, LocalDateTime fechaAplicacion) {
        this.id = id;
        this.jornadaId = jornadaId;
        this.mascotaNombre = mascotaNombre;
        this.duenoNombre = duenoNombre;
        this.duenoTelefono = duenoTelefono;
        this.vacunaAplicada = vacunaAplicada;
        this.fechaAplicacion = fechaAplicacion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJornadaId() { return jornadaId; }
    public void setJornadaId(int jornadaId) { this.jornadaId = jornadaId; }

    public String getMascotaNombre() { return mascotaNombre; }
    public void setMascotaNombre(String mascotaNombre) { this.mascotaNombre = mascotaNombre; }

    public String getDuenoNombre() { return duenoNombre; }
    public void setDuenoNombre(String duenoNombre) { this.duenoNombre = duenoNombre; }

    public String getDuenoTelefono() { return duenoTelefono; }
    public void setDuenoTelefono(String duenoTelefono) { this.duenoTelefono = duenoTelefono; }

    public String getVacunaAplicada() { return vacunaAplicada; }
    public void setVacunaAplicada(String vacunaAplicada) { this.vacunaAplicada = vacunaAplicada; }

    public LocalDateTime getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(LocalDateTime fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }

    public String getNombreJornada() { return nombreJornada; }
    public void setNombreJornada(String nombreJornada) { this.nombreJornada = nombreJornada; }

    @Override
    public String toString() {
        return "RegistroJornada [id=" + id +
                ", jornada=" + nombreJornada +
                ", mascota=" + mascotaNombre +
                ", dueño=" + duenoNombre +
                ", vacuna=" + vacunaAplicada +
                ", fecha=" + fechaAplicacion + "]";
    }
}