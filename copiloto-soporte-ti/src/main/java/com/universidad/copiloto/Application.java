package com.universidad.copiloto;

import com.universidad.copiloto.infrastructure.config.DatabaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        // PRUEBA DIRECTA DE CONEXIÓN AL ARRANCAR
        System.out.println("=========================================");
        System.out.println("⏳ PROBANDO CONEXIÓN A SQL SERVER...");
        try (Connection conn = DatabaseConfig.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ ¡CONECTADO CON ÉXITO A SQL SERVER!");
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR EXACTO DE CONEXIÓN:");
            e.printStackTrace(); // Esto imprimirá el error real (ej. credenciales, puerto, etc.)
        }
        System.out.println("=========================================");
    }
}