package br.com.systemchess.model;

import java.util.Objects;

public final class Jogador {

    private final int id;
    private final String nome;
    private final int rating;
    private double pontuacao;
    private int byes;

    public Jogador(int id, String nome, int rating) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id invalido");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome invalido");
        }
        if (rating < 100 || rating > 3500) {
            throw new IllegalArgumentException("Rating deve estar entre 100 e 3500");
        }

        this.id = id;
        this.nome = nome.trim();
        this.rating = rating;
    }

    public void adicionarPontos(double pontos) {
        if (pontos < 0) {
            throw new IllegalArgumentException("Pontuacao nao pode ser negativa");
        }
        pontuacao += pontos;
    }

    public void adicionarBye() {
        byes++;
        adicionarPontos(1);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getRating() {
        return rating;
    }

    public double getPontuacao() {
        return pontuacao;
    }

    public int getByes() {
        return byes;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Jogador)) {
            return false;
        }
        Jogador jogador = (Jogador) object;
        return id == jogador.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
