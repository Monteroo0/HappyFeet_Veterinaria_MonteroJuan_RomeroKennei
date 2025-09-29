package com.happyfeet.dao;

import com.happyfeet.model.Proveedor;
import com.happyfeet.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public void guardar(Proveedor proveedor) {
        String sql = "INSERT INTO proveedores (nombre, contacto, telefono, email, direccion) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getContacto());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getEmail());
            ps.setString(5, proveedor.getDireccion());

            ps.executeUpdate();
            System.out.println("✅ Proveedor registrado exitosamente");

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar proveedor: " + e.getMessage());
        }
    }

    public List<Proveedor> listar() {
        String sql = "SELECT id, nombre, contacto, telefono, email, direccion FROM proveedores ORDER BY nombre";
        List<Proveedor> proveedores = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setContacto(rs.getString("contacto"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setEmail(rs.getString("email"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedores.add(proveedor);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar proveedores: " + e.getMessage());
        }

        return proveedores;
    }

    public void actualizar(Proveedor proveedor) {
        String sql = "UPDATE proveedores SET nombre = ?, contacto = ?, telefono = ?, email = ?, direccion = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getContacto());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getEmail());
            ps.setString(5, proveedor.getDireccion());
            ps.setInt(6, proveedor.getId());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("✅ Proveedor actualizado exitosamente");
            } else {
                System.out.println("❌ No se encontró el proveedor con ID: " + proveedor.getId());
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar proveedor: " + e.getMessage());
        }
    }

    public List<Proveedor> buscarPorNombre(String nombre) {
        String sql = "SELECT id, nombre, contacto, telefono, email, direccion FROM proveedores WHERE nombre LIKE ? ORDER BY nombre";
        List<Proveedor> proveedores = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Proveedor proveedor = new Proveedor();
                    proveedor.setId(rs.getInt("id"));
                    proveedor.setNombre(rs.getString("nombre"));
                    proveedor.setContacto(rs.getString("contacto"));
                    proveedor.setTelefono(rs.getString("telefono"));
                    proveedor.setEmail(rs.getString("email"));
                    proveedor.setDireccion(rs.getString("direccion"));
                    proveedores.add(proveedor);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar proveedores: " + e.getMessage());
        }

        return proveedores;
    }
}