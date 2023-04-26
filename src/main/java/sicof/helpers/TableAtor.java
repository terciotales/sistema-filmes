package sicof.helpers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableAtor {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty filmes;

    public TableAtor() {
    }

    public TableAtor(int id, String name, int filmes) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.filmes = new SimpleIntegerProperty(filmes);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String toString() {
        return getName();
    }

    public int getFilmes() {
        return filmes.get();
    }

    public SimpleIntegerProperty filmesProperty() {
        return filmes;
    }

    public void setFilmes(int filmes) {
        this.filmes.set(filmes);
    }
}
