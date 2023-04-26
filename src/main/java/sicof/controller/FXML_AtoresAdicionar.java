package sicof.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sicof.dao.AtorDAO;
import sicof.model.Ator;

import static sicof.helpers.Utils.removeSpecialCharacters;

public class FXML_AtoresAdicionar {

    @FXML
    private TextField nome;

    @FXML
    void save(MouseEvent event) {
        AtorDAO atorDAO = new AtorDAO();
        if (nome.getText().length() > 0) {
            if (atorDAO.get(nome.getText()) != null || atorDAO.get(removeSpecialCharacters(nome.getText())) != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "O ator já existe!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            Ator ator = new Ator(0, nome.getText());

            Alert alert;
            if (atorDAO.insert(ator)) {
                alert = new Alert(Alert.AlertType.INFORMATION, "Ator adicionado com sucesso!", ButtonType.OK);
                nome.setText("");
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "Erro ao adicionar o ator!", ButtonType.OK);
            }
            alert.showAndWait();
        }
    }
}
