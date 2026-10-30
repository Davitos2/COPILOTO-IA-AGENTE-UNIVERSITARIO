package com.universidad.copiloto.domain.port;

import com.universidad.copiloto.domain.model.Ticket;
import java.util.List;

public interface TicketRepository {
    Ticket guardar(Ticket ticket);
    List<Ticket> obtenerTodos();
}