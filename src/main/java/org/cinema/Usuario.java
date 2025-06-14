package org.cinema;
import java.time.LocalDate;
import java.time.Period;

public class Usuario {
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private double saldo;

    public Usuario(String nome, String email, LocalDate dataNascimento, double saldo){
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.saldo = saldo;
    }

    public int getIdade(){
        return Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }

    public boolean temDescontoPorIdade(){
        int idade = getIdade();
        return idade < 18 || idade >= 60;
    }

    public void depositar(double valor){
        if(valor <= 0){
            throw new IllegalArgumentException("Valor deve ser maior que 0!");
        }
        this.saldo += valor;
    }

    public boolean saldoSuficiente(double valorIngresso){
        if(valorIngresso < 0){
            throw new IllegalArgumentException("Valor deve ser maior ou igual que 0!");
        }
        return this.saldo >= valorIngresso;
    }

    public boolean debitar(double valor){
        if(saldoSuficiente(valor)){
            this.saldo -= valor;
            return true;
        }
        System.out.println("Saldo insuficiente para realizar a aposta!");
        return false;
    }

    public String getNome(){
        return nome;
    }

    public String getEmail(){
        return email;
    }

    public double getSaldo(){
        return saldo;
    }
}