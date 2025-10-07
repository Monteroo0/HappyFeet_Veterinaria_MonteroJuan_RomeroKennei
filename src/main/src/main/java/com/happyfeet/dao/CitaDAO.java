package com.happyfeet.dao;

import com.happyfeet.model.Cita;
import com.happyfeet.model.CitaEstado;
import com.happyfeet.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    public void guardar(Cita cita) {
        String sql = "INSERT INTO citas (mascota_id, fecha_hora, motivo, estado_id) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection_Legacy();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cita.getMascotaId());
            ps.setTimestamp(2, Timestamp.valueOf(cita.getFechaHora()));
            ps.setString(3, cita.getMotivo());
            ps.setInt(4, cita.getEstadoId());

            ps.executeUpdate();
            System.out.println("✅ Cita guardada exitosamente");

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar la cita: " + e.getMessage());
        }
    }

    public List<Cita> listar() {
        String sql = """
            SELECT c.id, c.mascota_id, c.fecha_hora, c.motivo, c.estado_id,
                   m.nombre as nombre_mascota, d.nombre_completo as nombre_dueno,
                   ce.nombre as estado_nombre
            FROM citas c
            JOIN mascotas m ON c.mascota_id = m.id
            JOIN duenos d ON m.dueno_id = d.id
            JOIN cita_estados ce ON c.estado_id = ce.id
            ORDER BY c.fecha_hora DESC
            """;

        List<Cita> citas = new ArrayList<>();

        try (Connection con = DBConnection.getConnection_Legacy();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cita cita = new Cita();
                cita.setId(rs.getInt("id"));
                cita.setMascotaId(rs.getInt("mascota_id"));
                cita.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                cita.setMotivo(rs.getString("motivo"));
                cita.setEstadoId(rs.getInt("estado_id"));
                cita.setNombreMascota(rs.getString("nombre_mascota"));
                cita.setNombreDueno(rs.getString("nombre_dueno"));
                cita.setEstadoNombre(rs.getString("estado_nombre"));
                citas.add(cita);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar citas: " + e.getMessage());
        }

        return citas;
    }

    public void actualizarEstado(int citaId, int nuevoEstadoId) {
        String sql = "UPDATE citas SET estado_id = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection_Legacy();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevoEstadoId);
            ps.setInt(2, citaId);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("✅ Estado de cita actualizado exitosamente");
            } else {
                System.out.println("❌ No se encontró la cita con ID: " + citaId);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar estado de cita: " + e.getMessage());
        }
    }

    public List<CitaEstado> listarEstados() {
        String sql = "SELECT id, nombre FROM cita_estados ORDER BY id";
        List<CitaEstado> estados = new ArrayList<>();

        try (Connection con = DBConnection.getConnection_Legacy();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CitaEstado estado = new CitaEstado();
                estado.setId(rs.getInt("id"));
                estado.setNombre(rs.getString("nombre"));
                estados.add(estado);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar estados de cita: " + e.getMessage());
        }

        return estados;
    }

    public List<Cita> buscarPorMascota(int mascotaId) {
        String sql = """
            SELECT c.id, c.mascota_id, c.fecha_hora, c.motivo, c.estado_id,
                   m.nombre as nombre_mascota, d.nombre_completo as nombre_dueno,
                   ce.nombre as estado_nombre
            FROM citas c
            JOIN mascotas m ON c.mascota_id = m.id
            JOIN duenos d ON m.dueno_id = d.id
            JOIN cita_estados ce ON c.estado_id = ce.id
            WHERE c.mascota_id = ?
            ORDER BY c.fecha_hora DESC
            """;

        List<Cita> citas = new ArrayList<>();

        try (Connection con = DBConnection.getConnection_Legacy();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mascotaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita cita = new Cita();
                    cita.setId(rs.getInt("id"));
                    cita.setMascotaId(rs.getInt("mascota_id"));
                    cita.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                    cita.setMotivo(rs.getString("motivo"));
                    cita.setEstadoId(rs.getInt("estado_id"));
                    cita.setNombreMascota(rs.getString("nombre_mascota"));
                    cita.setNombreDueno(rs.getString("nombre_dueno"));
                    cita.setEstadoNombre(rs.getString("estado_nombre"));
                    citas.add(cita);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar citas por mascota: " + e.getMessage());
        }

        return citas;
    }
}