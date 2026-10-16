package com.parking24seven.controller;

import com.parking24seven.model.Usuario;
import com.parking24seven.service.UsuarioService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST del módulo de usuarios de Parking 24 Seven.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todos los usuarios registrados.
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {

        return ResponseEntity.ok(
                usuarioService.listarTodos()
        );
    }

    /**
     * Consulta un usuario por su identificador.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                usuarioService.buscarPorId(id)
        );
    }

    /**
     * Crea un nuevo usuario.
     */
    @PostMapping
    public ResponseEntity<Usuario> crear(
            @RequestBody Usuario usuario) {

        Usuario creado =
                usuarioService.crear(usuario);

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/usuarios/"
                                + creado.getId()
                        )
                )
                .body(creado);
    }

    /**
     * Actualiza los datos y el rol
     * de un usuario existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Integer id,
            @RequestBody Usuario usuario) {

        return ResponseEntity.ok(
                usuarioService.actualizar(
                        id,
                        usuario
                )
        );
    }

    /**
     * Desactiva un usuario sin eliminarlo
     * físicamente de la base de datos.
     */
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Usuario> desactivar(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                usuarioService.desactivar(id)
        );
    }

    /**
     * Valida las credenciales de acceso.
     */
    @PostMapping("/login")
    public ResponseEntity<Usuario> autenticar(
            @RequestBody Map<String, String> credenciales) {

        String nomusu =
                credenciales.get("nomusu");

        String passusu =
                credenciales.get("passusu");

        Usuario usuario =
                usuarioService.autenticar(
                        nomusu,
                        passusu
                );

        return ResponseEntity.ok(usuario);
    }
}