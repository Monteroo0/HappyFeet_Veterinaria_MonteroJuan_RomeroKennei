package com.happyfeet.model;

public class CitaEstado {
    private int id;
    private String nombre;

    public CitaEstado() {}

    public CitaEstado(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "CitaEstado [id=" + id + ", nombre=" + nombre + "]";
    }
}
