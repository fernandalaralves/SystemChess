package br.com.systemchess.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Torneio {

    private final String nome;
    private final List<Jogador> jogadores;
    private final List<Rodada> rodadas;
    private final Set<String> confrontos;

    public Torneio(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do torneio invalido");
        }

        this.nome = nome.trim();
        this.jogadores = new ArrayList<>();
        this.rodadas = new ArrayList<>();
        this.confrontos = new HashSet<>();
    }

    public void cadastrarJogador(Jogador jogador) {
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador obrigatorio");
        }
        boolean nomeRepetido = jogadores.stream()
                .anyMatch(atual -> atual.getNome().equalsIgnoreCase(jogador.getNome()));
        if (nomeRepetido) {
            throw new IllegalArgumentException("Jogador ja cadastrado");
        }
        jogadores.add(jogador);
    }

    public Rodada gerarRodada(int proximoIdPartida) {
        if (jogadores.size() < 2) {
            throw new IllegalStateException("Cadastre ao menos dois jogadores");
        }
        if (!rodadas.isEmpty() && !rodadas.get(rodadas.size() - 1).estaConcluida()) {
            throw new IllegalStateException("Conclua a rodada atual antes de gerar outra");
        }

        Rodada rodada = new Rodada(rodadas.size() + 1);
        List<Jogador> disponiveis = gerarRanking();

        if (disponiveis.size() % 2 != 0) {
            Jogador bye = selecionarBye(disponiveis);
            rodada.definirBye(bye);
            disponiveis.remove(bye);
        }

        int idPartida = proximoIdPartida;
        while (!disponiveis.isEmpty()) {
            Jogador jogador = disponiveis.remove(0);
            int adversarioIndex = encontrarAdversario(jogador, disponiveis);
            Jogador adversario = disponiveis.remove(adversarioIndex);
            registrarConfronto(jogador, adversario);
            rodada.adicionarPartida(new Partida(idPartida++, jogador, adversario));
        }

        rodadas.add(rodada);
        return rodada;
    }

    public List<Jogador> gerarRanking() {
        List<Jogador> ranking = new ArrayList<>(jogadores);
        ranking.sort(
                Comparator.comparingDouble(Jogador::getPontuacao)
                        .reversed()
                        .thenComparing(Comparator.comparingInt(Jogador::getRating).reversed())
                        .thenComparing(Jogador::getNome)
        );
        return ranking;
    }

    public Partida buscarPartida(int id) {
        return rodadas.stream()
                .flatMap(rodada -> rodada.getPartidas().stream())
                .filter(partida -> partida.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Partida nao encontrada"));
    }

    private Jogador selecionarBye(List<Jogador> disponiveis) {
        return disponiveis.stream()
                .filter(jogador -> jogador.getByes() == 0)
                .min(Comparator.comparingDouble(Jogador::getPontuacao)
                        .thenComparingInt(Jogador::getRating)
                        .thenComparing(Jogador::getNome))
                .orElse(disponiveis.get(disponiveis.size() - 1));
    }

    private int encontrarAdversario(Jogador jogador, List<Jogador> candidatos) {
        for (int i = 0; i < candidatos.size(); i++) {
            if (!jaSeEnfrentaram(jogador, candidatos.get(i))) {
                return i;
            }
        }
        return 0;
    }

    private boolean jaSeEnfrentaram(Jogador primeiro, Jogador segundo) {
        return confrontos.contains(chaveConfronto(primeiro, segundo));
    }

    private void registrarConfronto(Jogador primeiro, Jogador segundo) {
        confrontos.add(chaveConfronto(primeiro, segundo));
    }

    private String chaveConfronto(Jogador primeiro, Jogador segundo) {
        int menor = Math.min(primeiro.getId(), segundo.getId());
        int maior = Math.max(primeiro.getId(), segundo.getId());
        return menor + ":" + maior;
    }

    public String getNome() {
        return nome;
    }

    public List<Jogador> getJogadores() {
        return Collections.unmodifiableList(jogadores);
    }

    public List<Rodada> getRodadas() {
        return Collections.unmodifiableList(rodadas);
    }
}
