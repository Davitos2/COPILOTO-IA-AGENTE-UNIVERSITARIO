package com.universidad.copiloto.infrastructure.adapter.controller;

import com.universidad.copiloto.application.usecase.RegistrarTicketUseCase;
import com.universidad.copiloto.domain.model.Ticket;
import com.universidad.copiloto.domain.port.TicketRepository;
import com.universidad.copiloto.infrastructure.adapter.persistence.TicketRepositoryImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    // Declaramos ambas variables como atributos de la clase para que todos los métodos las reconozcan
    private final TicketRepository ticketRepository;
    private final RegistrarTicketUseCase registrarTicketUseCase;

    public TicketController() {
        this.ticketRepository = new TicketRepositoryImpl();
        this.registrarTicketUseCase = new RegistrarTicketUseCase(this.ticketRepository);
    }

    @PostMapping
    public ResponseEntity<?> crearTicket(@RequestBody Ticket ticketRequest) {
        Ticket ticketCreado = registrarTicketUseCase.ejecutar(
            ticketRequest.getIdUsuarioCreador(),
            ticketRequest.getTitulo(),
            ticketRequest.getDescripcion()
        );

        if (ticketCreado.getIdTicket() == null) {
            return ResponseEntity.internalServerError()
                .body("❌ ERROR CRÍTICO: La conexión a SQL Server falló o la consulta no se ejecutó.");
        }

        return ResponseEntity.ok(ticketCreado);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> listarTickets() {
        List<Ticket> tickets = ticketRepository.obtenerTodos();
        return ResponseEntity.ok(tickets);
    }
}