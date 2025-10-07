package com.happyfeet.model;

import java.time.LocalDate;

public class MascotaAdopcion {
    private int id;
    private String nombre;
    private Integer razaId;
    private String edadEstimada;
    private String sexo;
    private String descripcion;
    private String estado;
    private LocalDate fechaIngreso;
    private String urlFoto;

    // Campo adicional para mostrar el nombre de la raza
    private String nombreRaza;

    public MascotaAdopcion() {}

    public MascotaAdopcion(int id, String nombre, Integer razaId, String edadEstimada,
                           String sexo, String descripcion, String estado,
                           LocalDate fechaIngreso, String urlFoto) {
        this.id = id;
        this.nombre = nombre;
        this.razaId = razaId;
        this.edadEstimada = edadEstimada;
        this.sexo = sexo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.urlFoto = urlFoto;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getRazaId() { return razaId; }
    public void setRazaId(Integer razaId) { this.razaId = razaId; }

    public String getEdadEstimada() { return edadEstimada; }
    public void setEdadEstimada(String edadEstimada) { this.edadEstimada = edadEstimada; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getUrlFoto() { return urlFoto; }
    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }

    public String getNombreRaza() { return nombreRaza; }
    public void setNombreRaza(String nombreRaza) { this.nombreRaza = nombreRaza; }

    public boolean estaDisponible() {
        return "Disponible".equals(estado);
    }

    @Override
    public String toString() {
        return "MascotaAdopción [id=" + id +
                ", nombre=" + nombre +
                ", raza=" + nombreRaza +
                ", edad=" + edadEstimada +
                ", sexo=" + sexo +
                ", estado=" + estado +
                ", ingreso=" + fechaIngreso + "]";
    }
}