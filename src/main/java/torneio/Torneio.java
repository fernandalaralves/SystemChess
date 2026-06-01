package torneio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Torneio {

    private String nome;
    private List<Jogador> jogadores;
    private List<Rodada> rodadas;

    public Torneio(String nome) {

        this.nome = nome;
        this.jogadores = new ArrayList<>();
        this.rodadas = new ArrayList<>();
    }

    public void cadastrarJogador(Jogador jogador) {
        jogadores.add(jogador);
    }

    public Rodada gerarRodada() {

        Rodada rodada = new Rodada(
            rodadas.size() + 1
        );

        for(int i = 0; i < jogadores.size() - 1; i += 2) {

            Partida partida =
                new Partida(
                    jogadores.get(i),
                    jogadores.get(i + 1)
                );

            rodada.adicionarPartida(partida);
        }

        if(jogadores.size() % 2 != 0) {

            Jogador bye =
                jogadores.get(jogadores.size() - 1);

            bye.adicionarPontos(1);
        }

        rodadas.add(rodada);

        return rodada;
    }

    public List<Jogador> gerarRanking() {

        List<Jogador> ranking =
            new ArrayList<>(jogadores);

        ranking.sort(
            Comparator
                .comparingDouble(
                    Jogador::getPontuacao
                )
                .reversed()
                .thenComparing(
                    Jogador::getRating
                )
                .reversed()
        );

        return ranking;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public List<Rodada> getRodadas() {
        return rodadas;
    }
}