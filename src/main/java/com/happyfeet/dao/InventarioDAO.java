package com.happyfeet.dao;

import com.happyfeet.model.Inventario;
import com.happyfeet.model.ProductoTipo;
import com.happyfeet.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO {

    public void guardar(Inventario inventario) {
        String sql = "INSERT INTO inventario (nombre_producto, producto_tipo_id, descripcion, fabricante, lote, cantidad_stock, stock_minimo, fecha_vencimiento, precio_venta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, inventario.getNombreProducto());
            ps.setInt(2, inventario.getProductoTipoId());
            ps.setString(3, inventario.getDescripcion());
            ps.setString(4, inventario.getFabricante());
            ps.setString(5, inventario.getLote());
            ps.setInt(6, inventario.getCantidadStock());
            ps.setInt(7, inventario.getStockMinimo());
            ps.setDate(8, inventario.getFechaVencimiento() != null ? Date.valueOf(inventario.getFechaVencimiento()) : null);
            ps.setBigDecimal(9, inventario.getPrecioVenta());

            ps.executeUpdate();
            System.out.println("✅ Producto agregado al inventario exitosamente");

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar producto en inventario: " + e.getMessage());
        }
    }

    public List<Inventario> listar() {
        String sql = """
            SELECT i.id, i.nombre_producto, i.producto_tipo_id, i.descripcion, i.fabricante,
                   i.lote, i.cantidad_stock, i.stock_minimo, i.fecha_vencimiento, i.precio_venta,
                   pt.nombre as tipo_producto
            FROM inventario i
            JOIN producto_tipos pt ON i.producto_tipo_id = pt.id
            ORDER BY i.nombre_producto
            """;

        List<Inventario> productos = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Inventario producto = new Inventario();
                producto.setId(rs.getInt("id"));
                producto.setNombreProducto(rs.getString("nombre_producto"));
                producto.setProductoTipoId(rs.getInt("producto_tipo_id"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setFabricante(rs.getString("fabricante"));
                producto.setLote(rs.getString("lote"));
                producto.setCantidadStock(rs.getInt("cantidad_stock"));
                producto.setStockMinimo(rs.getInt("stock_minimo"));

                Date fechaVenc = rs.getDate("fecha_vencimiento");
                if (fechaVenc != null) {
                    producto.setFechaVencimiento(fechaVenc.toLocalDate());
                }

                producto.setPrecioVenta(rs.getBigDecimal("precio_venta"));
                producto.setTipoProducto(rs.getString("tipo_producto"));
                productos.add(producto);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar inventario: " + e.getMessage());
        }

        return productos;
    }

    public boolean actualizarStock(int productoId, int nuevaCantidad) {
        String sql = "UPDATE inventario SET cantidad_stock = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, productoId);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    public boolean deducirStock(String nombreProducto, int cantidad) {
        // Primero verificar si el producto existe y tiene suficiente stock
        String sqlSelect = "SELECT id, cantidad_stock FROM inventario WHERE nombre_producto = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlSelect)) {

            ps.setString(1, nombreProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int stockActual = rs.getInt("cantidad_stock");

                    if (stockActual >= cantidad) {
                        // Actualizar el stock
                        return actualizarStock(id, stockActual - cantidad);
                    } else {
                        System.out.println("❌ Stock insuficiente. Disponible: " + stockActual + ", Solicitado: " + cantidad);
                        return false;
                    }
                } else {
                    System.out.println("❌ Producto '" + nombreProducto + "' no encontrado en inventario");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al deducir stock: " + e.getMessage());
            return false;
        }
    }

    public List<Inventario> buscarPorNombre(String nombre) {
        String sql = """
            SELECT i.id, i.nombre_producto, i.producto_tipo_id, i.descripcion, i.fabricante,
                   i.lote, i.cantidad_stock, i.stock_minimo, i.fecha_vencimiento, i.precio_venta,
                   pt.nombre as tipo_producto
            FROM inventario i
            JOIN producto_tipos pt ON i.producto_tipo_id = pt.id
            WHERE i.nombre_producto LIKE ?
            ORDER BY i.nombre_producto
            """;

        List<Inventario> productos = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inventario producto = crearInventarioDesdeResultSet(rs);
                    productos.add(producto);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar productos: " + e.getMessage());
        }

        return productos;
    }

    public List<Inventario> obtenerProductosBajoStock() {
        String sql = """
            SELECT i.id, i.nombre_producto, i.producto_tipo_id, i.descripcion, i.fabricante,
                   i.lote, i.cantidad_stock, i.stock_minimo, i.fecha_vencimiento, i.precio_venta,
                   pt.nombre as tipo_producto
            FROM inventario i
            JOIN producto_tipos pt ON i.producto_tipo_id = pt.id
            WHERE i.cantidad_stock <= i.stock_minimo
            ORDER BY i.cantidad_stock ASC
            """;

        List<Inventario> productos = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Inventario producto = crearInventarioDesdeResultSet(rs);
                productos.add(producto);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener productos con bajo stock: " + e.getMessage());
        }

        return productos;
    }

    public List<Inventario> obtenerProductosProximosAVencer(int diasAlerta) {
        String sql = """
            SELECT i.id, i.nombre_producto, i.producto_tipo_id, i.descripcion, i.fabricante,
                   i.lote, i.cantidad_stock, i.stock_minimo, i.fecha_vencimiento, i.precio_venta,
                   pt.nombre as tipo_producto
            FROM inventario i
            JOIN producto_tipos pt ON i.producto_tipo_id = pt.id
            WHERE i.fecha_vencimiento IS NOT NULL
            AND i.fecha_vencimiento BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL ? DAY)
            ORDER BY i.fecha_vencimiento ASC
            """;

        List<Inventario> productos = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, diasAlerta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inventario producto = crearInventarioDesdeResultSet(rs);
                    productos.add(producto);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener productos próximos a vencer: " + e.getMessage());
        }

        return productos;
    }

    public List<ProductoTipo> listarTiposProducto() {
        String sql = "SELECT id, nombre FROM producto_tipos ORDER BY id";
        List<ProductoTipo> tipos = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ProductoTipo tipo = new ProductoTipo();
                tipo.setId(rs.getInt("id"));
                tipo.setNombre(rs.getString("nombre"));
                tipos.add(tipo);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar tipos de producto: " + e.getMessage());
        }

        return tipos;
    }

    private Inventario crearInventarioDesdeResultSet(ResultSet rs) throws SQLException {
        Inventario producto = new Inventario();
        producto.setId(rs.getInt("id"));
        producto.setNombreProducto(rs.getString("nombre_producto"));
        producto.setProductoTipoId(rs.getInt("producto_tipo_id"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setFabricante(rs.getString("fabricante"));
        producto.setLote(rs.getString("lote"));
        producto.setCantidadStock(rs.getInt("cantidad_stock"));
        producto.setStockMinimo(rs.getInt("stock_minimo"));

        Date fechaVenc = rs.getDate("fecha_vencimiento");
        if (fechaVenc != null) {
            producto.setFechaVencimiento(fechaVenc.toLocalDate());
        }

        producto.setPrecioVenta(rs.getBigDecimal("precio_venta"));
        producto.setTipoProducto(rs.getString("tipo_producto"));
        return producto;
    }
}