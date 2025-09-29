package com.happyfeet.dao;

import com.happyfeet.model.EventoTipo;
import com.happyfeet.model.HistorialMedico;
import com.happyfeet.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistorialMedicoDAO {

    public void guardar(HistorialMedico historial) {
        String sql = "INSERT INTO historial_medico (mascota_id, fecha_evento, evento_tipo_id, descripcion, diagnostico, tratamiento_recomendado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, historial.getMascotaId());
            ps.setDate(2, Date.valueOf(historial.getFechaEvento()));
            ps.setInt(3, historial.getEventoTipoId());
            ps.setString(4, historial.getDescripcion());
            ps.setString(5, historial.getDiagnostico());
            ps.setString(6, historial.getTratamientoRecomendado());

            ps.executeUpdate();
            System.out.println("✅ Registro médico guardado exitosamente");

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar historial médico: " + e.getMessage());
        }
    }

    public List<HistorialMedico> listar() {
        String sql = """
            SELECT h.id, h.mascota_id, h.fecha_evento, h.evento_tipo_id, h.descripcion, h.diagnostico, h.tratamiento_recomendado,
                   m.nombre as nombre_mascota, d.nombre_completo as nombre_dueno,
                   et.nombre as tipo_evento
            FROM historial_medico h
            JOIN mascotas m ON h.mascota_id = m.id
            JOIN duenos d ON m.dueno_id = d.id
            JOIN evento_tipos et ON h.evento_tipo_id = et.id
            ORDER BY h.fecha_evento DESC
            """;

        List<HistorialMedico> historiales = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HistorialMedico historial = new HistorialMedico();
                historial.setId(rs.getInt("id"));
                historial.setMascotaId(rs.getInt("mascota_id"));
                historial.setFechaEvento(rs.getDate("fecha_evento").toLocalDate());
                historial.setEventoTipoId(rs.getInt("evento_tipo_id"));
                historial.setDescripcion(rs.getString("descripcion"));
                historial.setDiagnostico(rs.getString("diagnostico"));
                historial.setTratamientoRecomendado(rs.getString("tratamiento_recomendado"));
                historial.setNombreMascota(rs.getString("nombre_mascota"));
                historial.setNombreDueno(rs.getString("nombre_dueno"));
                historial.setTipoEvento(rs.getString("tipo_evento"));
                historiales.add(historial);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar historial médico: " + e.getMessage());
        }

        return historiales;
    }

    public List<HistorialMedico> buscarPorMascota(int mascotaId) {
        String sql = """
            SELECT h.id, h.mascota_id, h.fecha_evento, h.evento_tipo_id, h.descripcion, h.diagnostico, h.tratamiento_recomendado,
                   m.nombre as nombre_mascota, d.nombre_completo as nombre_dueno,
                   et.nombre as tipo_evento
            FROM historial_medico h
            JOIN mascotas m ON h.mascota_id = m.id
            JOIN duenos d ON m.dueno_id = d.id
            JOIN evento_tipos et ON h.evento_tipo_id = et.id
            WHERE h.mascota_id = ?
            ORDER BY h.fecha_evento DESC
            """;

        List<HistorialMedico> historiales = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mascotaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HistorialMedico historial = new HistorialMedico();
                    historial.setId(rs.getInt("id"));
                    historial.setMascotaId(rs.getInt("mascota_id"));
                    historial.setFechaEvento(rs.getDate("fecha_evento").toLocalDate());
                    historial.setEventoTipoId(rs.getInt("evento_tipo_id"));
                    historial.setDescripcion(rs.getString("descripcion"));
                    historial.setDiagnostico(rs.getString("diagnostico"));
                    historial.setTratamientoRecomendado(rs.getString("tratamiento_recomendado"));
                    historial.setNombreMascota(rs.getString("nombre_mascota"));
                    historial.setNombreDueno(rs.getString("nombre_dueno"));
                    historial.setTipoEvento(rs.getString("tipo_evento"));
                    historiales.add(historial);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar historial por mascota: " + e.getMessage());
        }

        return historiales;
    }

    public List<EventoTipo> listarTiposEvento() {
        String sql = "SELECT id, nombre FROM evento_tipos ORDER BY id";
        List<EventoTipo> tipos = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                EventoTipo tipo = new EventoTipo();
                tipo.setId(rs.getInt("id"));
                tipo.setNombre(rs.getString("nombre"));
                tipos.add(tipo);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar tipos de evento: " + e.getMessage());
        }

        return tipos;
    }
}