package com.universidad.copiloto.infrastructure.adapter.rest.dto;

public class UsuarioDTO {
    private Integer idUsuario;
    private Integer idRol;
    private String nombre;
    private String correo;

    public UsuarioDTO(Integer idUsuario, Integer idRol, String nombre, String correo) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.nombre = nombre;
        this.correo = correo;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public Integer getIdRol() { return idRol; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
}