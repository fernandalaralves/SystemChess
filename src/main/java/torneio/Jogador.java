package torneio;

public class Jogador {

    private int id;
    private String nome;
    private int rating;
    private double pontuacao;

    public Jogador(int id, String nome, int rating) {

        if(nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome inválido");
        }

        this.id = id;
        this.nome = nome;
        this.rating = rating;
        this.pontuacao = 0;
    }

    public void adicionarPontos(double pontos) {
        this.pontuacao += pontos;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getRating() {
        return rating;
    }

    public double getPontuacao() {
        return pontuacao;
    }

    @Override
    public String toString() {
        return nome + " - " + pontuacao + " pts";
    }
}