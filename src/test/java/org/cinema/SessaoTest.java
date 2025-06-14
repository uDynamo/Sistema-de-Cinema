package org.cinema;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SessaoTest {
    private Sessao sessao;
    private Filme filmeMock;

    @BeforeEach
    public void setUp(){
        filmeMock = mock(Filme.class);
        sessao = new Sessao(filmeMock, LocalDateTime.of(2025, 7, 30, 20, 30 ));
    }

    @Test
    public void testReservarPoltronaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            sessao.reservarPoltrona(4);
        });
    }

    @Test
    public void testReservarPoltronaLivre(){
        boolean resultado = sessao.reservarPoltrona(3);
        assertTrue(resultado);
    }

    @Test
    public void testReservarPoltronaOcupada(){
        sessao.reservarPoltrona(3);
        boolean resultado = sessao.reservarPoltrona(3);

        assertFalse(resultado);
    }

    @Test
    public void testNumPoltronasOcupadas(){
        sessao.reservarPoltrona(1);
        sessao.reservarPoltrona(2);
        sessao.reservarPoltrona(3);

        assertEquals(3, sessao.getNumPoltronasOcupadas());
    }

    @Test
    public void testGetPoltronasDisponiveis(){
        sessao.reservarPoltrona(2);
        List<Integer> disponiveis = sessao.getPoltronaDisponiveis();

        assertEquals(List.of(1, 3), disponiveis);
    }

    @Test
    public void testDiaPromocionalParaQuartaFeira(){
        boolean resultado = sessao.isDiaPromocional();
        assertTrue(resultado);
    }

    @Test
    public void testNaoEDiaPromocionalParaQuintaFeira(){
        Sessao sessao2 = new Sessao(filmeMock, LocalDateTime.of(2025, 7, 31, 20, 30 ));
        boolean resultado = sessao2.isDiaPromocional();

        assertFalse(resultado);
    }

}