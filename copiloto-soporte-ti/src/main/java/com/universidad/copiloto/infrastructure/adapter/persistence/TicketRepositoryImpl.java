package com.universidad.copiloto.infrastructure.adapter.persistence;

import com.universidad.copiloto.domain.model.Ticket;
import com.universidad.copiloto.domain.port.TicketRepository;
import com.universidad.copiloto.infrastructure.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepository {

    @Override
    public Ticket guardar(Ticket ticket) {
        String sql = "INSERT INTO Ticket (IdUsuarioCreador, Titulo, Descripcion, Estado, Prioridad, FechaCreacion) " +
                     "VALUES (?, ?, ?, 'Abierto', 'Media', GETDATE())";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, ticket.getIdUsuarioCreador());
            stmt.setString(2, ticket.getTitulo());
            stmt.setString(3, ticket.getDescripcion());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        ticket.setIdTicket(rs.getInt(1));
                        ticket.setEstado("Abierto");
                        ticket.setFechaCreacion(LocalDateTime.now());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ ERROR SQL EN SERVIDOR: " + e.getMessage());
            throw new RuntimeException("Error al insertar ticket en la BD", e);
        }

        return ticket;
    }

    @Override
    public List<Ticket> obtenerTodos() {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT IdTicket, IdUsuarioCreador, Titulo, Descripcion, Estado, Prioridad, FechaCreacion FROM Ticket ORDER BY IdTicket DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ticket t = new Ticket();
                t.setIdTicket(rs.getInt("IdTicket"));
                t.setIdUsuarioCreador(rs.getInt("IdUsuarioCreador"));
                t.setTitulo(rs.getString("Titulo"));
                t.setDescripcion(rs.getString("Descripcion"));
                t.setEstado(rs.getString("Estado"));
                t.setPrioridad(rs.getString("Prioridad"));
                
                Timestamp ts = rs.getTimestamp("FechaCreacion");
                if (ts != null) {
                    t.setFechaCreacion(ts.toLocalDateTime());
                }
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("❌ ERROR AL CONSULTAR TICKETS: " + e.getMessage());
        }
        return lista;
    }
}