package com.happyfeet.repository;

import com.happyfeet.model.entities.InventarioProveedor;
import com.happyfeet.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Patrón DAO para la relación Inventario-Proveedor
 */
public class InventarioProveedorDAO {

    /**
     * Asocia un producto del inventario con un proveedor
     */
    public int asociar(InventarioProveedor relacion) {
        String sql = """
            INSERT INTO inventario_proveedores (
                inventario_id, proveedor_id, es_proveedor_principal,
                precio_compra, tiempo_entrega_dias
            ) VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, relacion.getInventarioId());
            stmt.setInt(2, relacion.getProveedorId());
            stmt.setBoolean(3, relacion.isEsProveedorPrincipal());

            if (relacion.getPrecioCompra() != null) {
                stmt.setBigDecimal(4, relacion.getPrecioCompra());
            } else {
                stmt.setNull(4, Types.DECIMAL);
            }

            if (relacion.getTiempoEntregaDias() != null) {
                stmt.setInt(5, relacion.getTiempoEntregaDias());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al asociar inventario con proveedor.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    relacion.setId(id);
                    System.out.println("✅ Relación inventario-proveedor creada exitosamente con ID: " + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al asociar inventario con proveedor: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Lista todos los proveedores de un producto específico
     */
    public List<InventarioProveedor> listarProveedoresPorProducto(int inventarioId) {
        List<InventarioProveedor> relaciones = new ArrayList<>();
        String sql = """
            SELECT ip.*, i.nombre_producto, p.nombre as nombre_proveedor
            FROM inventario_proveedores ip
            JOIN inventario i ON ip.inventario_id = i.id
            JOIN proveedores p ON ip.proveedor_id = p.id
            WHERE ip.inventario_id = ?
            ORDER BY ip.es_proveedor_principal DESC, p.nombre
        """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inventarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    relaciones.add(mapearDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar proveedores del producto: " + e.getMessage());
        }
        return relaciones;
    }

    /**
     * Lista todos los productos de un proveedor específico
     */
    public List<InventarioProveedor> listarProductosPorProveedor(int proveedorId) {
        List<InventarioProveedor> relaciones = new ArrayList<>();
        String sql = """
            SELECT ip.*, i.nombre_producto, p.nombre as nombre_proveedor
            FROM inventario_proveedores ip
            JOIN inventario i ON ip.inventario_id = i.id
            JOIN proveedores p ON ip.proveedor_id = p.id
            WHERE ip.proveedor_id = ?
            ORDER BY i.nombre_producto
        """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, proveedorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    relaciones.add(mapearDesdeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar productos del proveedor: " + e.getMessage());
        }
        return relaciones;
    }

    /**
     * Actualiza el proveedor principal de un producto
     */
    public boolean establecerProveedorPrincipal(int inventarioId, int proveedorId) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Primero, quitar el flag de proveedor principal a todos
            String sql1 = "UPDATE inventario_proveedores SET es_proveedor_principal = FALSE WHERE inventario_id = ?";
            try (PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
                stmt1.setInt(1, inventarioId);
                stmt1.executeUpdate();
            }

            // Luego, establecer el nuevo proveedor principal
            String sql2 = """
                UPDATE inventario_proveedores
                SET es_proveedor_principal = TRUE
                WHERE inventario_id = ? AND proveedor_id = ?
            """;
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                stmt2.setInt(1, inventarioId);
                stmt2.setInt(2, proveedorId);
                stmt2.executeUpdate();
            }

            conn.commit();
            System.out.println("✅ Proveedor principal actualizado exitosamente");
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("❌ Error al establecer proveedor principal: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Lista todas las relaciones inventario-proveedor
     */
    public List<InventarioProveedor> listarTodas() {
        List<InventarioProveedor> relaciones = new ArrayList<>();
        String sql = """
            SELECT ip.*, i.nombre_producto, p.nombre as nombre_proveedor
            FROM inventario_proveedores ip
            JOIN inventario i ON ip.inventario_id = i.id
            JOIN proveedores p ON ip.proveedor_id = p.id
            ORDER BY i.nombre_producto, ip.es_proveedor_principal DESC
        """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                relaciones.add(mapearDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar relaciones: " + e.getMessage());
        }
        return relaciones;
    }

    private InventarioProveedor mapearDesdeResultSet(ResultSet rs) throws SQLException {
        InventarioProveedor relacion = new InventarioProveedor();

        relacion.setId(rs.getInt("id"));
        relacion.setInventarioId(rs.getInt("inventario_id"));
        relacion.setProveedorId(rs.getInt("proveedor_id"));
        relacion.setEsProveedorPrincipal(rs.getBoolean("es_proveedor_principal"));

        BigDecimal precio = rs.getBigDecimal("precio_compra");
        if (!rs.wasNull()) {
            relacion.setPrecioCompra(precio);
        }

        int tiempo = rs.getInt("tiempo_entrega_dias");
        if (!rs.wasNull()) {
            relacion.setTiempoEntregaDias(tiempo);
        }

        Timestamp fecha = rs.getTimestamp("fecha_registro");
        if (fecha != null) {
            relacion.setFechaRegistro(fecha.toLocalDateTime());
        }

        // Información adicional del JOIN
        relacion.setNombreProducto(rs.getString("nombre_producto"));
        relacion.setNombreProveedor(rs.getString("nombre_proveedor"));

        return relacion;
    }
}