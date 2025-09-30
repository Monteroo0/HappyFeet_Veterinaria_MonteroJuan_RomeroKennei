package com.happyfeet.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa un procedimiento médico especial
 * (cirugías, tratamientos complejos, procedimientos especializados)
 */
public class ProcedimientoEspecial {
    private int id;
    private int mascotaId;
    private TipoProcedimiento tipoProcedimiento;
    private LocalDateTime fechaProcedimiento;
    private String veterinarioResponsable;

    // Información preoperatoria
    private String diagnosticoPreoperatorio;
    private String analisisPrevios;
    private String medicacionPrevia;
    private boolean ayunoRequerido;
    private String alergiasConocidas;

    // Detalles del procedimiento
    private String descripcionProcedimiento;
    private String anestesiaUtilizada;
    private Integer duracionMinutos;
    private String complicaciones;
    private String observaciones;

    // Seguimiento postoperatorio
    private String medicacionPostoperatoria;
    private String cuidadosEspeciales;
    private LocalDate proximaRevision;
    private String restricciones;
    private EstadoProcedimiento estadoActual;

    private LocalDateTime fechaRegistro;

    public enum TipoProcedimiento {
        CIRUGIA("Cirugía"),
        TRATAMIENTO_COMPLEJO("Tratamiento Complejo"),
        PROCEDIMIENTO_ESPECIALIZADO("Procedimiento Especializado");

        private final String descripcion;

        TipoProcedimiento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    public enum EstadoProcedimiento {
        EN_RECUPERACION("En Recuperación"),
        SEGUIMIENTO("Seguimiento"),
        ALTA_MEDICA("Alta Médica");

        private final String descripcion;

        EstadoProcedimiento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Constructor vacío
    public ProcedimientoEspecial() {
        this.estadoActual = EstadoProcedimiento.EN_RECUPERACION;
        this.fechaRegistro = LocalDateTime.now();
    }

    // Constructor con parámetros principales
    public ProcedimientoEspecial(int mascotaId, TipoProcedimiento tipoProcedimiento,
                                LocalDateTime fechaProcedimiento, String veterinarioResponsable,
                                String diagnosticoPreoperatorio, String descripcionProcedimiento,
                                String cuidadosEspeciales) {
        this();
        this.mascotaId = mascotaId;
        this.tipoProcedimiento = tipoProcedimiento;
        this.fechaProcedimiento = fechaProcedimiento;
        this.veterinarioResponsable = veterinarioResponsable;
        this.diagnosticoPreoperatorio = diagnosticoPreoperatorio;
        this.descripcionProcedimiento = descripcionProcedimiento;
        this.cuidadosEspeciales = cuidadosEspeciales;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMascotaId() { return mascotaId; }
    public void setMascotaId(int mascotaId) { this.mascotaId = mascotaId; }

    public TipoProcedimiento getTipoProcedimiento() { return tipoProcedimiento; }
    public void setTipoProcedimiento(TipoProcedimiento tipoProcedimiento) { this.tipoProcedimiento = tipoProcedimiento; }

    public LocalDateTime getFechaProcedimiento() { return fechaProcedimiento; }
    public void setFechaProcedimiento(LocalDateTime fechaProcedimiento) { this.fechaProcedimiento = fechaProcedimiento; }

    public String getVeterinarioResponsable() { return veterinarioResponsable; }
    public void setVeterinarioResponsable(String veterinarioResponsable) { this.veterinarioResponsable = veterinarioResponsable; }

    public String getDiagnosticoPreoperatorio() { return diagnosticoPreoperatorio; }
    public void setDiagnosticoPreoperatorio(String diagnosticoPreoperatorio) { this.diagnosticoPreoperatorio = diagnosticoPreoperatorio; }

    public String getAnalisisPrevios() { return analisisPrevios; }
    public void setAnalisisPrevios(String analisisPrevios) { this.analisisPrevios = analisisPrevios; }

    public String getMedicacionPrevia() { return medicacionPrevia; }
    public void setMedicacionPrevia(String medicacionPrevia) { this.medicacionPrevia = medicacionPrevia; }

    public boolean isAyunoRequerido() { return ayunoRequerido; }
    public void setAyunoRequerido(boolean ayunoRequerido) { this.ayunoRequerido = ayunoRequerido; }

    public String getAlergiasConocidas() { return alergiasConocidas; }
    public void setAlergiasConocidas(String alergiasConocidas) { this.alergiasConocidas = alergiasConocidas; }

    public String getDescripcionProcedimiento() { return descripcionProcedimiento; }
    public void setDescripcionProcedimiento(String descripcionProcedimiento) { this.descripcionProcedimiento = descripcionProcedimiento; }

    public String getAnestesiaUtilizada() { return anestesiaUtilizada; }
    public void setAnestesiaUtilizada(String anestesiaUtilizada) { this.anestesiaUtilizada = anestesiaUtilizada; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public String getComplicaciones() { return complicaciones; }
    public void setComplicaciones(String complicaciones) { this.complicaciones = complicaciones; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getMedicacionPostoperatoria() { return medicacionPostoperatoria; }
    public void setMedicacionPostoperatoria(String medicacionPostoperatoria) { this.medicacionPostoperatoria = medicacionPostoperatoria; }

    public String getCuidadosEspeciales() { return cuidadosEspeciales; }
    public void setCuidadosEspeciales(String cuidadosEspeciales) { this.cuidadosEspeciales = cuidadosEspeciales; }

    public LocalDate getProximaRevision() { return proximaRevision; }
    public void setProximaRevision(LocalDate proximaRevision) { this.proximaRevision = proximaRevision; }

    public String getRestricciones() { return restricciones; }
    public void setRestricciones(String restricciones) { this.restricciones = restricciones; }

    public EstadoProcedimiento getEstadoActual() { return estadoActual; }
    public void setEstadoActual(EstadoProcedimiento estadoActual) { this.estadoActual = estadoActual; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return String.format(
            "ProcedimientoEspecial [id=%d, mascotaId=%d, tipo=%s, fecha=%s, veterinario=%s, estado=%s]",
            id, mascotaId,
            tipoProcedimiento != null ? tipoProcedimiento.getDescripcion() : "N/A",
            fechaProcedimiento, veterinarioResponsable,
            estadoActual != null ? estadoActual.getDescripcion() : "N/A"
        );
    }
}