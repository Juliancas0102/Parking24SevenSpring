package com.parking24seven.repository;

import com.parking24seven.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para las operaciones de persistencia de usuarios.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByNomusu(String nomusu);
}
