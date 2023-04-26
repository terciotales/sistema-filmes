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
import sicof.dao.AtorDAO;
import sicof.dao.AtorDAO;
import sicof.main.Main;
import sicof.model.Ator;
import sicof.model.Ator;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static sicof.helpers.Utils.removeSpecialCharacters;

public class FXML_AtoresEditar implements Initializable {

    private Ator ator;

    @FXML
    private TextField new_name;

    @FXML
    private AnchorPane editar_ator_root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void save(MouseEvent event) {
        AtorDAO atorDAO = new AtorDAO();
        Ator ator = new Ator();

        if (new_name.getText().length() > 0) {
            if (atorDAO.get(new_name.getText()) != null || atorDAO.get(removeSpecialCharacters(new_name.getText())) != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Já existe um ator com esse nome!", ButtonType.OK);
                alert.showAndWait();
                return;
            }


            ator.setName(new_name.getText());
            ator.setId(this.ator.getId());

            Alert alert;
            if (atorDAO.update(ator)) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Ator alterado com sucesso!", ButtonType.OK);
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "Erro ao alterar ator!", ButtonType.OK);
            }
            alert.showAndWait();
        }
    }

    public void setAtor(Ator ator) {
        this.ator = ator;
        this.new_name.setText(ator.getName());
    }

    public void cancelEdit(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja cancelar a edição?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.NO) {
            return;
        }

        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("FXML_AtoresListar.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BorderPane borderPane = (BorderPane) editar_ator_root.getParent();
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
