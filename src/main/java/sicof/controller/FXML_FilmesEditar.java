package sicof.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.converter.LocalDateStringConverter;
import sicof.dao.AtorDAO;
import sicof.dao.AtorFilmesDAO;
import sicof.dao.CategoriaDAO;
import sicof.dao.FilmeDAO;
import sicof.main.Main;
import sicof.model.Ator;
import sicof.model.AtorFilmes;
import sicof.model.Categoria;
import sicof.model.Filme;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static sicof.helpers.Utils.dateFormatter;

public class FXML_FilmesEditar implements Initializable {

    @FXML
    private AnchorPane editar_filme_root;

    private Filme filme;

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

    public void setFilme(Filme filme) {
        this.filme = filme;
        this.title.setText(filme.getTitle());
        this.release_date.setValue(dateFormatter(filme.getReleaseDate().toString()));
        this.category.setValue(filme.getCategory());
        this.list_actors.getItems().addAll(filme.getActors());
    }

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

        if (ator != null) {
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
        Filme filme = new Filme(this.filme.getId(), title, releaseDate, category, actors);
        FilmeDAO filmeDAO = new FilmeDAO();
        AtorFilmes atorFilmes;

        Alert alert;
        if (filmeDAO.update(filme)) {
            AtorFilmesDAO atorFilmesDAO = new AtorFilmesDAO();
            for (Ator ator : filme.getActors()) {
                if (!this.filme.getActors().contains(ator)) {
                    atorFilmes = new AtorFilmes(ator, filme);
                    atorFilmesDAO.insert(atorFilmes);
                }
            }

            for (Ator ator : this.filme.getActors()) {
                if (!filme.getActors().contains(ator)) {
                    atorFilmes = new AtorFilmes(ator, filme);
                    atorFilmesDAO.delete(atorFilmes);
                }
            }

            alert = new Alert(Alert.AlertType.INFORMATION, "Filme editado com sucesso!", ButtonType.OK);
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Erro ao editar filme!", ButtonType.OK);
        }

        alert.showAndWait();
    }

    public void cancelEdit(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja cancelar a edição?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.NO) {
            return;
        }

        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("FXML_FilmesListar.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BorderPane borderPane = (BorderPane) editar_filme_root.getParent();
        borderPane.setCenter(root);

        for (Node node : borderPane.getChildren()) {
            if (node.getStyleClass().contains("header-menu") && Objects.equals(node.getTypeSelector(), "HBox")) {
                for (Node child : ((HBox) node).getChildren()) {
                    if (Objects.equals(child.getTypeSelector(), "HBox")) {
                        for (Node button : ((HBox) child).getChildren()) {
                            if (Objects.equals(button.getTypeSelector(), "Button") && Objects.equals(((Button) button).getText(), "Listar")) {
                                button.getStyleClass().add("active");
                            }
                        }
                    }
                }
            }
        }
    }
}
