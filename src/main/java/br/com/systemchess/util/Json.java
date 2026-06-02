package br.com.systemchess.util;

import br.com.systemchess.model.Jogador;
import br.com.systemchess.model.Partida;
import br.com.systemchess.model.Rodada;
import br.com.systemchess.model.Torneio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Json {

    private Json() {
    }

    public static Map<String, String> objeto(String json) {
        Map<String, String> dados = new LinkedHashMap<>();
        if (json == null || json.trim().isEmpty()) {
            return dados;
        }

        String conteudo = json.trim();
        if (!conteudo.startsWith("{") || !conteudo.endsWith("}")) {
            throw new IllegalArgumentException("JSON invalido");
        }

        conteudo = conteudo.substring(1, conteudo.length() - 1).trim();
        if (conteudo.isEmpty()) {
            return dados;
        }

        for (String item : conteudo.split(",")) {
            String[] partes = item.split(":", 2);
            if (partes.length != 2) {
                throw new IllegalArgumentException("JSON invalido");
            }
            dados.put(limpar(partes[0]), limpar(partes[1]));
        }
        return dados;
    }

    public static int inteiro(Map<String, String> dados, String campo) {
        try {
            return Integer.parseInt(dados.getOrDefault(campo, ""));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Campo " + campo + " invalido");
        }
    }

    public static String mensagem(String mensagem) {
        return "{\"status\":\"" + escapar(mensagem) + "\"}";
    }

    public static String erro(String mensagem) {
        return "{\"erro\":\"" + escapar(mensagem) + "\"}";
    }

    public static String torneio(Torneio torneio, List<Jogador> ranking) {
        return "{"
                + "\"nome\":\"" + escapar(torneio.getNome()) + "\","
                + "\"jogadores\":" + lista(torneio.getJogadores().stream().map(Json::jogador).toList()) + ","
                + "\"ranking\":" + lista(ranking.stream().map(Json::jogador).toList()) + ","
                + "\"rodadas\":" + lista(torneio.getRodadas().stream().map(Json::rodada).toList())
                + "}";
    }

    public static String jogador(Jogador jogador) {
        return "{"
                + "\"id\":" + jogador.getId() + ","
                + "\"nome\":\"" + escapar(jogador.getNome()) + "\","
                + "\"rating\":" + jogador.getRating() + ","
                + "\"pontuacao\":" + jogador.getPontuacao() + ","
                + "\"byes\":" + jogador.getByes()
                + "}";
    }

    public static String rodada(Rodada rodada) {
        String bye = rodada.getBye() == null ? "null" : jogador(rodada.getBye());
        return "{"
                + "\"numero\":" + rodada.getNumero() + ","
                + "\"concluida\":" + rodada.estaConcluida() + ","
                + "\"bye\":" + bye + ","
                + "\"partidas\":" + lista(rodada.getPartidas().stream().map(Json::partida).toList())
                + "}";
    }

    public static String partida(Partida partida) {
        String resultado = partida.getResultado() == null ? "null" : "\"" + partida.getResultado().name() + "\"";
        return "{"
                + "\"id\":" + partida.getId() + ","
                + "\"brancas\":" + jogador(partida.getBrancas()) + ","
                + "\"pretas\":" + jogador(partida.getPretas()) + ","
                + "\"resultado\":" + resultado
                + "}";
    }

    private static String lista(List<String> itens) {
        return itens.stream().collect(Collectors.joining(",", "[", "]"));
    }

    private static String limpar(String valor) {
        String limpo = valor.trim();
        if (limpo.startsWith("\"") && limpo.endsWith("\"") && limpo.length() >= 2) {
            return limpo.substring(1, limpo.length() - 1).trim();
        }
        return limpo;
    }

    private static String escapar(String valor) {
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
