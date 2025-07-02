package org.cinema;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SistemaCinema sistema = new SistemaCinema();
        Scanner scanner = new Scanner(System.in);
        CinemaController controller = new CinemaController(sistema, scanner);

        carregarDadosIniciais(sistema);

        Usuario usuarioLogado = controller.registrarNovoUsuario();

        int opcao;
        do {
            controller.exibirMenu(usuarioLogado);
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    sistema.listarSessoesDisponiveis();
                    break;
                case 2:
                    sistema.listarProdutosDisponiveis();
                    break;
                case 3:
                    controller.realizarDeposito(usuarioLogado);
                    break;
                case 4:
                    controller.processarReserva(usuarioLogado);
                    break;
                case 0:
                    System.out.println("Obrigado por usar nosso sistema. Volte sempre!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

            if (opcao != 0) {
                System.out.print("\nPressione Enter para voltar...");
                scanner.nextLine();
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void carregarDadosIniciais(SistemaCinema sistema) {
        sistema.cadastrarProduto(new Produto("Pipoca", 24.00));
        sistema.cadastrarProduto(new Produto("Refrigerante", 12.00));
        Filme filme1 = new Filme("Homem-Aranha", 0);
        Filme filme2 = new Filme("Premonição", 18);
        Filme filme3 = new Filme("Missão Impossível", 14);
        sistema.cadastrarFilme(filme1);
        sistema.cadastrarFilme(filme2);
        sistema.cadastrarFilme(filme3);
        sistema.agendarSessao(new Sessao(filme1, LocalDateTime.of(2025, 8, 16, 00, 00)));
        sistema.agendarSessao(new Sessao(filme2, LocalDateTime.of(2025, 8, 17, 00, 00)));
        sistema.agendarSessao(new Sessao(filme3, LocalDateTime.of(2025, 8, 19, 00, 00)));
    }
}