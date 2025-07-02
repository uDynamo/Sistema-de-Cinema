package org.cinema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SistemaCinemaTest_CadastroEBuscaDeUsuario {
    private SistemaCinema sistemaCinema;

    @BeforeEach
    void setUp() {
        sistemaCinema = new SistemaCinema();
    }

    @Test
    void testCadastrarUsuarioComSucesso() {
        Usuario usuario1 = new Usuario("Joao Antunes", "joao@mail.com", LocalDate.of(2000, 3, 10), 0);

        assertTrue(sistemaCinema.cadastrarUsuario(usuario1));
        assertEquals(1, sistemaCinema.getUsuarios().size());
        assertEquals(usuario1, sistemaCinema.buscarUsuarioPorEmail("joao@mail.com"));
    }

    @Test
    void testCadastrarUsuarioComEmailJaUsado() {
        Usuario usuario1 = new Usuario("Joao Antunes", "joao@mail.com", LocalDate.of(2000, 3, 10), 0);
        Usuario usuario2 = new Usuario("Joao Santos", "joao@mail.com", LocalDate.of(2000, 6, 10), 0);
        sistemaCinema.cadastrarUsuario(usuario1);

        assertFalse(sistemaCinema.cadastrarUsuario(usuario2));
        assertEquals(1, sistemaCinema.getUsuarios().size());
        assertEquals(usuario1, sistemaCinema.buscarUsuarioPorEmail("joao@mail.com"));
    }

    @Test
    void testCadastrarMaisDeUmUsuarioComSucesso() {
        Usuario usuario1 = new Usuario("Joao Antunes", "joao@mail.com", LocalDate.of(2000, 3, 10), 0);
        Usuario usuario2 = new Usuario("Joao Santos", "joao2@mail.com", LocalDate.of(2000, 6, 10), 0);
        sistemaCinema.cadastrarUsuario(usuario1);
        sistemaCinema.cadastrarUsuario(usuario2);

        assertEquals(2, sistemaCinema.getUsuarios().size());
        assertEquals(usuario1, sistemaCinema.buscarUsuarioPorEmail("joao@mail.com"));
        assertEquals(usuario2, sistemaCinema.buscarUsuarioPorEmail("joao2@mail.com"));
    }

    @Test
    void testBuscarUsuarioPorEmailExistente() {
        Usuario usuario1 = new Usuario("Joao Antunes", "joao@mail.com", LocalDate.of(2000, 3, 10), 0);
        sistemaCinema.cadastrarUsuario(usuario1);
        Usuario usuarioBuscado = sistemaCinema.buscarUsuarioPorEmail("joao@mail.com");

        assertEquals("Joao Antunes", usuarioBuscado.getNome());
    }

    @Test
    void testBuscarUsuarioPorEmailInexistente() {
        Usuario usuarioBuscado = sistemaCinema.buscarUsuarioPorEmail("joao@mail.com");

        assertNull(usuarioBuscado);
    }
}
