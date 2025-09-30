package com.happyfeet.repository;

import com.happyfeet.model.entities.TransferenciaPropiedad;
import com.happyfeet.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Patrón DAO para TransferenciaPropiedad
 */
public class TransferenciaPropiedadDAO {

    /**
     * Registra una nueva transferencia de propiedad
     */
    public int registrar(TransferenciaPropiedad transferencia) {
        String sql = """
            INSERT INTO transferencias_propiedad (
                mascota_id, dueno_anterior_id, dueno_nuevo_id,
                fecha_transferencia, motivo, observaciones
            ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, transferencia.getMascotaId());
            stmt.setInt(2, transferencia.getDuenoAnteriorId());
            stmt.setInt(3, transferencia.getDuenoNuevoId());
            stmt.setTimestamp(4, Timestamp.valueOf(transferencia.getFechaTransferencia()));
            stmt.setString(5, transferencia.getMotivo());
            stmt.setString(6, transferencia.getObservaciones());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al registrar transferencia.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    transferencia.setId(id);
                    System.out.println("✅ Transferencia de propiedad registrada exitosamente con ID: " + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al registrar transferencia: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Lista todas las transferencias de una mascota específica
     */
    public List<TransferenciaPropiedad> listarPorMascota(int mascotaId) {
        List<TransferenciaPropiedad> transferencias = new ArrayList<>();
        String sql = """
            SELECT t.*, m.nombre as nombre_mascota,
                   da.nombre_completo as nombre_dueno_anterior,
                   dn.nombre_completo as nombre_dueno_nuevo
            FROM transferencias_propiedad t
            JOIN mascotas m ON t.mascota_id = m.id
            JOIN duenos da ON t.dueno_anterior_id = da.id
            JOIN duenos dn ON t.dueno_nuevo_id = dn.id
            WHERE t.mascota_id = ?
            ORDER BY t.fecha_transferencia DESC
        """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mascotaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transferencias.add(mapearDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar transferencias: " + e.getMessage());
        }
        return transferencias;
    }

    /**
     * Lista todas las transferencias
     */
    public List<TransferenciaPropiedad> listarTodas() {
        List<TransferenciaPropiedad> transferencias = new ArrayList<>();
        String sql = """
            SELECT t.*, m.nombre as nombre_mascota,
                   da.nombre_completo as nombre_dueno_anterior,
                   dn.nombre_completo as nombre_dueno_nuevo
            FROM transferencias_propiedad t
            JOIN mascotas m ON t.mascota_id = m.id
            JOIN duenos da ON t.dueno_anterior_id = da.id
            JOIN duenos dn ON t.dueno_nuevo_id = dn.id
            ORDER BY t.fecha_transferencia DESC
        """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transferencias.add(mapearDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar transferencias: " + e.getMessage());
        }
        return transferencias;
    }

    private TransferenciaPropiedad mapearDesdeResultSet(ResultSet rs) throws SQLException {
        TransferenciaPropiedad transferencia = new TransferenciaPropiedad();

        transferencia.setId(rs.getInt("id"));
        transferencia.setMascotaId(rs.getInt("mascota_id"));
        transferencia.setDuenoAnteriorId(rs.getInt("dueno_anterior_id"));
        transferencia.setDuenoNuevoId(rs.getInt("dueno_nuevo_id"));

        Timestamp fecha = rs.getTimestamp("fecha_transferencia");
        if (fecha != null) {
            transferencia.setFechaTransferencia(fecha.toLocalDateTime());
        }

        transferencia.setMotivo(rs.getString("motivo"));
        transferencia.setObservaciones(rs.getString("observaciones"));

        // Información adicional del JOIN
        transferencia.setNombreMascota(rs.getString("nombre_mascota"));
        transferencia.setNombreDuenoAnterior(rs.getString("nombre_dueno_anterior"));
        transferencia.setNombreDuenoNuevo(rs.getString("nombre_dueno_nuevo"));

        return transferencia;
    }
}