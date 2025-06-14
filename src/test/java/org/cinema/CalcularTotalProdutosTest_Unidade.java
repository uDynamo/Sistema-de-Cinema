package org.cinema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalcularTotalProdutosTest_Unidade {
    private SistemaCinema sistemaCinema;

    @BeforeEach
    void setUp() {
        sistemaCinema = new SistemaCinema();
    }

    @Test
    void calcularTotalProdutosComZeroProdutos() {
        List<Produto> produtos = Collections.emptyList();
        double resultado = sistemaCinema.calcularTotalProdutos(produtos);

        assertEquals(0.0, resultado, 0.001);
    }

    @Test
    void calcularTotalProdutosCom2Produtos() {
        Produto mockPipoca = mock(Produto.class);
        Produto mockRefri = mock(Produto.class);

        when(mockPipoca.getPreco()).thenReturn(24.0);
        when(mockRefri.getPreco()).thenReturn(12.0);

        List<Produto> produtos = List.of(mockPipoca, mockRefri);
        double resultado = sistemaCinema.calcularTotalProdutos(produtos);

        assertEquals(36.0, resultado, 0.001);
    }
}
