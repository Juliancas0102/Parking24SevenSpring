package com.parking24seven.service;

import com.parking24seven.exception.RecursoNoEncontradoException;
import com.parking24seven.model.Usuario;
import com.parking24seven.repository.UsuarioRepository;
import com.parking24seven.util.PasswordUtil;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de la lógica de negocio del módulo de usuarios.
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un usuario con id " + id + "."));
    }

    @Override
    public Usuario crear(Usuario usuario) {
        validarUsuario(usuario, true);

        if (usuarioRepository.existsByNomusu(usuario.getNomusu().trim())) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado.");
        }

        usuario.setId(null);
        usuario.setNomusu(usuario.getNomusu().trim());
        usuario.setPassusu(PasswordUtil.hash(usuario.getPassusu()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Integer id, Usuario datos) {
        Usuario actual = buscarPorId(id);
        validarUsuario(datos, false);

        String nuevoNombre = datos.getNomusu().trim();
        if (!actual.getNomusu().equalsIgnoreCase(nuevoNombre)
                && usuarioRepository.existsByNomusu(nuevoNombre)) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado.");
        }

        actual.setNomusu(nuevoNombre);
        if (datos.getPassusu() != null && !datos.getPassusu().isBlank()) {
            actual.setPassusu(PasswordUtil.hash(datos.getPassusu()));
        }

        return usuarioRepository.save(actual);
    }

    @Override
    public void eliminar(Integer id) {
        Usuario actual = buscarPorId(id);
        usuarioRepository.delete(actual);
    }

    private void validarUsuario(Usuario usuario, boolean requierePassword) {
        if (usuario == null) {
            throw new IllegalArgumentException("Los datos del usuario son obligatorios.");
        }
        if (usuario.getNomusu() == null || usuario.getNomusu().isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (usuario.getNomusu().trim().length() > 50) {
            throw new IllegalArgumentException("El nombre de usuario no puede superar 50 caracteres.");
        }
        if (requierePassword && (usuario.getPassusu() == null || usuario.getPassusu().isBlank())) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
    }
}
