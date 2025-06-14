package org.cinema;

public class Filme {
    private String titulo;
    private int classificacaoIndicativa;

    public Filme(String titulo, int classificacaoIndicativa){
        this.titulo = titulo;
        this.classificacaoIndicativa = classificacaoIndicativa;
    }

    public String getTitulo(){
        return titulo;
    }

    public int getClassificacaoIndicativa(){
        return classificacaoIndicativa;
    }
}
