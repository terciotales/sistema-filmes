package sicof.model;

public class AtorFilmes {
    private Ator ator;
    private Filme filme;

    public AtorFilmes() {
    }

    public AtorFilmes(Ator ator, Filme filme) {
        this.ator = ator;
        this.filme = filme;
    }

    public Ator getAtor() {
        return ator;
    }

    public void setAtor(Ator ator) {
        this.ator = ator;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }
}
