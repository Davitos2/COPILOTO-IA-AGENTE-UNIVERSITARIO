package com.universidad.copiloto.infrastructure.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=CopilotoSoporteTI;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "admin_1"; 
    private static final String PASSWORD = "Admin_1596*"; 

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("==================================================");
            System.err.println("❌ ERROR FATAL DE CONEXIÓN A SQL SERVER ❌");
            System.err.println("MOTIVO: " + e.getMessage());
            System.err.println("ESTADO SQL: " + e.getSQLState());
            System.err.println("==================================================");
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }
}