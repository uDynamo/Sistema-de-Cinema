package org.cinema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CadastrarUsuarioTest_Unidade {
    private SistemaCinema sistemaCinema;
    private Usuario usuarioMock1;
    private Usuario usuarioMock2;

    @BeforeEach
    void setUp() {
        sistemaCinema = new SistemaCinema();
        usuarioMock1 = mock(Usuario.class);
        usuarioMock2 = mock(Usuario.class);

        when(usuarioMock1.getEmail()).thenReturn("bruno@gmail.com");
        when(usuarioMock2.getEmail()).thenReturn("joao@gmail.com");
    }

    @Test
    public void testCadastrarUsuarioComEmailUnico(){
        boolean resultado = sistemaCinema.cadastrarUsuario(usuarioMock1);

        assertTrue(resultado);
        assertTrue(sistemaCinema.getUsuarios().contains(usuarioMock1));
        assertEquals(1, sistemaCinema.getUsuarios().size());
    }

    @Test
    public void testCadastrarUsuarioComEmailExistente(){
        Usuario usuarioMock3 = mock(Usuario.class);
        when(usuarioMock3.getEmail()).thenReturn("bruno@gmail.com");

        sistemaCinema.cadastrarUsuario(usuarioMock1); //cadastrar o bruno@gmail.com
        boolean resultado = sistemaCinema.cadastrarUsuario(usuarioMock3);

        assertFalse(resultado);
        assertFalse(sistemaCinema.getUsuarios().contains(usuarioMock3));
        assertEquals(1, sistemaCinema.getUsuarios().size());
    }

    @Test
    public void testCadastrarMaisDeUmUsuario(){
        sistemaCinema.cadastrarUsuario(usuarioMock1); //cadastrar o bruno@gmail.com
        sistemaCinema.cadastrarUsuario(usuarioMock2); //cadastrar o joao@gmail.com

        assertTrue(sistemaCinema.getUsuarios().contains(usuarioMock1));
        assertTrue(sistemaCinema.getUsuarios().contains(usuarioMock2));
        assertEquals(2, sistemaCinema.getUsuarios().size());
    }
}