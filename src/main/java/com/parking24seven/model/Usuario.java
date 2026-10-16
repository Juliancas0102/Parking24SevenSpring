package com.parking24seven.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad que representa un usuario almacenado en la tabla usuarios.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nomusu", nullable = false, length = 50, unique = true)
    private String nomusu;

    /**
     * La contraseña se acepta al crear/actualizar,
     * pero nunca se devuelve en las respuestas JSON.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "passusu", nullable = false, length = 255)
    private String passusu;

    /**
     * Rol asignado al usuario dentro del sistema.
     * Ejemplos: ADMINISTRADOR, OPERADOR.
     */
    @Column(
        name = "rol",
        nullable = false,
        length = 30,
        columnDefinition = "varchar(30) default 'OPERADOR'"
    )
    private String rol = "OPERADOR";

    /**
     * Indica si el usuario puede acceder al sistema.
     * La desactivación conserva el registro y su historial.
     */
    @Column(
        name = "activo",
        nullable = false,
        columnDefinition = "boolean default true"
    )
    private Boolean activo = true;

    public Usuario() {
    }

    // Se conserva para no afectar el código y pruebas existentes.
    public Usuario(Integer id, String nomusu, String passusu) {
        this.id = id;
        this.nomusu = nomusu;
        this.passusu = passusu;
        this.rol = "OPERADOR";
        this.activo = true;
    }

    public Usuario(
            Integer id,
            String nomusu,
            String passusu,
            String rol,
            Boolean activo) {

        this.id = id;
        this.nomusu = nomusu;
        this.passusu = passusu;
        this.rol = rol;
        this.activo = activo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomusu() {
        return nomusu;
    }

    public void setNomusu(String nomusu) {
        this.nomusu = nomusu;
    }

    public String getPassusu() {
        return passusu;
    }

    public void setPassusu(String passusu) {
        this.passusu = passusu;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}