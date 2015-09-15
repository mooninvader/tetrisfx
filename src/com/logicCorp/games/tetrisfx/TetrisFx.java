package com.logicCorp.games.tetrisfx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author hakim
 */
public class TetrisFx extends Application {

    FXMLController controller ;

    public TetrisFx()  {
        try {
            controller = new FXMLController();
        } catch (IOException ex) {
            Logger.getLogger(TetrisFx.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root;
        Scene scene;
        
        FXMLLoader loader = new FXMLLoader(TetrisFx.class.getResource("resources/MainForm.fxml"), null);
        controller.getTetris().setObserver(controller);
        loader.setController(controller);
        controller.getTetris().addObserver(controller);
        loader.load();

        root = loader.getRoot();
        scene = new Scene(root);
        
        primaryStage.setScene(scene);
        
        
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
        
        primaryStage.setTitle("TETRIS");
        controller.startGame();
        
    }
   
}
