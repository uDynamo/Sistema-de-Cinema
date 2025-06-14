package org.cinema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    private Usuario usuario;

    @BeforeEach
    void setUp(){
        usuario = new Usuario("Bruno", "bruno@mail.com", LocalDate.of(2000, 01, 01), 50);
    }

    @Test
    public void testDepositarComValorNegativo(){
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.depositar(-1);
        });
    }

    @Test
    public void testDepositarComValorZero(){
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.depositar(-0);
        });
    }

    @Test
    public void testDepositarComValorPositivo(){
        usuario.depositar(100);
        assertEquals(150, usuario.getSaldo());
    }

    @Test
    public void testSaldoSuficiente() {
        boolean resultado = usuario.saldoSuficiente(50);
        assertTrue(resultado);
    }

    @Test
    public void testSaldoInsuficiente(){
        boolean resultado = usuario.saldoSuficiente(51);
        assertFalse(resultado);
    }

    @Test
    public void testSaldoSuficienteComValorNegativo(){
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.saldoSuficiente(-1);
        });
    }

    @Test
    public void testDebitarComSaldoSuficiente(){
        boolean resultado = usuario.debitar(50);
        assertTrue(resultado);
        assertEquals(0, usuario.getSaldo());
    }

    @Test
    public void testDebitarComSaldoInsuficiente(){
        boolean resultado = usuario.debitar(51);
        assertFalse(resultado);
        assertEquals(50, usuario.getSaldo());
    }

    @Test
    public void testDebitarComValorNegativo(){
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.debitar(-1);
        });
    }

    @Test
    public void testNaoTemDescontoPorIdade(){
        boolean resultado = usuario.temDescontoPorIdade();
        assertFalse(resultado);
    }

    @Test
    public void testTemDescontoPorIdadePorSerMenorDe18(){
        Usuario usuario2 = new Usuario("Daniel", "daniel@mail.com", LocalDate.of(2010, 01, 01), 50);
        boolean resultado = usuario2.temDescontoPorIdade();

        assertTrue(resultado);
    }

    @Test
    public void testTemDescontoPorIdadePorSerIdoso(){
        Usuario usuario2 = new Usuario("Daniel", "daniel@mail.com", LocalDate.of(1950, 01, 01), 50);
        boolean resultado = usuario2.temDescontoPorIdade();

        assertTrue(resultado);
    }
}