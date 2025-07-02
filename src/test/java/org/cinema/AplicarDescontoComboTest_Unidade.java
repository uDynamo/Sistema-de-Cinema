package org.cinema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AplicarDescontoComboTest_Unidade {
    private SistemaCinema sistemaCinema;
    private Produto mockPipoca;
    private Produto mockRefrigerante;


    @BeforeEach
    void setUp() {
        sistemaCinema = new SistemaCinema();

        mockPipoca = mock(Produto.class);
        when(mockPipoca.getNome()).thenReturn("Pipoca");

        mockRefrigerante = mock(Produto.class);
        when(mockRefrigerante.getNome()).thenReturn("Refrigerante");
    }

    @Test
    void testAplicarDescontoPipocaSozinha(){
        double precoTotal = 60.00;
        List<Produto> produtos = List.of(mockPipoca);
        precoTotal = sistemaCinema.aplicarDescontoCombo(precoTotal, produtos);

        assertEquals(54.00, precoTotal, 0.001);
    }

    @Test
    void testAplicarDescontoRefrigeranteSozinho(){
        double precoTotal = 48.00;
        List<Produto> produtos = List.of(mockPipoca);
        precoTotal = sistemaCinema.aplicarDescontoCombo(precoTotal, produtos);

        assertEquals(43.20, precoTotal, 0.001);
    }

    @Test
    void testAplicarDescontoComboRefrigeranteEPipoca(){
        double precoTotal = 72.00;
        List<Produto> produtos = List.of(mockPipoca, mockRefrigerante);
        precoTotal = sistemaCinema.aplicarDescontoCombo(precoTotal, produtos);

        assertEquals(57.60, precoTotal, 0.001);
    }

    @Test
    void testNaoAplicarDesconto(){
        double precoTotal = 36.00;
        List<Produto> produtos = Collections.emptyList();
        precoTotal = sistemaCinema.aplicarDescontoCombo(precoTotal, produtos);

        assertEquals(36, precoTotal, 0.001);
    }
}
