package sicof.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import sicof.dao.FilmeDAO;
import sicof.main.Main;
import sicof.model.Filme;
import sicof.helpers.TableFilme;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class FXML_FilmesListar implements Initializable {

    @FXML
    private AnchorPane listar_filmes_root;

    @FXML
    private TextField busca;

    @FXML
    private TableView<TableFilme> table;

    @FXML
    private TableColumn<TableFilme, String> table_actor;

    @FXML
    private TableColumn<TableFilme, String> table_category;

    @FXML
    private TableColumn<TableFilme, Integer> table_id;

    @FXML
    private TableColumn<TableFilme, String> table_releaseDate;

    @FXML
    private TableColumn<TableFilme, String> table_title;

    @FXML
    private Button button_delete;

    @FXML
    private Button button_edit;

    public void initialize(URL location, ResourceBundle resources) {
        FilmeDAO filmeDAO = new FilmeDAO();
        ArrayList<Filme> filmes = filmeDAO.getAll();
        ObservableList<TableFilme> tableFilmes = FXCollections.observableArrayList();

        table_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        table_title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        table_releaseDate.setCellValueFactory(new PropertyValueFactory<>("ReleaseDate"));
        table_category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        table_actor.setCellValueFactory(new PropertyValueFactory<>("Actors"));

        for (Filme filme : filmes) {
            tableFilmes.add(new TableFilme(filme.getId(), filme.getTitle(), filme.getReleaseDateFormatted(), filme.getCategory(), filme.getActors()));
        }

        table.setItems(tableFilmes);

        tableSelectionListener();
    }

    private void tableSelectionListener() {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                button_delete.setDisable(false);
                button_edit.setDisable(false);
            } else {
                button_delete.setDisable(true);
                button_edit.setDisable(true);
            }
        });
    }

    public void clickEdit(MouseEvent mouseEvent) {
        Filme filme;
        FilmeDAO filmeDAO = new FilmeDAO();
        filme = filmeDAO.getById(table.getSelectionModel().getSelectedItem().getId());

        if (filme != null) {
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("FXML_FilmesEditar.fxml"));
                root = loader.load();
                FXML_FilmesEditar controller = loader.getController();
                controller.setFilme(filme);
            } catch (IOException e) {
                e.printStackTrace();
            }

            BorderPane borderPane = (BorderPane) listar_filmes_root.getParent();
            borderPane.setCenter(root);

            for (Node node : borderPane.getChildren()) {
                if (node.getStyleClass().contains("header-menu") && Objects.equals(node.getTypeSelector(), "HBox")) {
                    for (Node child : ((HBox) node).getChildren()) {
                        if (Objects.equals(child.getTypeSelector(), "HBox")) {
                            for (Node button : ((HBox) child).getChildren()) {
                               if (Objects.equals(button.getTypeSelector(), "Button")) {
                                   button.getStyleClass().remove("active");
                               }
                            }
                        }
                    }
                }
            }
        }
    }

    @FXML
    public void onSearch(KeyEvent keyEvent) {
        FilmeDAO filmeDAO = new FilmeDAO();
        ArrayList<Filme> filmes = filmeDAO.search(busca.getText());
        ObservableList<TableFilme> tableFilmes = FXCollections.observableArrayList();

        if (Objects.equals(busca.getText(), "")) {
            filmes = filmeDAO.getAll();
        }

        for (Filme filme : filmes) {
            tableFilmes.add(new TableFilme(filme.getId(), filme.getTitle(), filme.getReleaseDateFormatted(), filme.getCategory(), filme.getActors()));
        }

        table.getItems().clear();
        table.setItems(tableFilmes);
    }

    public void clickDelete(MouseEvent mouseEvent) {
        Filme filme;
        FilmeDAO filmeDAO = new FilmeDAO();
        filme = filmeDAO.getById(table.getSelectionModel().getSelectedItem().getId());

        if (filme != null) {
            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deletar Filme");
            alert.setHeaderText("Deseja realmente deletar o filme " + filme.getTitle() + "?");
            alert.showAndWait();

            if (alert.getResult().getText().equals("OK")) {
                if (filmeDAO.delete(filme)) {
                    table.getItems().remove(table.getSelectionModel().getSelectedItem());
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Deletar Filme");
                    alert.setHeaderText("Filme deletado com sucesso!");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Deletar Filme");
                    alert.setHeaderText("Erro ao deletar filme!");
                    alert.showAndWait();
                }
            } else {
                alert.close();
            }
        }
    }
}
