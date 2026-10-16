package com.parking24seven.service;

import com.parking24seven.model.Usuario;
import java.util.List;

/**
 * Contrato de servicios del módulo de usuarios.
 */
public interface UsuarioService {

    List<Usuario> listarTodos();

    Usuario buscarPorId(Integer id);

    Usuario crear(Usuario usuario);

    Usuario actualizar(Integer id, Usuario usuario);

    void eliminar(Integer id);
}
