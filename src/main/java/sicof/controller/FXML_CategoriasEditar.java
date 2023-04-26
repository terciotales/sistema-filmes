package sicof.controller;

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
import sicof.dao.CategoriaDAO;
import sicof.main.Main;
import sicof.model.Categoria;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static sicof.helpers.Utils.removeSpecialCharacters;

public class FXML_CategoriasEditar implements Initializable {

    private Categoria categoria;

    @FXML
    private TextField new_name;

    @FXML
    private AnchorPane editar_categoria_root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void save(MouseEvent event) {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        Categoria categoria = new Categoria();

        if (new_name.getText().length() > 0) {
            if (categoriaDAO.getByName(new_name.getText()) != null || categoriaDAO.getByName(removeSpecialCharacters(new_name.getText())) != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Já existe uma categoria com esse nome!", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            
            
            categoria.setName(new_name.getText());
            categoria.setId(this.categoria.getId());

            Alert alert;
            if (categoriaDAO.update(categoria)) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Categoria alterada com sucesso!", ButtonType.OK);
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "Erro ao alterar categoria!", ButtonType.OK);
            }
            alert.showAndWait();
        }
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        this.new_name.setText(categoria.getName());
    }

    public void cancelEdit(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja cancelar a edição?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.NO) {
            return;
        }

        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("FXML_CategoriasListar.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BorderPane borderPane = (BorderPane) editar_categoria_root.getParent();
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
