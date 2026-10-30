package com.universidad.copiloto.infrastructure.adapter.rest;

import com.universidad.copiloto.infrastructure.adapter.rest.dto.LoginRequest;
import com.universidad.copiloto.infrastructure.adapter.rest.dto.UsuarioDTO;
import com.universidad.copiloto.infrastructure.config.DatabaseConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Consulta adaptada exactamente a tu tabla dbo.Usuario
        String sql = "SELECT IdUsuario, IdRol, Nombre, Correo FROM dbo.Usuario WHERE Correo = ? AND ContrasenaHash = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, request.getCorreo());
            stmt.setString(2, request.getContrasena());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UsuarioDTO user = new UsuarioDTO(
                        rs.getInt("IdUsuario"),
                        rs.getInt("IdRol"),
                        rs.getString("Nombre"),
                        rs.getString("Correo")
                    );
                    return ResponseEntity.ok(user);
                } else {
                    return ResponseEntity.status(401).body("Correo o contraseña incorrectos");
                }
            }
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Error en BD: " + e.getMessage());
        }
    }
}