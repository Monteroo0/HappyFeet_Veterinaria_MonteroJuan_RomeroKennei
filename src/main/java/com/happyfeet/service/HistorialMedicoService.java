package com.happyfeet.service;

import com.happyfeet.dao.HistorialMedicoDAO;
import com.happyfeet.model.EventoTipo;
import com.happyfeet.model.HistorialMedico;

import java.time.LocalDate;
import java.util.List;

public class HistorialMedicoService {
    private final HistorialMedicoDAO historialDAO = new HistorialMedicoDAO();
    private final InventarioService inventarioService = new InventarioService();

    public void registrarEvento(HistorialMedico historial) {
        // Validar que la fecha no sea futura
        if (historial.getFechaEvento().isAfter(LocalDate.now())) {
            System.out.println("❌ No se puede registrar un evento médico en el futuro");
            return;
        }

        historialDAO.guardar(historial);
    }

    public void registrarConsultaCompleta(int mascotaId, String descripcion, String diagnostico,
                                          String tratamiento, int tipoEvento, String medicamentoPrescrito, int cantidad) {
        // Crear el registro médico
        HistorialMedico historial = new HistorialMedico();
        historial.setMascotaId(mascotaId);
        historial.setFechaEvento(LocalDate.now());
        historial.setEventoTipoId(tipoEvento);
        historial.setDescripcion(descripcion);
        historial.setDiagnostico(diagnostico);
        historial.setTratamientoRecomendado(tratamiento);

        // Guardar el historial médico
        registrarEvento(historial);

        // Si se prescribió medicamento, deducir del inventario
        if (medicamentoPrescrito != null && !medicamentoPrescrito.trim().isEmpty() && cantidad > 0) {
            boolean deducido = inventarioService.deducirStock(medicamentoPrescrito, cantidad);
            if (deducido) {
                System.out.println("✅ Medicamento '" + medicamentoPrescrito + "' deducido del inventario");
            } else {
                System.out.println("⚠️  Advertencia: No se pudo deducir el medicamento del inventario");
            }
        }
    }

    public List<HistorialMedico> listarHistorial() {
        return historialDAO.listar();
    }

    public List<HistorialMedico> buscarHistorialPorMascota(int mascotaId) {
        return historialDAO.buscarPorMascota(mascotaId);
    }

    public List<EventoTipo> listarTiposEvento() {
        return historialDAO.listarTiposEvento();
    }

    public void mostrarTiposEventoDisponibles() {
        System.out.println("\n=== Tipos de Evento Médico Disponibles ===");
        List<EventoTipo> tipos = listarTiposEvento();
        for (EventoTipo tipo : tipos) {
            System.out.println(tipo.getId() + ". " + tipo.getNombre());
        }
    }
}