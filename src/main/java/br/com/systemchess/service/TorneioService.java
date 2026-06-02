package br.com.systemchess.service;

import br.com.systemchess.model.Jogador;
import br.com.systemchess.model.Partida;
import br.com.systemchess.model.Resultado;
import br.com.systemchess.model.Rodada;
import br.com.systemchess.model.Torneio;

import java.util.List;

public final class TorneioService {

    private Torneio torneio;
    private int proximoJogadorId;
    private int proximoPartidaId;

    public TorneioService(String nome) {
        reiniciar(nome);
    }

    public static TorneioService criarPadrao() {
        TorneioService service = new TorneioService("SystemChess Open");
        service.cadastrarJogador("Kasparov", 2851);
        service.cadastrarJogador("Polgar", 2735);
        service.cadastrarJogador("Carlsen", 2882);
        service.cadastrarJogador("Anand", 2817);
        return service;
    }

    public synchronized Jogador cadastrarJogador(String nome, int rating) {
        Jogador jogador = new Jogador(proximoJogadorId++, nome, rating);
        torneio.cadastrarJogador(jogador);
        return jogador;
    }

    public synchronized Rodada gerarRodada() {
        Rodada rodada = torneio.gerarRodada(proximoPartidaId);
        proximoPartidaId += rodada.getPartidas().size();
        return rodada;
    }

    public synchronized Partida registrarResultado(int partidaId, Resultado resultado) {
        Partida partida = torneio.buscarPartida(partidaId);
        partida.registrarResultado(resultado);
        return partida;
    }

    public synchronized void reiniciar(String nome) {
        torneio = new Torneio(nome);
        proximoJogadorId = 1;
        proximoPartidaId = 1;
    }

    public synchronized Torneio getTorneio() {
        return torneio;
    }

    public synchronized List<Jogador> gerarRanking() {
        return torneio.gerarRanking();
    }
}
