package com.parking24seven.service;

import com.parking24seven.exception.RecursoNoEncontradoException;
import com.parking24seven.model.Usuario;
import com.parking24seven.repository.UsuarioRepository;
import com.parking24seven.util.PasswordUtil;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de la lógica de negocio del módulo de usuarios.
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private static final Set<String> ROLES_VALIDOS =
            Set.of(
                    "ADMINISTRADOR",
                    "OPERADOR",
                    "CONTADOR",
                    "CLIENTE"
            );

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
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe un usuario con id " + id + "."
                        )
                );
    }

    @Override
    public Usuario crear(Usuario usuario) {

        validarUsuario(usuario, true);

        String nombre = usuario.getNomusu().trim();

        if (usuarioRepository.existsByNomusu(nombre)) {
            throw new IllegalArgumentException(
                    "El nombre de usuario ya está registrado."
            );
        }

        usuario.setId(null);
        usuario.setNomusu(nombre);

        usuario.setPassusu(
                PasswordUtil.hash(usuario.getPassusu())
        );

        usuario.setRol(
                normalizarRol(usuario.getRol())
        );

        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Integer id, Usuario datos) {

        Usuario actual = buscarPorId(id);

        validarUsuario(datos, false);

        String nuevoNombre = datos.getNomusu().trim();

        if (!actual.getNomusu().equalsIgnoreCase(nuevoNombre)
                && usuarioRepository.existsByNomusu(nuevoNombre)) {

            throw new IllegalArgumentException(
                    "El nombre de usuario ya está registrado."
            );
        }

        actual.setNomusu(nuevoNombre);

        if (datos.getPassusu() != null
                && !datos.getPassusu().isBlank()) {

            actual.setPassusu(
                    PasswordUtil.hash(datos.getPassusu())
            );
        }

        if (datos.getRol() != null
                && !datos.getRol().isBlank()) {

            actual.setRol(
                    normalizarRol(datos.getRol())
            );
        }

        return usuarioRepository.save(actual);
    }

    @Override
    public Usuario desactivar(Integer id) {

        Usuario usuario = buscarPorId(id);

        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new IllegalArgumentException(
                    "El usuario ya se encuentra inactivo."
            );
        }

        usuario.setActivo(false);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario autenticar(
            String nomusu,
            String passusu) {

        if (nomusu == null
                || nomusu.isBlank()
                || passusu == null
                || passusu.isBlank()) {

            throw new IllegalArgumentException(
                    "El usuario y la contraseña son obligatorios."
            );
        }

        Usuario usuario =
                usuarioRepository
                        .findByNomusu(nomusu.trim())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Usuario o contraseña incorrectos."
                                )
                        );

        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new IllegalArgumentException(
                    "El usuario se encuentra inactivo."
            );
        }

        if (!PasswordUtil.matches(
                passusu,
                usuario.getPassusu())) {

            throw new IllegalArgumentException(
                    "Usuario o contraseña incorrectos."
            );
        }

        return usuario;
    }

    private void validarUsuario(
            Usuario usuario,
            boolean requierePassword) {

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "Los datos del usuario son obligatorios."
            );
        }

        if (usuario.getNomusu() == null
                || usuario.getNomusu().isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre de usuario es obligatorio."
            );
        }

        String nombre = usuario.getNomusu().trim();

        if (nombre.length() < 3) {
            throw new IllegalArgumentException(
                    "El nombre de usuario debe tener mínimo 3 caracteres."
            );
        }

        if (nombre.length() > 50) {
            throw new IllegalArgumentException(
                    "El nombre de usuario no puede superar 50 caracteres."
            );
        }

        if (!nombre.matches("[A-Za-z0-9._-]+")) {
            throw new IllegalArgumentException(
                    "El nombre de usuario solo puede contener letras, números, punto, guion y guion bajo."
            );
        }

        if (requierePassword) {

            if (usuario.getPassusu() == null
                    || usuario.getPassusu().isBlank()) {

                throw new IllegalArgumentException(
                        "La contraseña es obligatoria."
                );
            }

            if (usuario.getPassusu().length() < 8) {
                throw new IllegalArgumentException(
                        "La contraseña debe tener mínimo 8 caracteres."
                );
            }
        }

        if (usuario.getRol() != null
                && !usuario.getRol().isBlank()) {

            normalizarRol(usuario.getRol());
        }
    }

    private String normalizarRol(String rol) {

        if (rol == null || rol.isBlank()) {
            return "OPERADOR";
        }

        String rolNormalizado =
                rol.trim().toUpperCase();

        if (!ROLES_VALIDOS.contains(rolNormalizado)) {
            throw new IllegalArgumentException(
                    "Rol no válido. Use ADMINISTRADOR, OPERADOR, CONTADOR o CLIENTE."
            );
        }

        return rolNormalizado;
    }
}