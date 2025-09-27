package com.happyfeet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/happyfeet";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private DBConnection() {
        // Constructor privado para evitar instanciación
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Carga el driver
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a happyfeet!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC no encontrado: " + e.getMessage());
            throw new SQLException("Fallo al cargar el driver", e);
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
            throw e;
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar: " + e.getMessage());
            }
        }
    }
}