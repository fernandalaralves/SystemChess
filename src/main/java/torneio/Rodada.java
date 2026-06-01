package torneio;

import java.util.ArrayList;
import java.util.List;

public class Rodada {

    private int numero;
    private List<Partida> partidas;

    public Rodada(int numero) {
        this.numero = numero;
        this.partidas = new ArrayList<>();
    }

    public void adicionarPartida(Partida partida) {
        partidas.add(partida);
    }

    public int getNumero() {
        return numero;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }
}