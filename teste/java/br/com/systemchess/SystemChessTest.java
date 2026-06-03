package br.com.systemchess;

import br.com.systemchess.model.Jogador;
import br.com.systemchess.model.Partida;
import br.com.systemchess.model.Resultado;
import br.com.systemchess.model.Rodada;
import br.com.systemchess.service.TorneioService;
import br.com.systemchess.util.Json;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class SystemChessTest {

    @Test
    void deveCadastrarJogadorValido() {
        Jogador jogador = new Jogador(1, " Fernanda ", 1500);

        assertEquals("Fernanda", jogador.getNome());
        assertEquals(1500, jogador.getRating());
    }

    @Test
    void deveRejeitarDadosInvalidosDoJogador() {
        assertThrows(IllegalArgumentException.class, () -> new Jogador(0, "Ana", 1500));
        assertThrows(IllegalArgumentException.class, () -> new Jogador(1, " ", 1500));
        assertThrows(IllegalArgumentException.class, () -> new Jogador(1, "Ana", 99));
        assertThrows(IllegalArgumentException.class, () -> new Jogador(1, "Ana", 3501));
    }

    @Test
    void naoDeveCadastrarNomeDuplicado() {
        TorneioService service = new TorneioService("Teste");

        service.cadastrarJogador("Ana", 1400);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrarJogador("ana", 1500));
    }

    @Test
    void deveGerarRodadaComPartidas() {
        TorneioService service = torneioComQuatroJogadores();

        Rodada rodada = service.gerarRodada();

        assertEquals(1, rodada.getNumero());
        assertEquals(2, rodada.getPartidas().size());
        assertRegex("\\{\"numero\":1,\"concluida\":false,\"bye\":null,\"partidas\":\\[.+]}", Json.rodada(rodada));
    }

    @Test
    void deveRegistrarVitoriaDasBrancas() {
        TorneioService service = torneioComQuatroJogadores();
        Partida partida = service.gerarRodada().getPartidas().get(0);

        service.registrarResultado(partida.getId(), Resultado.VITORIA_BRANCAS);

        assertEquals(1.0, partida.getBrancas().getPontuacao());
        assertEquals(0.0, partida.getPretas().getPontuacao());
        assertRegex("\"resultado\":\"VITORIA_BRANCAS\"", Json.partida(partida));
    }

    @Test
    void deveRegistrarEmpate() {
        TorneioService service = torneioComQuatroJogadores();
        Partida partida = service.gerarRodada().getPartidas().get(0);

        service.registrarResultado(partida.getId(), Resultado.EMPATE);

        assertEquals(0.5, partida.getBrancas().getPontuacao());
        assertEquals(0.5, partida.getPretas().getPontuacao());
    }

    @Test
    void naoDeveRegistrarResultadoDuasVezes() {
        TorneioService service = torneioComQuatroJogadores();
        Partida partida = service.gerarRodada().getPartidas().get(0);

        service.registrarResultado(partida.getId(), Resultado.VITORIA_PRETAS);

        assertThrows(IllegalStateException.class, () -> service.registrarResultado(partida.getId(), Resultado.EMPATE));
    }

    @Test
    void naoDeveGerarNovaRodadaComRodadaAberta() {
        TorneioService service = torneioComQuatroJogadores();

        service.gerarRodada();

        assertThrows(IllegalStateException.class, service::gerarRodada);
    }

    @Test
    void deveAtribuirByeQuandoNumeroImpar() {
        TorneioService service = new TorneioService("Teste");
        service.cadastrarJogador("Ana", 1500);
        service.cadastrarJogador("Bia", 1600);
        service.cadastrarJogador("Caio", 1400);

        Rodada rodada = service.gerarRodada();

        assertNotNull(rodada.getBye());
        assertEquals(1.0, rodada.getBye().getPontuacao());
        assertRegex("\"bye\":\\{\"id\":\\d+,\"nome\":\"Caio\",\"rating\":1400,\"pontuacao\":1\\.0,\"byes\":1}", Json.rodada(rodada));
    }

    @Test
    void deveOrdenarRankingPorPontosRatingENome() {
        TorneioService service = torneioComQuatroJogadores();
        Rodada rodada = service.gerarRodada();
        rodada.getPartidas().forEach(partida -> service.registrarResultado(partida.getId(), Resultado.EMPATE));

        assertEquals("Diana", service.gerarRanking().get(0).getNome());
    }

    @Test
    void deveConverterJsonSimples() {
        Map<String, String> dados = Json.objeto("{\"nome\":\"Ana\",\"rating\":1500}");

        assertEquals("Ana", dados.get("nome"));
        assertEquals(1500, Json.inteiro(dados, "rating"));
    }

    @Test
    void deveSerializarMensagensNoContratoJson() {
        assertRegex("\\{\"status\":\"Torneio iniciado\"\\}", Json.mensagem("Torneio iniciado"));
        assertRegex("\\{\"erro\":\"Nome .*\"\\}", Json.erro("Nome \"invalido\""));
    }

    private TorneioService torneioComQuatroJogadores() {
        TorneioService service = new TorneioService("Teste");
        service.cadastrarJogador("Ana", 1500);
        service.cadastrarJogador("Bruno", 1450);
        service.cadastrarJogador("Caio", 1550);
        service.cadastrarJogador("Diana", 1600);
        return service;
    }

    private void assertRegex(String regex, String texto) {
        assertTrue(Pattern.compile(regex).matcher(texto).find(), () -> "Texto nao corresponde ao regex: " + texto);
    }
}
