package br.com.systemchess.controller;

import br.com.systemchess.model.Resultado;
import br.com.systemchess.service.TorneioService;
import br.com.systemchess.util.Json;

import java.util.Map;

public final class TorneioController {

    private final TorneioService service;

    public TorneioController(TorneioService service) {
        this.service = service;
    }

    public String obterTorneio() {
        return Json.torneio(service.getTorneio(), service.gerarRanking());
    }

    public String cadastrarJogador(String body) {
        Map<String, String> dados = Json.objeto(body);
        String nome = dados.get("nome");
        int rating = Json.inteiro(dados, "rating");
        return Json.jogador(service.cadastrarJogador(nome, rating));
    }

    public String gerarRodada() {
        return Json.rodada(service.gerarRodada());
    }

    public String registrarResultado(int partidaId, String body) {
        Map<String, String> dados = Json.objeto(body);
        Resultado resultado = Resultado.valueOf(dados.getOrDefault("resultado", ""));
        return Json.partida(service.registrarResultado(partidaId, resultado));
    }

    public String reiniciar(String body) {
        Map<String, String> dados = Json.objeto(body);
        String nome = dados.getOrDefault("nome", "SystemChess Open");
        service.reiniciar(nome);
        return obterTorneio();
    }
}
