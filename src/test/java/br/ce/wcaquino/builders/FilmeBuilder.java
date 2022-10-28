package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder() {

    }

    public static FilmeBuilder umFilme() { // Apenas o método de entrada deve ser estático
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setEstoque(2);
        builder.filme.setNome("Filme 1");
        builder.filme.setPrecoLocacao(10.0);
        return builder;
    }

    public static FilmeBuilder umFilmeSemEstoque() { // Uma segunda opção de método de entrada
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setEstoque(0);
        builder.filme.setNome("Filme 1");
        builder.filme.setPrecoLocacao(10.0);
        return builder;
    }

    // Os demais métodos dependem de já ter uma instância do Builder
    // Criar esses métodos de construção sob demanda, apenas se forem necessários para as regras de negócio
    public FilmeBuilder semEstoque() {
        filme.setEstoque(0);
        return this; // Retorna a instância do builder
    }

    public FilmeBuilder comValor(Double valor) {
        filme.setPrecoLocacao(valor);
        return this;
    }

    public Filme agora() {
        return filme;
    }
}
