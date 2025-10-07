package com.happyfeet.repository;

import com.happyfeet.model.entities.ProcedimientoEspecial;
import com.happyfeet.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Patrón DAO - Data Access Object para ProcedimientoEspecial
 * Proporciona acceso a datos con abstracción de la base de datos
 */
public class ProcedimientoEspecialDAO {

    /**
     * Registra un nuevo procedimiento especial
     */
    public int registrar(ProcedimientoEspecial procedimiento) {
        String sql = """
            INSERT INTO procedimientos_especiales (
                mascota_id, tipo_procedimiento, fecha_procedimiento, veterinario_responsable,
                diagnostico_preoperatorio, analisis_previos, medicacion_previa,
                ayuno_requerido, alergias_conocidas, descripcion_procedimiento,
                anestesia_utilizada, duracion_minutos, complicaciones, observaciones,
                medicacion_postoperatoria, cuidados_especiales, proxima_revision,
                restricciones, estado_actual
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, procedimiento.getMascotaId());
            stmt.setString(2, procedimiento.getTipoProcedimiento().getDescripcion());
            stmt.setTimestamp(3, Timestamp.valueOf(procedimiento.getFechaProcedimiento()));
            stmt.setString(4, procedimiento.getVeterinarioResponsable());
            stmt.setString(5, procedimiento.getDiagnosticoPreoperatorio());
            stmt.setString(6, procedimiento.getAnalisisPrevios());
            stmt.setString(7, procedimiento.getMedicacionPrevia());
            stmt.setBoolean(8, procedimiento.isAyunoRequerido());
            stmt.setString(9, procedimiento.getAlergiasConocidas());
            stmt.setString(10, procedimiento.getDescripcionProcedimiento());
            stmt.setString(11, procedimiento.getAnestesiaUtilizada());

            if (procedimiento.getDuracionMinutos() != null) {
                stmt.setInt(12, procedimiento.getDuracionMinutos());
            } else {
                stmt.setNull(12, Types.INTEGER);
            }

            stmt.setString(13, procedimiento.getComplicaciones());
            stmt.setString(14, procedimiento.getObservaciones());
            stmt.setString(15, procedimiento.getMedicacionPostoperatoria());
            stmt.setString(16, procedimiento.getCuidadosEspeciales());

            if (procedimiento.getProximaRevision() != null) {
                stmt.setDate(17, Date.valueOf(procedimiento.getProximaRevision()));
            } else {
                stmt.setNull(17, Types.DATE);
            }

            stmt.setString(18, procedimiento.getRestricciones());
            stmt.setString(19, procedimiento.getEstadoActual().getDescripcion());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al registrar procedimiento, no se insertó ninguna fila.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    procedimiento.setId(id);
                    System.out.println("✅ Procedimiento especial registrado exitosamente con ID: " + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al registrar procedimiento: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Busca un procedimiento por ID
     */
    public Optional<ProcedimientoEspecial> buscarPorId(int id) {
        String sql = "SELECT * FROM procedimientos_especiales WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar procedimiento: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Lista todos los procedimientos de una mascota
     */
    public List<ProcedimientoEspecial> listarPorMascota(int mascotaId) {
        List<ProcedimientoEspecial> procedimientos = new ArrayList<>();
        String sql = "SELECT * FROM procedimientos_especiales WHERE mascota_id = ? ORDER BY fecha_procedimiento DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mascotaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    procedimientos.add(mapearDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar procedimientos: " + e.getMessage());
        }
        return procedimientos;
    }

    /**
     * Lista todos los procedimientos con un estado específico
     */
    public List<ProcedimientoEspecial> listarPorEstado(ProcedimientoEspecial.EstadoProcedimiento estado) {
        List<ProcedimientoEspecial> procedimientos = new ArrayList<>();
        String sql = "SELECT * FROM procedimientos_especiales WHERE estado_actual = ? ORDER BY fecha_procedimiento DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado.getDescripcion());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    procedimientos.add(mapearDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar procedimientos por estado: " + e.getMessage());
        }
        return procedimientos;
    }

    /**
     * Actualiza el estado de un procedimiento
     */
    public boolean actualizarEstado(int id, ProcedimientoEspecial.EstadoProcedimiento nuevoEstado) {
        String sql = "UPDATE procedimientos_especiales SET estado_actual = ? WHERE id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado.getDescripcion());
            stmt.setInt(2, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("✅ Estado del procedimiento actualizado exitosamente");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar estado: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lista todos los procedimientos
     */
    public List<ProcedimientoEspecial> listarTodos() {
        List<ProcedimientoEspecial> procedimientos = new ArrayList<>();
        String sql = "SELECT * FROM procedimientos_especiales ORDER BY fecha_procedimiento DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                procedimientos.add(mapearDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar procedimientos: " + e.getMessage());
        }
        return procedimientos;
    }

    /**
     * Mapea un ResultSet a un objeto ProcedimientoEspecial
     */
    private ProcedimientoEspecial mapearDesdeResultSet(ResultSet rs) throws SQLException {
        ProcedimientoEspecial procedimiento = new ProcedimientoEspecial();

        procedimiento.setId(rs.getInt("id"));
        procedimiento.setMascotaId(rs.getInt("mascota_id"));

        // Mapear tipo de procedimiento
        String tipoStr = rs.getString("tipo_procedimiento");
        procedimiento.setTipoProcedimiento(mapearTipoProcedimiento(tipoStr));

        Timestamp fechaProc = rs.getTimestamp("fecha_procedimiento");
        if (fechaProc != null) {
            procedimiento.setFechaProcedimiento(fechaProc.toLocalDateTime());
        }

        procedimiento.setVeterinarioResponsable(rs.getString("veterinario_responsable"));
        procedimiento.setDiagnosticoPreoperatorio(rs.getString("diagnostico_preoperatorio"));
        procedimiento.setAnalisisPrevios(rs.getString("analisis_previos"));
        procedimiento.setMedicacionPrevia(rs.getString("medicacion_previa"));
        procedimiento.setAyunoRequerido(rs.getBoolean("ayuno_requerido"));
        procedimiento.setAlergiasConocidas(rs.getString("alergias_conocidas"));
        procedimiento.setDescripcionProcedimiento(rs.getString("descripcion_procedimiento"));
        procedimiento.setAnestesiaUtilizada(rs.getString("anestesia_utilizada"));

        int duracion = rs.getInt("duracion_minutos");
        if (!rs.wasNull()) {
            procedimiento.setDuracionMinutos(duracion);
        }

        procedimiento.setComplicaciones(rs.getString("complicaciones"));
        procedimiento.setObservaciones(rs.getString("observaciones"));
        procedimiento.setMedicacionPostoperatoria(rs.getString("medicacion_postoperatoria"));
        procedimiento.setCuidadosEspeciales(rs.getString("cuidados_especiales"));

        Date proximaRev = rs.getDate("proxima_revision");
        if (proximaRev != null) {
            procedimiento.setProximaRevision(proximaRev.toLocalDate());
        }

        procedimiento.setRestricciones(rs.getString("restricciones"));

        // Mapear estado
        String estadoStr = rs.getString("estado_actual");
        procedimiento.setEstadoActual(mapearEstadoProcedimiento(estadoStr));

        Timestamp fechaReg = rs.getTimestamp("fecha_registro");
        if (fechaReg != null) {
            procedimiento.setFechaRegistro(fechaReg.toLocalDateTime());
        }

        return procedimiento;
    }

    private ProcedimientoEspecial.TipoProcedimiento mapearTipoProcedimiento(String tipo) {
        return switch (tipo) {
            case "Cirugía" -> ProcedimientoEspecial.TipoProcedimiento.CIRUGIA;
            case "Tratamiento Complejo" -> ProcedimientoEspecial.TipoProcedimiento.TRATAMIENTO_COMPLEJO;
            case "Procedimiento Especializado" -> ProcedimientoEspecial.TipoProcedimiento.PROCEDIMIENTO_ESPECIALIZADO;
            default -> ProcedimientoEspecial.TipoProcedimiento.PROCEDIMIENTO_ESPECIALIZADO;
        };
    }

    private ProcedimientoEspecial.EstadoProcedimiento mapearEstadoProcedimiento(String estado) {
        return switch (estado) {
            case "En Recuperación" -> ProcedimientoEspecial.EstadoProcedimiento.EN_RECUPERACION;
            case "Seguimiento" -> ProcedimientoEspecial.EstadoProcedimiento.SEGUIMIENTO;
            case "Alta Médica" -> ProcedimientoEspecial.EstadoProcedimiento.ALTA_MEDICA;
            default -> ProcedimientoEspecial.EstadoProcedimiento.EN_RECUPERACION;
        };
    }
}