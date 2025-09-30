package com.happyfeet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Patrón Singleton para la gestión de conexiones a la base de datos
 * Asegura una única instancia de configuración en toda la aplicación
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/happyfeet";
    private static final String USER = "root";
    private static final String PASSWORD = "campus2023";

    // Instancia única del Singleton
    private static DBConnection instance;

    // Constructor privado para evitar instanciación externa
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC no encontrado: " + e.getMessage());
            throw new RuntimeException("Fallo al cargar el driver", e);
        }
    }

    /**
     * Método estático para obtener la única instancia (Patrón Singleton)
     */
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /**
     * Obtiene una nueva conexión a la base de datos
     */
    public Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Método legacy para compatibilidad con código existente
     */
    public static Connection getConnection_Legacy() throws SQLException {
        return getInstance().getConnection();
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}