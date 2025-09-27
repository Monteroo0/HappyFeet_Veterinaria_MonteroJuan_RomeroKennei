package com.happyfeet.dao;

import com.happyfeet.model.Mascota;
import com.happyfeet.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAO {

    public void guardar(Mascota mascota) {
        String sql = "INSERT INTO mascotas (dueno_id, nombre, raza_id, fecha_nacimiento, sexo, url_foto) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mascota.getDuenoId());
            ps.setString(2, mascota.getNombre());
            // ⚠️ Simplificación: por ahora no usamos raza_id real, dejamos NULL
            ps.setNull(3, Types.INTEGER);
            ps.setString(4, mascota.getFechaNacimiento());
            ps.setString(5, mascota.getSexo());
            ps.setString(6, mascota.getUrlFoto());
            ps.executeUpdate();
            System.out.println("✅ Mascota registrada correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Mascota> listar() {
        List<Mascota> lista = new ArrayList<>();
        String sql = "SELECT * FROM mascotas";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Mascota m = new Mascota(
                        rs.getInt("id"),
                        rs.getInt("dueno_id"),
                        rs.getString("nombre"),
                        rs.getString("raza_id"), // aquí raza_id es int, lo mapeamos como texto simplificado
                        rs.getString("fecha_nacimiento"),
                        rs.getString("sexo"),
                        rs.getString("url_foto")
                );
                lista.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
