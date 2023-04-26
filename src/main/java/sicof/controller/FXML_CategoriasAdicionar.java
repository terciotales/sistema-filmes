package sicof.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sicof.dao.CategoriaDAO;
import sicof.model.Categoria;

import java.util.Objects;

import static sicof.helpers.Utils.removeSpecialCharacters;

public class FXML_CategoriasAdicionar {

    @FXML
    private TextField nome;

    @FXML
    void save(MouseEvent event) {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        if (nome.getText().length() > 0) {
            if (categoriaDAO.getByName(nome.getText()) != null || categoriaDAO.getByName(removeSpecialCharacters(nome.getText())) != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Categoria já existe!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            Categoria categoria = new Categoria(0, nome.getText());
            boolean insert = categoriaDAO.insert(categoria);
            nome.setText("");

            Alert alert;
            if (insert) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Categoria adicionada com sucesso!", ButtonType.OK);
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "Erro ao adicionar categoria!", ButtonType.OK);
            }
            alert.showAndWait();
        }
    }
}
