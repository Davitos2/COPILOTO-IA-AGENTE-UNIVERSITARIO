package com.universidad.copiloto.application.usecase;

import com.universidad.copiloto.domain.model.Ticket;
import com.universidad.copiloto.domain.port.TicketRepository;

public class RegistrarTicketUseCase {

    private final TicketRepository ticketRepository;

    public RegistrarTicketUseCase(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket ejecutar(Integer idUsuario, String titulo, String descripcion) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título del ticket no puede estar vacío");
        }

        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setIdUsuarioCreador(idUsuario);
        nuevoTicket.setTitulo(titulo);
        nuevoTicket.setDescripcion(descripcion);

        return ticketRepository.guardar(nuevoTicket);
    }
}