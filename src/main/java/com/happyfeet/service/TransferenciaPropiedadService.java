package com.happyfeet.service;

import com.happyfeet.dao.MascotaDAO;
import com.happyfeet.model.Mascota;
import com.happyfeet.model.entities.TransferenciaPropiedad;
import com.happyfeet.repository.TransferenciaPropiedadDAO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar transferencias de propiedad de mascotas
 * Implementa programación funcional y patrón Observer (notificaciones)
 */
public class TransferenciaPropiedadService {

    private final TransferenciaPropiedadDAO transferenciaDAO;
    private final MascotaDAO mascotaDAO;

    // Patrón Observer: Lista de observadores que serán notificados
    private final List<TransferenciaObserver> observadores;

    public TransferenciaPropiedadService() {
        this.transferenciaDAO = new TransferenciaPropiedadDAO();
        this.mascotaDAO = new MascotaDAO();
        this.observadores = new java.util.ArrayList<>();
    }

    /**
     * Interface Observer para notificaciones de transferencias
     */
    public interface TransferenciaObserver {
        void onTransferenciaRealizada(TransferenciaPropiedad transferencia);
    }

    /**
     * Agrega un observador para recibir notificaciones
     */
    public void agregarObservador(TransferenciaObserver observer) {
        observadores.add(observer);
    }

    /**
     * Notifica a todos los observadores sobre una transferencia
     */
    private void notificarObservadores(TransferenciaPropiedad transferencia) {
        observadores.forEach(observer -> observer.onTransferenciaRealizada(transferencia));
    }

    /**
     * Transfiere la propiedad de una mascota a un nuevo dueño
     */
    public boolean transferirPropiedad(int mascotaId, int duenoNuevoId, String motivo, String observaciones) {
        // Obtener mascota actual
        Mascota mascota = mascotaDAO.buscarPorId(mascotaId);

        if (mascota == null) {
            System.out.println("❌ Mascota no encontrada");
            return false;
        }

        int duenoAnteriorId = mascota.getDuenoId();

        if (duenoAnteriorId == duenoNuevoId) {
            System.out.println("❌ El nuevo dueño es el mismo que el actual");
            return false;
        }

        // Crear registro de transferencia
        TransferenciaPropiedad transferencia = new TransferenciaPropiedad(
            mascotaId, duenoAnteriorId, duenoNuevoId, motivo, observaciones
        );

        // Registrar transferencia
        int transferenciaId = transferenciaDAO.registrar(transferencia);

        if (transferenciaId <= 0) {
            System.out.println("❌ Error al registrar la transferencia");
            return false;
        }

        // Actualizar el dueño de la mascota
        mascota.setDuenoId(duenoNuevoId);
        boolean actualizado = mascotaDAO.actualizar(mascota);

        if (actualizado) {
            System.out.println("✅ Transferencia de propiedad completada exitosamente");
            System.out.println("La mascota '" + mascota.getNombre() + "' ahora pertenece al nuevo dueño");

            // Notificar a los observadores
            notificarObservadores(transferencia);

            return true;
        } else {
            System.out.println("❌ Error al actualizar el dueño de la mascota");
            return false;
        }
    }

    /**
     * Lista todas las transferencias de una mascota específica
     */
    public List<TransferenciaPropiedad> listarPorMascota(int mascotaId) {
        return transferenciaDAO.listarPorMascota(mascotaId);
    }

    /**
     * Lista todas las transferencias realizadas
     */
    public List<TransferenciaPropiedad> listarTodas() {
        return transferenciaDAO.listarTodas();
    }

    /**
     * Genera reporte de transferencias por motivo (Programación Funcional)
     */
    public void generarReportePorMotivo() {
        System.out.println("\n=== REPORTE DE TRANSFERENCIAS POR MOTIVO ===");

        List<TransferenciaPropiedad> transferencias = transferenciaDAO.listarTodas();

        if (transferencias.isEmpty()) {
            System.out.println("No hay transferencias registradas.");
            return;
        }

        // Agrupar y contar por motivo
        transferencias.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getMotivo() != null ? t.getMotivo() : "Sin motivo",
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .forEach(entry ->
                        System.out.printf("  %s: %d transferencia(s)%n", entry.getKey(), entry.getValue())
                );

        System.out.println("\nTotal de transferencias: " + transferencias.size());
    }

    /**
     * Lista transferencias recientes (últimos N días) - Programación Funcional
     */
    public List<TransferenciaPropiedad> listarTransferenciasRecientes(int diasAtras) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasAtras);

        return transferenciaDAO.listarTodas()
                .stream()
                .filter(t -> t.getFechaTransferencia().isAfter(fechaLimite))
                .sorted((t1, t2) -> t2.getFechaTransferencia().compareTo(t1.getFechaTransferencia()))
                .collect(Collectors.toList());
    }

    /**
     * Muestra el historial completo de transferencias de una mascota
     */
    public void mostrarHistorialMascota(int mascotaId) {
        List<TransferenciaPropiedad> transferencias = listarPorMascota(mascotaId);

        if (transferencias.isEmpty()) {
            System.out.println("Esta mascota no tiene transferencias registradas.");
            return;
        }

        System.out.println("\n=== HISTORIAL DE TRANSFERENCIAS ===");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        transferencias.forEach(t -> {
            System.out.println("\n-".repeat(50));
            System.out.println("Fecha: " + t.getFechaTransferencia().format(formatter));
            System.out.println("De: " + t.getNombreDuenoAnterior());
            System.out.println("A: " + t.getNombreDuenoNuevo());
            System.out.println("Motivo: " + (t.getMotivo() != null ? t.getMotivo() : "No especificado"));
            if (t.getObservaciones() != null && !t.getObservaciones().trim().isEmpty()) {
                System.out.println("Observaciones: " + t.getObservaciones());
            }
        });
    }
}