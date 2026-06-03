package br.com.systemchess.model;

public final class Partida {

    private final int id;
    private final Jogador brancas;
    private final Jogador pretas;
    private Resultado resultado;

    public Partida(int id, Jogador brancas, Jogador pretas) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id invalido");
        }
        if (brancas == null || pretas == null) {
            throw new IllegalArgumentException("Jogadores sao obrigatorios");
        }
        if (brancas.equals(pretas)) {
            throw new IllegalArgumentException("Jogador nao pode enfrentar a si mesmo");
        }

        this.id = id;
        this.brancas = brancas;
        this.pretas = pretas;
    }

    public void registrarResultado(Resultado resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("Resultado obrigatorio");
        }
        if (this.resultado != null) {
            throw new IllegalStateException("Resultado ja registrado");
        }

        this.resultado = resultado;

        switch (resultado) {
            case VITORIA_BRANCAS:
                brancas.adicionarPontos(1);
                break;
            case VITORIA_PRETAS:
                pretas.adicionarPontos(1);
                break;
            case EMPATE:
                brancas.adicionarPontos(0.5);
                pretas.adicionarPontos(0.5);
                break;
        }
    }

    public int getId() {
        return id;
    }

    public Jogador getBrancas() {
        return brancas;
    }

    public Jogador getPretas() {
        return pretas;
    }

    public Resultado getResultado() {
        return resultado;
    }
}
