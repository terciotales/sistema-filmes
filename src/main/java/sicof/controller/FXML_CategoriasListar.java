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
import sicof.dao.CategoriaDAO;
import sicof.dao.FilmeDAO;
import sicof.helpers.TableCategoria;
import sicof.helpers.TableFilme;
import sicof.main.Main;
import sicof.model.Categoria;
import sicof.model.Filme;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class FXML_CategoriasListar implements Initializable {

    @FXML
    private AnchorPane listar_categorias_root;

    @FXML
    private TextField busca;

    @FXML
    private Button button_delete;

    @FXML
    private Button button_edit;

    @FXML
    private TableView<TableCategoria> table;

    @FXML
    private TableColumn<TableCategoria, Integer> table_id;

    @FXML
    private TableColumn<TableCategoria, String> table_title;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ArrayList<Categoria> categorias = categoriaDAO.getAll();
        ObservableList<TableCategoria> tableCategorias = FXCollections.observableArrayList();

        table_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        table_title.setCellValueFactory(new PropertyValueFactory<>("Name"));

        for (Categoria categoria : categorias) {
            tableCategorias.add(new TableCategoria(categoria.getId(), categoria.getName()));
        }

        table.setItems(tableCategorias);
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

    @FXML
    void clickDelete(MouseEvent event) {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        Categoria categoria = categoriaDAO.getById(table.getSelectionModel().getSelectedItem().getId());
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir a categoria " + categoria.getName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            FilmeDAO filmeDAO = new FilmeDAO();

            if (filmeDAO.existsWithCategory(categoria.getId())) {
                alert = new Alert(Alert.AlertType.ERROR, "Não foi possível excluir a categoria, pois existem filmes associados a ela!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            if (categoriaDAO.delete(categoria)) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Categoria excluída com sucesso!", ButtonType.OK);
                table.getItems().remove(table.getSelectionModel().getSelectedItem());
                table.getSelectionModel().clearSelection();
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "Não foi possível excluir a categoria!", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    @FXML
    void clickEdit(MouseEvent event) {
        Categoria categoria;
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        categoria = categoriaDAO.getById(table.getSelectionModel().getSelectedItem().getId());

        if (categoria != null) {
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("FXML_CategoriasEditar.fxml"));
                root = loader.load();
                FXML_CategoriasEditar controller = loader.getController();
                controller.setCategoria(categoria);
            } catch (IOException e) {
                e.printStackTrace();
            }

            BorderPane borderPane = (BorderPane) listar_categorias_root.getParent();
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
    void onSearch(KeyEvent event) {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ArrayList<Categoria> categorias = categoriaDAO.getAll();
        ObservableList<TableCategoria> tableCategorias = FXCollections.observableArrayList();

        table_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        table_title.setCellValueFactory(new PropertyValueFactory<>("Name"));

        for (Categoria categoria : categorias) {
            if (Objects.requireNonNull(categoria.getName()).toLowerCase().contains(busca.getText().toLowerCase())) {
                tableCategorias.add(new TableCategoria(categoria.getId(), categoria.getName()));
            }
        }

        table.setItems(tableCategorias);
        tableSelectionListener();
    }
}
