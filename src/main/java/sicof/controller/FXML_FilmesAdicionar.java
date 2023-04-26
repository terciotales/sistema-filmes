package sicof.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import sicof.dao.AtorDAO;
import sicof.dao.AtorFilmesDAO;
import sicof.dao.CategoriaDAO;
import sicof.dao.FilmeDAO;
import sicof.model.Ator;
import sicof.model.AtorFilmes;
import sicof.model.Categoria;
import sicof.model.Filme;

import java.net.URL;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class FXML_FilmesAdicionar implements Initializable {

    @FXML
    private ComboBox<Ator> actor;

    @FXML
    private ComboBox<Categoria> category;

    @FXML
    private ListView<Ator> list_actors;

    @FXML
    private DatePicker release_date;

    @FXML
    private TextField title;

    @FXML
    private Button add_ator;

    @FXML
    private Button remove_ator;

    public void initialize(URL location, ResourceBundle resources) {
        AtorDAO atorDAO = new AtorDAO();
        ArrayList<Ator> atores = atorDAO.getAll();
        ObservableList<Ator> observableList = FXCollections.observableArrayList(atores);
        this.actor.setItems(observableList);

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ArrayList<Categoria> categorias = categoriaDAO.getAll();
        ObservableList<Categoria> observableList2 = FXCollections.observableArrayList(categorias);
        this.category.setItems(observableList2);

        remove_ator.setDisable(true);
        add_ator.setDisable(true);

        this.list_actors.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            remove_ator.setDisable(newValue == null);
        });
    }

    @FXML
    void onComboAction(ActionEvent event) {
        if (this.actor.getValue() == null) {
            add_ator.setDisable(true);
            return;
        }

        for (Ator ator : this.list_actors.getItems()) {
            if (ator.getId() == this.actor.getValue().getId()) {
                add_ator.setDisable(true);
                return;
            }
        }

        Ator ator = this.actor.getValue();
        add_ator.setDisable(this.list_actors.getItems().contains(ator));
    }

    @FXML
    void addActor(MouseEvent event) {
        Ator ator = this.actor.getValue();
        if (ator != null && !this.list_actors.getItems().contains(ator)) {
            this.list_actors.getItems().add(ator);
            this.actor.setValue(null);
        }
    }

    @FXML
    void removeActor(MouseEvent event) {
        Ator ator = this.list_actors.getSelectionModel().getSelectedItem();
        if (ator != null) {
            this.list_actors.getItems().remove(ator);
        }
    }

    @FXML
    void saveFilme(MouseEvent event) {
        String title = this.title.getText();
        ZoneOffset zoneOffset = ZoneOffset.ofHours(0);
        Date releaseDate;
        Categoria category = this.category.getValue();
        ArrayList<Ator> actors = new ArrayList<>(this.list_actors.getItems());

        if (title.length() == 0 || this.release_date.getValue() == null || category == null || actors.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Revise os dados!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        releaseDate = Date.from(this.release_date.getValue().atStartOfDay().toInstant(zoneOffset));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(releaseDate);
        calendar.add(Calendar.DATE, 1);
        releaseDate = calendar.getTime();
        Filme filme = new Filme(0, title, releaseDate, category, actors);
        FilmeDAO filmeDAO = new FilmeDAO();
        AtorFilmes atorFilmes;

        boolean insert = filmeDAO.insert(filme);

        Alert alert;
        if (insert) {
            filme = filmeDAO.getLast();
            AtorDAO atorDAO = new AtorDAO();
            for (Ator ator : actors) {
                atorFilmes = new AtorFilmes(atorDAO.get(ator.getName()), filme);
                AtorFilmesDAO atorFilmesDAO = new AtorFilmesDAO();
                if (!atorFilmesDAO.insert(atorFilmes)) {
                    alert = new Alert(Alert.AlertType.ERROR, "Erro ao adicionar filme!", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            }

            alert = new Alert(Alert.AlertType.INFORMATION, "Filme adicionado com sucesso!", ButtonType.OK);

            this.title.setText("");
            this.release_date.setValue(null);
            this.category.setValue(null);
            this.list_actors.getItems().clear();
            this.actor.setValue(null);
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Erro ao adicionar filme!", ButtonType.OK);
        }

        alert.showAndWait();
    }
}
