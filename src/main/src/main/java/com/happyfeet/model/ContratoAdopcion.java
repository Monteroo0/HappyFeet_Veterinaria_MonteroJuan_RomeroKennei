package com.happyfeet.model;

import java.time.LocalDate;

public class ContratoAdopcion {
    private int id;
    private int mascotaAdopcionId;
    private String adoptanteNombre;
    private String adoptanteDocumento;
    private String adoptanteTelefono;
    private String adoptanteDireccion;
    private LocalDate fechaAdopcion;
    private String contratoTexto;

    // Campo adicional para mostrar información de la mascota
    private String nombreMascota;

    public ContratoAdopcion() {}

    public ContratoAdopcion(int id, int mascotaAdopcionId, String adoptanteNombre,
                            String adoptanteDocumento, String adoptanteTelefono,
                            String adoptanteDireccion, LocalDate fechaAdopcion, String contratoTexto) {
        this.id = id;
        this.mascotaAdopcionId = mascotaAdopcionId;
        this.adoptanteNombre = adoptanteNombre;
        this.adoptanteDocumento = adoptanteDocumento;
        this.adoptanteTelefono = adoptanteTelefono;
        this.adoptanteDireccion = adoptanteDireccion;
        this.fechaAdopcion = fechaAdopcion;
        this.contratoTexto = contratoTexto;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMascotaAdopcionId() { return mascotaAdopcionId; }
    public void setMascotaAdopcionId(int mascotaAdopcionId) { this.mascotaAdopcionId = mascotaAdopcionId; }

    public String getAdoptanteNombre() { return adoptanteNombre; }
    public void setAdoptanteNombre(String adoptanteNombre) { this.adoptanteNombre = adoptanteNombre; }

    public String getAdoptanteDocumento() { return adoptanteDocumento; }
    public void setAdoptanteDocumento(String adoptanteDocumento) { this.adoptanteDocumento = adoptanteDocumento; }

    public String getAdoptanteTelefono() { return adoptanteTelefono; }
    public void setAdoptanteTelefono(String adoptanteTelefono) { this.adoptanteTelefono = adoptanteTelefono; }

    public String getAdoptanteDireccion() { return adoptanteDireccion; }
    public void setAdoptanteDireccion(String adoptanteDireccion) { this.adoptanteDireccion = adoptanteDireccion; }

    public LocalDate getFechaAdopcion() { return fechaAdopcion; }
    public void setFechaAdopcion(LocalDate fechaAdopcion) { this.fechaAdopcion = fechaAdopcion; }

    public String getContratoTexto() { return contratoTexto; }
    public void setContratoTexto(String contratoTexto) { this.contratoTexto = contratoTexto; }

    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }

    @Override
    public String toString() {
        return "ContratoAdopción [id=" + id +
                ", mascota=" + nombreMascota +
                ", adoptante=" + adoptanteNombre +
                ", documento=" + adoptanteDocumento +
                ", fecha=" + fechaAdopcion + "]";
    }
}