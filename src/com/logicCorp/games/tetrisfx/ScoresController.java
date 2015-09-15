package com.logicCorp.games.tetrisfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author hakim
 */
public class ScoresController implements Initializable {
   
    @FXML
    private TableColumn<Gamer, String> name;
    @FXML
    private TableColumn<Gamer, Integer> score;
    @FXML
    private TableColumn<Gamer, Integer> level;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    name.setCellValueFactory(
                new PropertyValueFactory<>("name"));

    score.setCellValueFactory(
                new PropertyValueFactory<>("score"));

    level.setCellValueFactory(
                new PropertyValueFactory<>("level"));
   
    }

}
