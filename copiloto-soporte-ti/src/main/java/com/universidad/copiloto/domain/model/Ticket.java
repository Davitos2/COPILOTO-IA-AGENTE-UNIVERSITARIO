package com.universidad.copiloto.domain.model;

import java.time.LocalDateTime;

public class Ticket {
    private Integer idTicket;
    private Integer idUsuarioCreador;
    private Integer idCategoria;
    private Integer idAnalistaAsignado;
    private String titulo;
    private String descripcion;
    private String estado;
    private String prioridad;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaResolucion;

    public Ticket() {}

    // Getters y Setters
    public Integer getIdTicket() { return idTicket; }
    public void setIdTicket(Integer idTicket) { this.idTicket = idTicket; }

    public Integer getIdUsuarioCreador() { return idUsuarioCreador; }
    public void setIdUsuarioCreador(Integer idUsuarioCreador) { this.idUsuarioCreador = idUsuarioCreador; }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    public Integer getIdAnalistaAsignado() { return idAnalistaAsignado; }
    public void setIdAnalistaAsignado(Integer idAnalistaAsignado) { this.idAnalistaAsignado = idAnalistaAsignado; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }
}