package org.cinema;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sessao {
    private Filme filme;
    private LocalDateTime dataHora;
    private boolean[] poltrona;
    private double precoIngressoBase = 36;
    private int capacidadeMaxima = 3;

    public Sessao(Filme filme, LocalDateTime dataHora){
        this.filme = filme;
        this.dataHora = dataHora;
        this.poltrona = new boolean[capacidadeMaxima]; // 0 = disponivel e 1 = ocupada          todos iniciam como 0
    }

    public boolean isPoltronaOcupada(int numero){
        if(numero < 1 || numero > capacidadeMaxima){
            throw new IllegalArgumentException("Número da Poltrona deve ser entre 1 e " + capacidadeMaxima);
        }
        return poltrona[numero - 1];
    }

    public boolean reservarPoltrona(int numero){
        // Verifica se o número da Poltrona é válido
        if(numero < 1 || numero > capacidadeMaxima){
            throw new IllegalArgumentException("Número da Poltrona deve ser entre 1 e " + capacidadeMaxima);
        }

        if(isPoltronaOcupada(numero)){
            return false; // Poltrona ocupada
        }

        poltrona[numero - 1] = true; // Marca como ocupado
        return true;
    }

    public boolean isDiaPromocional(){
        DayOfWeek dia = getDataHora().getDayOfWeek();
        return dia == DayOfWeek.MONDAY || dia == DayOfWeek.TUESDAY || dia == DayOfWeek.WEDNESDAY;
    }

    public List<Integer> getPoltronaDisponiveis(){
        List<Integer> disponiveis = new ArrayList<>();
        for(int i = 0; i < poltrona.length; i++){
            if(!poltrona[i]){ // Se false, está disponível
                disponiveis.add(i + 1); // Adiciona o número da poltrona (numero + 1)
            }
        }
        return disponiveis;
    }

    public int getNumPoltronasOcupadas(){
        int contador = 0;
        for(boolean ocupado : poltrona){
            if(ocupado) contador++;
        }
        return contador;
    }

    public Filme getFilme(){
        return filme;
    }

    public LocalDateTime getDataHora(){
        return dataHora;
    }

    public double getPrecoIngressoBase(){
        return precoIngressoBase;
    }

    public int getCapacidadeMaxima(){
        return capacidadeMaxima;
    }

    @Override
    public String toString(){
        return filme.getTitulo() + " - " + dataHora.toLocalDate() + " às " + dataHora.toLocalTime();
    }
}
