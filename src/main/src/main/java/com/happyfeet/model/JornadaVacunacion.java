package com.happyfeet.model;

import java.time.LocalDate;

public class JornadaVacunacion {
    private int id;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String descripcion;
    private boolean activa;

    public JornadaVacunacion() {}

    public JornadaVacunacion(int id, String nombre, LocalDate fechaInicio, LocalDate fechaFin,
                             String descripcion, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.activa = activa;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public boolean estaEnCurso() {
        LocalDate hoy = LocalDate.now();
        return activa && !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
    }

    @Override
    public String toString() {
        String estado = activa ? (estaEnCurso() ? "[EN CURSO]" : "[ACTIVA]") : "[INACTIVA]";
        return "JornadaVacunación [id=" + id +
                ", nombre=" + nombre +
                ", inicio=" + fechaInicio +
                ", fin=" + fechaFin +
                ", estado=" + estado + "]";
    }
}