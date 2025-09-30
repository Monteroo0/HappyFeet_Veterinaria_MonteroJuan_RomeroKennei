package com.happyfeet.model.entities;

import java.time.LocalDateTime;

/**
 * Entidad que representa una transferencia de propiedad de mascota entre dueños
 */
public class TransferenciaPropiedad {
    private int id;
    private int mascotaId;
    private int duenoAnteriorId;
    private int duenoNuevoId;
    private LocalDateTime fechaTransferencia;
    private String motivo;
    private String observaciones;

    // Información adicional para el registro
    private String nombreMascota;
    private String nombreDuenoAnterior;
    private String nombreDuenoNuevo;

    public TransferenciaPropiedad() {
        this.fechaTransferencia = LocalDateTime.now();
    }

    public TransferenciaPropiedad(int mascotaId, int duenoAnteriorId, int duenoNuevoId,
                                 String motivo, String observaciones) {
        this();
        this.mascotaId = mascotaId;
        this.duenoAnteriorId = duenoAnteriorId;
        this.duenoNuevoId = duenoNuevoId;
        this.motivo = motivo;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMascotaId() { return mascotaId; }
    public void setMascotaId(int mascotaId) { this.mascotaId = mascotaId; }

    public int getDuenoAnteriorId() { return duenoAnteriorId; }
    public void setDuenoAnteriorId(int duenoAnteriorId) { this.duenoAnteriorId = duenoAnteriorId; }

    public int getDuenoNuevoId() { return duenoNuevoId; }
    public void setDuenoNuevoId(int duenoNuevoId) { this.duenoNuevoId = duenoNuevoId; }

    public LocalDateTime getFechaTransferencia() { return fechaTransferencia; }
    public void setFechaTransferencia(LocalDateTime fechaTransferencia) { this.fechaTransferencia = fechaTransferencia; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }

    public String getNombreDuenoAnterior() { return nombreDuenoAnterior; }
    public void setNombreDuenoAnterior(String nombreDuenoAnterior) { this.nombreDuenoAnterior = nombreDuenoAnterior; }

    public String getNombreDuenoNuevo() { return nombreDuenoNuevo; }
    public void setNombreDuenoNuevo(String nombreDuenoNuevo) { this.nombreDuenoNuevo = nombreDuenoNuevo; }

    @Override
    public String toString() {
        return String.format(
            "Transferencia [id=%d, mascota=%s (ID:%d), de=%s a=%s, fecha=%s, motivo=%s]",
            id, nombreMascota != null ? nombreMascota : "N/A", mascotaId,
            nombreDuenoAnterior != null ? nombreDuenoAnterior : "ID:" + duenoAnteriorId,
            nombreDuenoNuevo != null ? nombreDuenoNuevo : "ID:" + duenoNuevoId,
            fechaTransferencia, motivo
        );
    }
}