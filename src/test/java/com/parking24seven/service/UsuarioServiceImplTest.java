package com.parking24seven.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parking24seven.exception.RecursoNoEncontradoException;
import com.parking24seven.model.Usuario;
import com.parking24seven.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Pruebas unitarias de la lógica de negocio del módulo de usuarios.
 */
class UsuarioServiceImplTest {

    private UsuarioRepository repository;
    private UsuarioServiceImpl service;

    @BeforeEach
    void configurar() {
        repository = Mockito.mock(UsuarioRepository.class);
        service = new UsuarioServiceImpl(repository);
    }

    @Test
    void crearDebeGuardarPasswordHasheado() {
        Usuario entrada = new Usuario(null, "operador01", "Clave123");
        when(repository.existsByNomusu("operador01")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenAnswer(invocacion -> {
            Usuario u = invocacion.getArgument(0);
            u.setId(10);
            return u;
        });

        Usuario creado = service.crear(entrada);

        assertEquals(10, creado.getId());
        assertEquals("operador01", creado.getNomusu());
        assertNotEquals("Clave123", creado.getPassusu());
        verify(repository).save(entrada);
    }

    @Test
    void buscarIdInexistenteDebeLanzarExcepcion() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.buscarPorId(99));
    }
}
