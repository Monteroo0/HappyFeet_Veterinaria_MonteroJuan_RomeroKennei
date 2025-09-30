package com.happyfeet.dao;

import com.happyfeet.model.Factura;
import com.happyfeet.model.ItemFactura;
import com.happyfeet.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    public int guardar(Factura factura) {
        String sql = "INSERT INTO facturas (dueno_id, fecha_emision, total) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection_Legacy();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, factura.getDuenoId());
            ps.setTimestamp(2, Timestamp.valueOf(factura.getFechaEmision()));
            ps.setBigDecimal(3, factura.getTotal());

            ps.executeUpdate();

            // Obtener el ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int facturaId = rs.getInt(1);
                    factura.setId(facturaId);

                    // Guardar los items de la factura
                    for (ItemFactura item : factura.getItems()) {
                        item.setFacturaId(facturaId);
                        guardarItemFactura(item);
                    }

                    System.out.println("✅ Factura generada exitosamente con ID: " + facturaId);
                    return facturaId;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar factura: " + e.getMessage());
        }

        return -1;
    }

    private void guardarItemFactura(ItemFactura item) {
        String sql = "INSERT INTO items_factura (factura_id, producto_id, servicio_descripcion, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection_Legacy();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, item.getFacturaId());
            if (item.getProductoId() != null) {
                ps.setInt(2, item.getProductoId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, item.getServicioDescripcion());
            ps.setInt(4, item.getCantidad());
            ps.setBigDecimal(5, item.getPrecioUnitario());
            ps.setBigDecimal(6, item.getSubtotal());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar item de factura: " + e.getMessage());
        }
    }

    public List<Factura> listar() {
        String sql = """
            SELECT f.id, f.dueno_id, f.fecha_emision, f.total,
                   d.nombre_completo, d.documento_identidad
            FROM facturas f
            JOIN duenos d ON f.dueno_id = d.id
            ORDER BY f.fecha_emision DESC
            """;

        List<Factura> facturas = new ArrayList<>();

        try (Connection con = DBConnection.getConnection_Legacy();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Factura factura = new Factura();
                factura.setId(rs.getInt("id"));
                factura.setDuenoId(rs.getInt("dueno_id"));
                factura.setFechaEmision(rs.getTimestamp("fecha_emision").toLocalDateTime());
                factura.setTotal(rs.getBigDecimal("total"));
                factura.setNombreDueno(rs.getString("nombre_completo"));
                factura.setDocumentoDueno(rs.getString("documento_identidad"));

                // Cargar los items de la factura
                factura.setItems(obtenerItemsFactura(factura.getId()));

                facturas.add(factura);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar facturas: " + e.getMessage());
        }

        return facturas;
    }

    public Factura obtenerPorId(int facturaId) {
        String sql = """
            SELECT f.id, f.dueno_id, f.fecha_emision, f.total,
                   d.nombre_completo, d.documento_identidad, d.direccion, d.telefono, d.email
            FROM facturas f
            JOIN duenos d ON f.dueno_id = d.id
            WHERE f.id = ?
            """;

        try (Connection con = DBConnection.getConnection_Legacy();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, facturaId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Factura factura = new Factura();
                    factura.setId(rs.getInt("id"));
                    factura.setDuenoId(rs.getInt("dueno_id"));
                    factura.setFechaEmision(rs.getTimestamp("fecha_emision").toLocalDateTime());
                    factura.setTotal(rs.getBigDecimal("total"));
                    factura.setNombreDueno(rs.getString("nombre_completo"));
                    factura.setDocumentoDueno(rs.getString("documento_identidad"));

                    // Cargar los items de la factura
                    factura.setItems(obtenerItemsFactura(facturaId));

                    return factura;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener factura: " + e.getMessage());
        }

        return null;
    }

    private List<ItemFactura> obtenerItemsFactura(int facturaId) {
        String sql = """
            SELECT if.id, if.factura_id, if.producto_id, if.servicio_descripcion,
                   if.cantidad, if.precio_unitario, if.subtotal,
                   i.nombre_producto
            FROM items_factura if
            LEFT JOIN inventario i ON if.producto_id = i.id
            WHERE if.factura_id = ?
            ORDER BY if.id
            """;

        List<ItemFactura> items = new ArrayList<>();

        try (Connection con = DBConnection.getConnection_Legacy();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, facturaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ItemFactura item = new ItemFactura();
                    item.setId(rs.getInt("id"));
                    item.setFacturaId(rs.getInt("factura_id"));

                    Integer productoId = rs.getObject("producto_id", Integer.class);
                    item.setProductoId(productoId);

                    item.setServicioDescripcion(rs.getString("servicio_descripcion"));
                    item.setCantidad(rs.getInt("cantidad"));
                    item.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                    item.setSubtotal(rs.getBigDecimal("subtotal"));
                    item.setNombreProducto(rs.getString("nombre_producto"));

                    items.add(item);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener items de factura: " + e.getMessage());
        }

        return items;
    }

    public List<Factura> buscarPorDueno(int duenoId) {
        String sql = """
            SELECT f.id, f.dueno_id, f.fecha_emision, f.total,
                   d.nombre_completo, d.documento_identidad
            FROM facturas f
            JOIN duenos d ON f.dueno_id = d.id
            WHERE f.dueno_id = ?
            ORDER BY f.fecha_emision DESC
            """;

        List<Factura> facturas = new ArrayList<>();

        try (Connection con = DBConnection.getConnection_Legacy();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, duenoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Factura factura = new Factura();
                    factura.setId(rs.getInt("id"));
                    factura.setDuenoId(rs.getInt("dueno_id"));
                    factura.setFechaEmision(rs.getTimestamp("fecha_emision").toLocalDateTime());
                    factura.setTotal(rs.getBigDecimal("total"));
                    factura.setNombreDueno(rs.getString("nombre_completo"));
                    factura.setDocumentoDueno(rs.getString("documento_identidad"));
                    facturas.add(factura);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar facturas por dueño: " + e.getMessage());
        }

        return facturas;
    }
}