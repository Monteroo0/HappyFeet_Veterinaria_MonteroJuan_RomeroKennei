package com.happyfeet.dao;

import com.happyfeet.model.Dueno;
import com.happyfeet.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DuenoDAO {

    public void guardar(Dueno dueno) {
        String sql = "INSERT INTO duenos (nombre_completo, documento_identidad, direccion, telefono, email) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dueno.getNombreCompleto());
            ps.setString(2, dueno.getDocumentoIdentidad());
            ps.setString(3, dueno.getDireccion());
            ps.setString(4, dueno.getTelefono());
            ps.setString(5, dueno.getEmail());
            ps.executeUpdate();
            System.out.println("✅ Dueño registrado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Dueno> listar() {
        List<Dueno> lista = new ArrayList<>();
        String sql = "SELECT * FROM duenos";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Dueno d = new Dueno(
                        rs.getInt("id"),
                        rs.getString("nombre_completo"),
                        rs.getString("documento_identidad"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                lista.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
