package org.cinema;
import java.util.ArrayList;
import java.util.List;

public class SistemaCinema {
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Filme> filmes = new ArrayList<>();
    private List<Sessao> sessoes = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();


    public boolean cadastrarUsuario(Usuario usuario){
        for(Usuario u : usuarios){
            if(u.getEmail().equalsIgnoreCase(usuario.getEmail())){
                System.out.println("Erro: Já existe um usuário com o email " + usuario.getEmail());
                return false;
            }
        }
        usuarios.add(usuario);
        return true;
    }

    public Usuario buscarUsuarioPorEmail(String email){
        for(Usuario usuario : usuarios){
            if(usuario.getEmail().equalsIgnoreCase(email)){
                return usuario;
            }
        }
        System.out.println("Usuário com email '" + email + "' não encontrado.");
        return null;
    }

    public void cadastrarFilme(Filme filme){
        filmes.add(filme);
    }

    public void agendarSessao(Sessao sessao){
        sessoes.add(sessao);
    }

    public List<Sessao> getSessoesPorFilme(Filme filme){
        List<Sessao> sessoesDoFilme = new ArrayList<>();
        for(Sessao sessao : sessoes){
            if(sessao.getFilme().equals(filme)){
                sessoesDoFilme.add(sessao);
            }
        }
        return sessoesDoFilme;
    }

    public double calcularTotalProdutos(List<Produto> produtos){
        double total = 0.0;
        for(Produto produto : produtos){
            total += produto.getPreco();
        }
        return total;
    }

    public double calcularPrecoIngressoComDescontos(Sessao sessao, Usuario usuario){
        double precoIngresso = sessao.getPrecoIngressoBase();

        if(usuario.temDescontoPorIdade()){
            precoIngresso *= 0.5; // 50% de desconto
        }
        if(sessao.isDiaPromocional()){
            precoIngresso *= 0.7; // 30% de desconto adicional
        }
        return precoIngresso;
    }

    public double aplicarDescontoCombo(double precoTotal, List<Produto> produtos){
        boolean hasPipoca = false;
        boolean hasRefrigerante = false;

        for(Produto p : produtos){
            if(p.getNome().equalsIgnoreCase("Pipoca")) hasPipoca = true;
            if(p.getNome().equalsIgnoreCase("Refrigerante")) hasRefrigerante = true;
        }

        if(hasPipoca && hasRefrigerante){
            return precoTotal * 0.8; // 20% de desconto no combo completo
        } else if(hasPipoca || hasRefrigerante){
            return precoTotal * 0.9; // 10% de desconto com um dos itens
        }

        return precoTotal;
    }

    public double calcularPrecoFinal(Sessao sessao, Usuario usuario, List<Produto> produtos){
        double precoIngresso = calcularPrecoIngressoComDescontos(sessao, usuario);

        double precoProdutos = calcularTotalProdutos(produtos);

        double precoTotal = precoIngresso + precoProdutos;

        precoTotal = aplicarDescontoCombo(precoTotal, produtos);

        return precoTotal;
    }

    public boolean validarEProcessarReserva(Usuario usuario, Sessao sessao, int numeroPoltrona, List<Produto> produtos) {
        // Validar idade do usuário
        if(usuario.getIdade() < sessao.getFilme().getClassificacaoIndicativa()){
            System.out.println("Falha na reserva: Usuário não tem idade suficiente para este filme.");
            return false;
        }

        // Validar disponibilidade da pooltrona
        if(sessao.isPoltronaOcupada(numeroPoltrona)){
            System.out.println("Falha na reserva: A poltrona " + numeroPoltrona + " já está ocupada.");
            return false;
        }

        // Calcular o prećo
        double precoFinal = calcularPrecoFinal(sessao, usuario, produtos);
        System.out.printf("O preço final da sua reserva é: R$ %.2f%n", precoFinal);

        // Verificar se tem saldo suficiente
        if(!usuario.saldoSuficiente(precoFinal)){
            System.out.println("Falha na reserva: Saldo insuficiente.");
            return false;
        }

        // 5. Execução: Se todas as validações passaram, efetiva a reserva
        usuario.debitar(precoFinal);
        sessao.reservarPoltrona(numeroPoltrona);

        System.out.println("Reserva realizada com sucesso para " + usuario.getEmail());
        System.out.printf("Novo saldo: R$ %.2f%n", usuario.getSaldo());

        return true;
    }

    public List<Usuario> getUsuarios(){
        return this.usuarios;
    }

    public void cadastrarProduto(Produto produto){
        produtos.add(produto);
    }

    public List<Sessao> listarTodasSessoes(){
        return new ArrayList<>(sessoes);
    }

    public List<Produto> listarProdutos(){
        return new ArrayList<>(produtos);
    }

    public void listarSessoesDisponiveis(){
        System.out.println("\nSessões Disponíveis:");
        List<Sessao> sessoes = listarTodasSessoes();
        for (int i = 0; i < sessoes.size(); i++) {
            Sessao sessao = sessoes.get(i);
            System.out.printf("%d. %s - Poltronas disponíveis: %s%n", (i + 1), sessao.toString(), sessao.getPoltronaDisponiveis().toString());
        }
    }

    public void listarProdutosDisponiveis(){
        System.out.println("\nProdutos Disponiveis:");
        List<Produto> produtos = listarProdutos();
        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            System.out.printf("%d. %s - R$ %.2f%n", (i + 1), p.getNome(), p.getPreco());
        }
    }
}