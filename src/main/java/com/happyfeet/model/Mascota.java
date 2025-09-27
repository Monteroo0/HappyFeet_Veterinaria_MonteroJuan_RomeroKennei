package com.happyfeet.model;

public class Mascota {
    private int id;
    private int duenoId;
    private String nombre;
    private String raza;
    private String fechaNacimiento; // opcional, puede ser null
    private String sexo;
    private String urlFoto;

    public Mascota(int id, int duenoId, String nombre, String raza, String fechaNacimiento, String sexo, String urlFoto) {
        this.id = id;
        this.duenoId = duenoId;
        this.nombre = nombre;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.urlFoto = urlFoto;
    }

    public Mascota() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDuenoId() { return duenoId; }
    public void setDuenoId(int duenoId) { this.duenoId = duenoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getUrlFoto() { return urlFoto; }
    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }

    @Override
    public String toString() {
        return "Mascota [id=" + id +
                ", dueñoId=" + duenoId +
                ", nombre=" + nombre +
                ", raza=" + raza +
                ", sexo=" + sexo +
                ", fechaNacimiento=" + fechaNacimiento +
                ", urlFoto=" + urlFoto + "]";
    }
}
