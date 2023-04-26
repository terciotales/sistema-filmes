package sicof.helpers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import sicof.model.Ator;
import sicof.model.Categoria;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class TableCategoria {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;

    public TableCategoria() {
    }

    public TableCategoria(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
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
}
