package br.com.systemchess.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Rodada {

    private final int numero;
    private final List<Partida> partidas;
    private Jogador bye;

    public Rodada(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("Numero da rodada invalido");
        }

        this.numero = numero;
        this.partidas = new ArrayList<>();
    }

    public void adicionarPartida(Partida partida) {
        if (partida == null) {
            throw new IllegalArgumentException("Partida obrigatoria");
        }
        partidas.add(partida);
    }

    public void definirBye(Jogador jogador) {
        if (bye != null) {
            throw new IllegalStateException("Bye ja definido");
        }
        bye = jogador;
        jogador.adicionarBye();
    }

    public boolean estaConcluida() {
        return partidas.stream().allMatch(partida -> partida.getResultado() != null);
    }

    public int getNumero() {
        return numero;
    }

    public List<Partida> getPartidas() {
        return Collections.unmodifiableList(partidas);
    }

    public Jogador getBye() {
        return bye;
    }
}
