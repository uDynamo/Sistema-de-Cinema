package org.cinema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static SistemaCinema sistema = new SistemaCinema();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args){
        carregarDadosIniciais();
        Usuario usuarioLogado = registrarNovoUsuario();
        if(usuarioLogado == null){
            System.out.println("Encerrando o programa.");
            scanner.close();
            return;
        }

        int opcao;
        do{
            exibirMenu(usuarioLogado);
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1:
                    sistema.listarSessoesDisponiveis();
                    break;
                case 2:
                    sistema.listarProdutosDisponiveis();
                    break;
                case 3:
                    realizarDeposito(usuarioLogado);
                    break;
                case 4:
                    processarReserva(usuarioLogado);
                    break;
                case 0:
                    System.out.println("Obrigado por usar nosso sistema. Volte sempre!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            if(opcao != 0){
                System.out.print("\nPressione Enter para voltar...");
                scanner.nextLine();
            }

        }while(opcao != 0);

        scanner.close();
    }

    private static Usuario registrarNovoUsuario(){
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();
        System.out.print("Digite seu e-mail: ");
        String email = scanner.nextLine();
        LocalDate dataNascimento = null;
        while(dataNascimento == null){
            System.out.print("Digite sua data de nascimento (DD/MM/AAAA): ");
            String dataStr = scanner.nextLine();
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataNascimento = LocalDate.parse(dataStr, formatter);
            }catch(Exception e){
                System.out.println("ERRO: Formato de data inválido. Por favor, use DD/MM/AAAA.");
            }
        }
        Usuario novoUsuario = new Usuario(nome, email, dataNascimento, 0.0);
        if(sistema.cadastrarUsuario(novoUsuario)){
            System.out.println("Cadastro realizado com sucesso!");
            return novoUsuario;
        }else{
            return null;
        }
    }

    private static void exibirMenu(Usuario usuario){
        System.out.println("\nMEnu Principal:");
        System.out.printf("Saldo atual: R$ %.2f%n", usuario.getSaldo());
        System.out.println("1. Listar sessões em catálogo");
        System.out.println("2. Listar produtos disponíveis");
        System.out.println("3. Depositar valor na conta");
        System.out.println("4. Fazer uma reserva");
        System.out.println("0. Sair");
        System.out.println("----------------------");
    }

    private static void carregarDadosIniciais(){
        sistema.cadastrarProduto(new Produto("Pipoca", 24.00));
        sistema.cadastrarProduto(new Produto("Refrigerante", 12.00));
        Filme filme1 = new Filme("Homem Aranha", 0);
        Filme filme2 = new Filme("Premonição", 18);
        Filme filme3 = new Filme("Missão Impossível", 14);
        sistema.cadastrarFilme(filme1);
        sistema.cadastrarFilme(filme2);
        sistema.cadastrarFilme(filme3);
        sistema.agendarSessao(new Sessao(filme1, LocalDateTime.of(2025, 7, 16, 20, 30)));
        sistema.agendarSessao(new Sessao(filme2, LocalDateTime.of(2025, 7, 14, 20, 30)));
        sistema.agendarSessao(new Sessao(filme3, LocalDateTime.of(2025, 7, 14, 20, 30)));
    }

    private static void realizarDeposito(Usuario usuario){
        System.out.print("\nDigite o valor a ser depositado: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();
        try{
            usuario.depositar(valor);
            System.out.printf("Depósito realizado com sucesso! Novo saldo: R$ %.2f%n", usuario.getSaldo());
        }catch(IllegalArgumentException e){
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void processarReserva(Usuario usuario){
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