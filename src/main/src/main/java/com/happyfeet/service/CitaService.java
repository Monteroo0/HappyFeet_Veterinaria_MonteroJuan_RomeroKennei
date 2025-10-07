package com.happyfeet.service;

import com.happyfeet.dao.CitaDAO;
import com.happyfeet.model.Cita;
import com.happyfeet.model.CitaEstado;

import java.time.LocalDateTime;
import java.util.List;

public class CitaService {
    private final CitaDAO citaDAO = new CitaDAO();

    public void agendarCita(Cita cita) {
        // Validar que la fecha no sea en el pasado
        if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
            System.out.println("❌ No se puede agendar una cita en el pasado");
            return;
        }

        // Por defecto, las citas nuevas se crean como "Programada" (estado 1)
        cita.setEstadoId(1);
        citaDAO.guardar(cita);
    }

    public List<Cita> listarCitas() {
        return citaDAO.listar();
    }

    public List<Cita> buscarCitasPorMascota(int mascotaId) {
        return citaDAO.buscarPorMascota(mascotaId);
    }

    public void cambiarEstadoCita(int citaId, int nuevoEstadoId) {
        citaDAO.actualizarEstado(citaId, nuevoEstadoId);
    }

    public List<CitaEstado> listarEstados() {
        return citaDAO.listarEstados();
    }

    public void mostrarEstadosDisponibles() {
        System.out.println("\n=== Estados de Cita Disponibles ===");
        List<CitaEstado> estados = listarEstados();
        for (CitaEstado estado : estados) {
            System.out.println(estado.getId() + ". " + estado.getNombre());
        }
    }
}