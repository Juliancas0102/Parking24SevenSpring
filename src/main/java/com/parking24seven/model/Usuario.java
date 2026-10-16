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
     * La contraseña se acepta al crear/actualizar, pero nunca se devuelve en JSON.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "passusu", nullable = false, length = 255)
    private String passusu;

    public Usuario() {
    }

    public Usuario(Integer id, String nomusu, String passusu) {
        this.id = id;
        this.nomusu = nomusu;
        this.passusu = passusu;
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
}
