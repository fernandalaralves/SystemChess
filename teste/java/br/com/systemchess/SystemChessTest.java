package br.com.systemchess;

import br.com.systemchess.model.Jogador;
import br.com.systemchess.model.Partida;
import br.com.systemchess.model.Resultado;
import br.com.systemchess.model.Rodada;
import br.com.systemchess.service.TorneioService;
import br.com.systemchess.util.Json;

import java.util.Map;

public final class SystemChessTest {

    private int executados;

    public static void main(String[] args) {
        SystemChessTest suite = new SystemChessTest();
        suite.deveCadastrarJogadorValido();
        suite.naoDevePermitirNomeVazio();
        suite.naoDevePermitirRatingForaDaFaixa();
        suite.naoDeveCadastrarNomeDuplicado();
        suite.deveGerarRodadaComPartidas();
        suite.deveRegistrarVitoriaDasBrancas();
        suite.deveRegistrarEmpate();
        suite.naoDeveRegistrarResultadoDuasVezes();
        suite.naoDeveGerarNovaRodadaComRodadaAberta();
        suite.deveAtribuirByeQuandoNumeroImpar();
        suite.deveOrdenarRankingPorPontosRatingENome();
        suite.deveConverterJsonSimples();
        System.out.println("Testes executados: " + suite.executados);
    }

    void deveCadastrarJogadorValido() {
        Jogador jogador = new Jogador(1, "Fernanda", 1500);
        igual("Fernanda", jogador.getNome());
        igual(1500, jogador.getRating());
    }

    void naoDevePermitirNomeVazio() {
        erro(IllegalArgumentException.class, () -> new Jogador(1, " ", 1500));
    }

    void naoDevePermitirRatingForaDaFaixa() {
        erro(IllegalArgumentException.class, () -> new Jogador(1, "Ana", 99));
        erro(IllegalArgumentException.class, () -> new Jogador(1, "Ana", 3501));
    }

    void naoDeveCadastrarNomeDuplicado() {
        TorneioService service = new TorneioService("Teste");
        service.cadastrarJogador("Ana", 1400);
        erro(IllegalArgumentException.class, () -> service.cadastrarJogador("ana", 1500));
    }

    void deveGerarRodadaComPartidas() {
        TorneioService service = torneioComQuatroJogadores();
        Rodada rodada = service.gerarRodada();
        igual(1, rodada.getNumero());
        igual(2, rodada.getPartidas().size());
    }

    void deveRegistrarVitoriaDasBrancas() {
        TorneioService service = torneioComQuatroJogadores();
        Partida partida = service.gerarRodada().getPartidas().get(0);
        service.registrarResultado(partida.getId(), Resultado.VITORIA_BRANCAS);
        igual(1.0, partida.getBrancas().getPontuacao());
        igual(0.0, partida.getPretas().getPontuacao());
    }

    void deveRegistrarEmpate() {
        TorneioService service = torneioComQuatroJogadores();
        Partida partida = service.gerarRodada().getPartidas().get(0);
        service.registrarResultado(partida.getId(), Resultado.EMPATE);
        igual(0.5, partida.getBrancas().getPontuacao());
        igual(0.5, partida.getPretas().getPontuacao());
    }

    void naoDeveRegistrarResultadoDuasVezes() {
        TorneioService service = torneioComQuatroJogadores();
        Partida partida = service.gerarRodada().getPartidas().get(0);
        service.registrarResultado(partida.getId(), Resultado.VITORIA_PRETAS);
        erro(IllegalStateException.class, () -> service.registrarResultado(partida.getId(), Resultado.EMPATE));
    }

    void naoDeveGerarNovaRodadaComRodadaAberta() {
        TorneioService service = torneioComQuatroJogadores();
        service.gerarRodada();
        erro(IllegalStateException.class, service::gerarRodada);
    }

    void deveAtribuirByeQuandoNumeroImpar() {
        TorneioService service = new TorneioService("Teste");
        service.cadastrarJogador("Ana", 1500);
        service.cadastrarJogador("Bia", 1600);
        service.cadastrarJogador("Caio", 1400);
        Rodada rodada = service.gerarRodada();
        verdadeiro(rodada.getBye() != null);
        igual(1.0, rodada.getBye().getPontuacao());
    }

    void deveOrdenarRankingPorPontosRatingENome() {
        TorneioService service = torneioComQuatroJogadores();
        Rodada rodada = service.gerarRodada();
        for (Partida partida : rodada.getPartidas()) {
            service.registrarResultado(partida.getId(), Resultado.EMPATE);
        }
        igual("Diana", service.gerarRanking().get(0).getNome());
    }

    void deveConverterJsonSimples() {
        Map<String, String> dados = Json.objeto("{\"nome\":\"Ana\",\"rating\":1500}");
        igual("Ana", dados.get("nome"));
        igual(1500, Json.inteiro(dados, "rating"));
    }

    private TorneioService torneioComQuatroJogadores() {
        TorneioService service = new TorneioService("Teste");
        service.cadastrarJogador("Ana", 1500);
        service.cadastrarJogador("Bruno", 1450);
        service.cadastrarJogador("Caio", 1550);
        service.cadastrarJogador("Diana", 1600);
        return service;
    }

    private void verdadeiro(boolean condicao) {
        executados++;
        if (!condicao) {
            throw new AssertionError("Condicao esperada nao foi atendida");
        }
    }

    private void igual(Object esperado, Object obtido) {
        executados++;
        if (!esperado.equals(obtido)) {
            throw new AssertionError("Esperado " + esperado + ", obtido " + obtido);
        }
    }

    private void erro(Class<? extends Throwable> tipo, Acao acao) {
        executados++;
        try {
            acao.executar();
        } catch (Throwable ex) {
            if (tipo.isInstance(ex)) {
                return;
            }
            throw new AssertionError("Excecao esperada " + tipo.getSimpleName() + ", obtida " + ex.getClass().getSimpleName());
        }
        throw new AssertionError("Excecao esperada " + tipo.getSimpleName());
    }

    @FunctionalInterface
    interface Acao {
        void executar();
    }
}
