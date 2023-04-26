package sicof.controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sicof.dao.CategoriaDAO;
import sicof.main.Main;
import sicof.model.Categoria;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class FXML_Categorias implements Initializable {
    private String page = "Listar";

    @FXML
    private BorderPane border_pane;

    @FXML
    private HBox buttons;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.loadPage();
            this.setActiveButton();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void setPage(MouseEvent event) throws IOException {
        this.page = ((Button) event.getSource()).getText();
        this.setActiveButton();
        this.loadPage();
    }

    private void setActiveButton() {
        this.buttons.getChildren().forEach(button -> {
            if (Objects.equals(((Button) button).getText(), this.page)) {
                if (!((Button) button).getStyleClass().contains("active")) {
                    ((Button) button).getStyleClass().add("active");
                }
            } else {
                ((Button) button).getStyleClass().remove("active");
            }
        });
    }

    private void loadPage() throws IOException {
        Parent root = null;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("FXML_Categorias" + this.page + ".fxml"));
            root = fxmlLoader.load();
        } catch (IOException exception) {
            Logger.getLogger(FXML_View.class.getName()).log(java.util.logging.Level.SEVERE, null, exception);
        }

        border_pane.setCenter(root);
    }
}
