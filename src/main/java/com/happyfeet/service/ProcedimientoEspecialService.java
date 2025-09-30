package com.happyfeet.service;

import com.happyfeet.model.entities.ProcedimientoEspecial;
import com.happyfeet.repository.ProcedimientoEspecialDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de procedimientos especiales
 * Implementa lógica de negocio y aplica programación funcional
 * Patrón Strategy implícito en el manejo de diferentes tipos de procedimientos
 */
public class ProcedimientoEspecialService {

    private final ProcedimientoEspecialDAO procedimientoDAO;

    public ProcedimientoEspecialService() {
        this.procedimientoDAO = new ProcedimientoEspecialDAO();
    }

    /**
     * Registra un nuevo procedimiento especial con validaciones
     */
    public int registrarProcedimiento(ProcedimientoEspecial procedimiento) {
        // Validación básica
        if (procedimiento.getMascotaId() <= 0) {
            System.out.println("❌ ID de mascota inválido");
            return -1;
        }

        if (procedimiento.getDiagnosticoPreoperatorio() == null ||
            procedimiento.getDiagnosticoPreoperatorio().trim().isEmpty()) {
            System.out.println("❌ El diagnóstico preoperatorio es obligatorio");
            return -1;
        }

        if (procedimiento.getDescripcionProcedimiento() == null ||
            procedimiento.getDescripcionProcedimiento().trim().isEmpty()) {
            System.out.println("❌ La descripción del procedimiento es obligatoria");
            return -1;
        }

        if (procedimiento.getCuidadosEspeciales() == null ||
            procedimiento.getCuidadosEspeciales().trim().isEmpty()) {
            System.out.println("❌ Los cuidados especiales son obligatorios");
            return -1;
        }

        // Calcular próxima revisión si no está establecida (7 días después por defecto)
        if (procedimiento.getProximaRevision() == null) {
            procedimiento.setProximaRevision(LocalDate.now().plusDays(7));
        }

        return procedimientoDAO.registrar(procedimiento);
    }

    /**
     * Busca un procedimiento por ID
     */
    public Optional<ProcedimientoEspecial> buscarPorId(int id) {
        return procedimientoDAO.buscarPorId(id);
    }

    /**
     * Lista todos los procedimientos de una mascota
     */
    public List<ProcedimientoEspecial> listarPorMascota(int mascotaId) {
        return procedimientoDAO.listarPorMascota(mascotaId);
    }

    /**
     * Lista procedimientos en recuperación (Programación Funcional)
     */
    public List<ProcedimientoEspecial> listarEnRecuperacion() {
        return procedimientoDAO
                .listarTodos()
                .stream()
                .filter(p -> p.getEstadoActual() == ProcedimientoEspecial.EstadoProcedimiento.EN_RECUPERACION)
                .collect(Collectors.toList());
    }

    /**
     * Lista procedimientos que requieren revisión próxima (Programación Funcional)
     */
    public List<ProcedimientoEspecial> listarProximasRevisiones(int diasAnticipacion) {
        LocalDate fechaLimite = LocalDate.now().plusDays(diasAnticipacion);

        return procedimientoDAO
                .listarTodos()
                .stream()
                .filter(p -> p.getProximaRevision() != null)
                .filter(p -> !p.getProximaRevision().isAfter(fechaLimite))
                .filter(p -> p.getEstadoActual() != ProcedimientoEspecial.EstadoProcedimiento.ALTA_MEDICA)
                .sorted((p1, p2) -> p1.getProximaRevision().compareTo(p2.getProximaRevision()))
                .collect(Collectors.toList());
    }

    /**
     * Actualiza el estado de un procedimiento
     */
    public boolean actualizarEstado(int id, ProcedimientoEspecial.EstadoProcedimiento nuevoEstado) {
        return procedimientoDAO.actualizarEstado(id, nuevoEstado);
    }

    /**
     * Genera reporte de procedimientos por tipo (Programación Funcional)
     */
    public void generarReportePorTipo() {
        System.out.println("\n=== REPORTE DE PROCEDIMIENTOS POR TIPO ===");

        List<ProcedimientoEspecial> todos = procedimientoDAO.listarTodos();

        // Agrupar y contar por tipo usando streams
        todos.stream()
             .collect(Collectors.groupingBy(
                 p -> p.getTipoProcedimiento(),
                 Collectors.counting()
             ))
             .forEach((tipo, cantidad) ->
                 System.out.printf("%s: %d procedimiento(s)%n",
                     tipo.getDescripcion(), cantidad)
             );

        System.out.println("Total de procedimientos: " + todos.size());
    }

    /**
     * Genera estadísticas de procedimientos (Programación Funcional)
     */
    public void generarEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS DE PROCEDIMIENTOS ESPECIALES ===");

        List<ProcedimientoEspecial> todos = procedimientoDAO.listarTodos();

        if (todos.isEmpty()) {
            System.out.println("No hay procedimientos registrados.");
            return;
        }

        // Contar por estado
        System.out.println("\nPor Estado:");
        todos.stream()
             .collect(Collectors.groupingBy(
                 p -> p.getEstadoActual(),
                 Collectors.counting()
             ))
             .forEach((estado, cantidad) ->
                 System.out.printf("  %s: %d%n", estado.getDescripcion(), cantidad)
             );

        // Contar por tipo
        System.out.println("\nPor Tipo:");
        todos.stream()
             .collect(Collectors.groupingBy(
                 p -> p.getTipoProcedimiento(),
                 Collectors.counting()
             ))
             .forEach((tipo, cantidad) ->
                 System.out.printf("  %s: %d%n", tipo.getDescripcion(), cantidad)
             );

        // Calcular duración promedio
        double duracionPromedio = todos.stream()
                .filter(p -> p.getDuracionMinutos() != null)
                .mapToInt(ProcedimientoEspecial::getDuracionMinutos)
                .average()
                .orElse(0.0);

        System.out.printf("\nDuración promedio: %.2f minutos%n", duracionPromedio);

        // Contar procedimientos con complicaciones
        long conComplicaciones = todos.stream()
                .filter(p -> p.getComplicaciones() != null && !p.getComplicaciones().trim().isEmpty())
                .count();

        System.out.printf("Procedimientos con complicaciones: %d (%.1f%%)%n",
                conComplicaciones, (conComplicaciones * 100.0) / todos.size());
    }

    /**
     * Lista todos los procedimientos
     */
    public List<ProcedimientoEspecial> listarTodos() {
        return procedimientoDAO.listarTodos();
    }

    /**
     * Muestra los tipos de procedimiento disponibles
     */
    public void mostrarTiposDisponibles() {
        System.out.println("\nTipos de Procedimiento:");
        int i = 1;
        for (ProcedimientoEspecial.TipoProcedimiento tipo : ProcedimientoEspecial.TipoProcedimiento.values()) {
            System.out.printf("%d. %s%n", i++, tipo.getDescripcion());
        }
    }

    /**
     * Muestra los estados disponibles
     */
    public void mostrarEstadosDisponibles() {
        System.out.println("\nEstados de Procedimiento:");
        int i = 1;
        for (ProcedimientoEspecial.EstadoProcedimiento estado : ProcedimientoEspecial.EstadoProcedimiento.values()) {
            System.out.printf("%d. %s%n", i++, estado.getDescripcion());
        }
    }
}