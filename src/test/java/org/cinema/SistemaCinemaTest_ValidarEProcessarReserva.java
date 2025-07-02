package org.cinema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SistemaCinemaTest_ValidarEProcessarReserva {
    private SistemaCinema sistema;
    private Usuario usuarioAdulto;
    private Usuario usuarioAdolescente;
    private Filme filmeLivre;
    private Filme filmeAdulto;
    private Sessao sessaoEmDiaPromocional;
    private Sessao sessaoEmDiaNormal;
    private LocalDateTime dataNormal;
    private LocalDateTime dataPromocional;

    @BeforeEach
    void setUp() {
        sistema = new SistemaCinema();

        usuarioAdulto = new Usuario("Andre", "andre@mail.com", LocalDate.now().minusYears(30), 100.0);
        usuarioAdolescente = new Usuario("Joao", "joao@mail.com", LocalDate.now().minusYears(15), 100.0);
        sistema.cadastrarUsuario(usuarioAdulto);
        sistema.cadastrarUsuario(usuarioAdolescente);

        filmeLivre = new Filme("Homem-Aranha", 0);
        filmeAdulto = new Filme("Premonićão", 18);
        sistema.cadastrarFilme(filmeLivre);
        sistema.cadastrarFilme(filmeAdulto);

        dataPromocional = LocalDateTime.of(2025, 7, 7, 20, 0);
        dataNormal = LocalDateTime.of(2025, 7, 11, 20, 0);

        sistema.agendarSessao(sessaoEmDiaPromocional);
        sistema.agendarSessao(sessaoEmDiaNormal);
    }

    @Test
    void testValidarEProcessarReservaSemIdadeSuficiente() {
        sessaoEmDiaNormal = new Sessao(filmeAdulto, dataNormal);
        List<Produto> produtos = new ArrayList<>();
        boolean resultado = sistema.validarEProcessarReserva(usuarioAdolescente, sessaoEmDiaNormal, 1, produtos);

        assertFalse(resultado);
        assertEquals(100.0, usuarioAdolescente.getSaldo());
    }

    @Test
    void testValidarEProcessarReservaEmPoltronaJaOcupada() {
        sessaoEmDiaNormal = new Sessao(filmeAdulto, dataNormal);
        sessaoEmDiaNormal.reservarPoltrona(1); // Ocupar a poltrona antes de fazer a reserva para produzir a falha
        List<Produto> produtos = new ArrayList<>();
        boolean resultado = sistema.validarEProcessarReserva(usuarioAdulto, sessaoEmDiaNormal, 1, produtos); // Reservar a mesma poltrona que foi ocupada anteriormente

        assertFalse(resultado);
        assertEquals(100.0, usuarioAdulto.getSaldo());
    }

    @Test
    void testValidarEProcessarReservaComSaldoInsuficiente() {
        sessaoEmDiaNormal = new Sessao(filmeAdulto, dataNormal);
        usuarioAdulto.debitar(100); // Usuário vai ficar com 0 de saldo
        List<Produto> produtos = new ArrayList<>(); // Ingresso custa 36 reais
        boolean resultado = sistema.validarEProcessarReserva(usuarioAdulto, sessaoEmDiaNormal, 1, produtos);

        assertFalse(resultado);
        assertEquals(0, usuarioAdulto.getSaldo());
    }

    @Test
    void testValidarEProcessarReservaComSucessoSemDescontos() {
        sessaoEmDiaNormal = new Sessao(filmeAdulto, dataNormal);
        List<Produto> produtos = new ArrayList<>();
        boolean resultado = sistema.validarEProcessarReserva(usuarioAdulto, sessaoEmDiaNormal, 1, produtos);

        assertTrue(resultado);
        assertTrue(sessaoEmDiaNormal.isPoltronaOcupada(1));
        assertEquals(64.0, usuarioAdulto.getSaldo());
    }

    @Test
    void testValidarEProcessarReservaComSucessoComDescontoDeIdade() {
        sessaoEmDiaNormal = new Sessao(filmeLivre, dataNormal);
        List<Produto> produtos = new ArrayList<>();
        boolean resultado = sistema.validarEProcessarReserva(usuarioAdolescente, sessaoEmDiaNormal, 1, produtos);

        assertTrue(resultado);
        assertTrue(sessaoEmDiaNormal.isPoltronaOcupada(1));
        assertEquals(82, usuarioAdolescente.getSaldo(), 0.01);
    }

    @Test
    void validarEProcessarReservaComSucessoComDescontoDeDiaPromocional() {
        sessaoEmDiaPromocional = new Sessao(filmeAdulto, dataPromocional);
        List<Produto> produtos = new ArrayList<>();
        boolean resultado = sistema.validarEProcessarReserva(usuarioAdulto, sessaoEmDiaPromocional, 1, produtos);

        assertTrue(resultado);
        assertTrue(sessaoEmDiaPromocional.isPoltronaOcupada(1));
        assertEquals(74.80, usuarioAdulto.getSaldo(), 0.01);
    }

    @Test
    void testValidarEProcessarReservaComSucessoComDescontoDeIdadeEDiaPromocional() {
        sessaoEmDiaPromocional = new Sessao(filmeLivre, dataPromocional);
        List<Produto> produtos = new ArrayList<>();
        boolean resultado = sistema.validarEProcessarReserva(usuarioAdolescente, sessaoEmDiaPromocional, 1, produtos);

        assertTrue(resultado);
        assertTrue(sessaoEmDiaPromocional.isPoltronaOcupada(1));
        assertEquals(87.40, usuarioAdolescente.getSaldo(), 0.01);
    }
}