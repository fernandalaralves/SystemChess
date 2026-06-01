package torneio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TorneioTest {

    @Test
    void deveCadastrarJogador() {

        Jogador jogador =
            new Jogador(
                1,
                "Fernanda",
                1500
            );

        assertEquals(
            "Fernanda",
            jogador.getNome()
        );
    }

    @Test
    void naoDevePermitirNomeVazio() {

        assertThrows(
            IllegalArgumentException.class,
            () -> new Jogador(
                1,
                "",
                1500
            )
        );
    }

    @Test
    void deveAdicionarUmPontoAoVencedor() {

        Jogador j1 =
            new Jogador(
                1,
                "Ana",
                1400
            );

        Jogador j2 =
            new Jogador(
                2,
                "Carlos",
                1500
            );

        Partida partida =
            new Partida(j1, j2);

        partida.registrarResultado(
            Resultado.VITORIA_JOGADOR1
        );

        assertEquals(
            1.0,
            j1.getPontuacao()
        );
    }

    @Test
    void deveAdicionarMeioPontoNoEmpate() {

        Jogador j1 =
            new Jogador(
                1,
                "Ana",
                1400
            );

        Jogador j2 =
            new Jogador(
                2,
                "Carlos",
                1500
            );

        Partida partida =
            new Partida(j1, j2);

        partida.registrarResultado(
            Resultado.EMPATE
        );

        assertEquals(
            0.5,
            j1.getPontuacao()
        );

        assertEquals(
            0.5,
            j2.getPontuacao()
        );
    }

    @Test
    void deveGerarRankingCorreto() {

        Torneio torneio =
            new Torneio("IFCE Chess");

        Jogador j1 =
            new Jogador(
                1,
                "Ana",
                1500
            );

        Jogador j2 =
            new Jogador(
                2,
                "Carlos",
                1400
            );

        j1.adicionarPontos(3);
        j2.adicionarPontos(1);

        torneio.cadastrarJogador(j1);
        torneio.cadastrarJogador(j2);

        List<Jogador> ranking =
            torneio.gerarRanking();

        assertEquals(
            "Ana",
            ranking.get(0).getNome()
        );
    }
}