package torneio;

public class Partida {

    private Jogador jogador1;
    private Jogador jogador2;
    private Resultado resultado;

    public Partida(Jogador jogador1, Jogador jogador2) {

        if(jogador1 == jogador2) {
            throw new IllegalArgumentException(
                "Jogador não pode jogar contra si mesmo"
            );
        }

        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
    }

    public void registrarResultado(Resultado resultado) {

        this.resultado = resultado;

        switch(resultado) {

            case VITORIA_JOGADOR1:
                jogador1.adicionarPontos(1);
                break;

            case VITORIA_JOGADOR2:
                jogador2.adicionarPontos(1);
                break;

            case EMPATE:
                jogador1.adicionarPontos(0.5);
                jogador2.adicionarPontos(0.5);
                break;
        }
    }

    public Jogador getJogador1() {
        return jogador1;
    }

    public Jogador getJogador2() {
        return jogador2;
    }

    public Resultado getResultado() {
        return resultado;
    }
}