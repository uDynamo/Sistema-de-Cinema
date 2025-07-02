package org.cinema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CinemaController {
    private SistemaCinema sistema;
    private Scanner scanner;

    public CinemaController(SistemaCinema sistema, Scanner scanner) {
        this.sistema = sistema;
        this.scanner = scanner;
    }

    public Usuario registrarNovoUsuario() {
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();
        System.out.print("Digite seu e-mail: ");
        String email = scanner.nextLine();

        LocalDate dataNascimento = null;
        while (dataNascimento == null) {
            System.out.print("Digite sua data de nascimento (DD/MM/AAAA): ");
            String dataStr = scanner.nextLine();
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataNascimento = LocalDate.parse(dataStr, formatter);
            } catch (Exception e) {
                System.out.println("ERRO: Formato de data inválido. Por favor, use DD/MM/AAAA.");
            }
        }

        Usuario novoUsuario = new Usuario(nome, email, dataNascimento, 0.0);
        if (sistema.cadastrarUsuario(novoUsuario)) {
            System.out.println("Cadastro realizado com sucesso!");
            return novoUsuario;
        } else {
            return null;
        }
    }

    public void exibirMenu(Usuario usuario) {
        System.out.println("\nMenu Principal:");
        System.out.printf("Saldo atual: R$ %.2f%n", usuario.getSaldo());
        System.out.println("1. Listar sessões em catálogo");
        System.out.println("2. Listar produtos disponíveis");
        System.out.println("3. Depositar valor na conta");
        System.out.println("4. Fazer uma reserva");
        System.out.println("0. Sair");
        System.out.println("----------------------");
    }

    public void realizarDeposito(Usuario usuario) {
        System.out.print("\nDigite o valor a ser depositado: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();
        try {
            usuario.depositar(valor);
            System.out.printf("Depósito realizado com sucesso! Novo saldo: R$ %.2f%n", usuario.getSaldo());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void processarReserva(Usuario usuario) {
        System.out.println("\nFazer Reserva: ");

        sistema.listarSessoesDisponiveis();
        List<Sessao> sessoes = sistema.listarTodasSessoes();

        System.out.print("Escolha o número da sessão: ");
        int indiceSessao = scanner.nextInt() - 1;
        scanner.nextLine();
        if(indiceSessao < 0 || indiceSessao >= sessoes.size()){
            System.out.println("Seleção de sessão inválida.");
            return;
        }
        Sessao sessaoEscolhida = sessoes.get(indiceSessao);

        System.out.println("Poltronas disponíveis: " + sessaoEscolhida.getPoltronaDisponiveis());
        if(sessaoEscolhida.getPoltronaDisponiveis().isEmpty()){
            System.out.println("Desculpe, não há mais poltronas disponíveis para esta sessão.");
            return;
        }
        System.out.print("Escolha o número da poltrona: ");
        int numPoltrona = scanner.nextInt();
        scanner.nextLine();

        List<Produto> produtosComprados = new ArrayList<>();
        String adicionarProduto;
        do{
            System.out.print("Deseja adicionar algum produto? (s/n): ");
            adicionarProduto = scanner.nextLine();
            if(adicionarProduto.equalsIgnoreCase("s")){
                sistema.listarProdutosDisponiveis();
                System.out.print("Digite o número do produto a adicionar: ");
                int indiceProduto = scanner.nextInt() - 1;
                scanner.nextLine();
                List<Produto> catalogo = sistema.listarProdutos();
                if(indiceProduto >= 0 && indiceProduto < catalogo.size()){
                    produtosComprados.add(catalogo.get(indiceProduto));
                    System.out.println(catalogo.get(indiceProduto).getNome() + " adicionado ao carrinho.");
                } else {
                    System.out.println("Produto inválido.");
                }
            }
        }while(adicionarProduto.equalsIgnoreCase("s"));

        System.out.print("\nConfirmar e processar a reserva? (s/n): ");
        String confirmacao = scanner.nextLine();
        if(confirmacao.equalsIgnoreCase("s")){
            sistema.validarEProcessarReserva(usuario, sessaoEscolhida, numPoltrona, produtosComprados);
        } else {
            System.out.println("Reserva cancelada pelo usuário.");
        }
    }
}