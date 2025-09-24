package com.happyfeet;

import com.happyfeet.util.DBConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Conexión establecida con MySQL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
