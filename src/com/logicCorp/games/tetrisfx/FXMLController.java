package com.logicCorp.games.tetrisfx;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author hakim
 */
public class FXMLController implements Initializable, Observer {

    @FXML
    Pane boardPanel;
    @FXML
    Pane scorePanel;
    @FXML
    Pane nextPanel;
    @FXML
    AnchorPane mainPane;
    @FXML
    Label levelLabel;
    @FXML 
    ImageView pausedImage;
    @FXML
    Label currentScoreLabel;
    @FXML
    Label hiScoreLabel;
            
    AnimationTimer  animationTimer;   
    Canvas          canvas          = new javafx.scene.canvas.Canvas(20 * 10, 20 * 20);
    Canvas          canvas2         = new javafx.scene.canvas.Canvas(80, 80);
    JavaFxCanvas    canvasFX        = new JavaFxCanvas(canvas2);
    Canvas          background      = new javafx.scene.canvas.Canvas(canvas.getWidth(), canvas.getHeight());
    Tetris          tetris;
    Image           image           = new Image("com/logicCorp/games/tetrisfx/resources/tetris.jpg");
    HiScores        hiscores        = new HiScores();
    Node          scoresDialogBox;        
    
    public FXMLController() throws IOException {
        tetris = new Tetris(10, 20, new JavaFxCanvas(canvas));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        canvasFX.init(4, 4);        
        boardPanel.getChildren().add(background);
        boardPanel.getChildren().add(canvas);
        nextPanel.getChildren().add(canvas2);      
        scorePanel.setFocusTraversable(true);
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                tetris.enterGameLoop(now);
            }
        };
        hiscores.getTopGamers();
        try {
            scoresDialogBox = FXMLLoader.load(FXMLController.class.getResource("resources/scores.fxml"));            
            // showScoresDialog(null, 2);
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        drawBackground();
    }

    @FXML
    private void HandleKeyAction(KeyEvent e) {
        tetris.inputManager.handle(e);
    }

    private void drawBackground() {
        GraphicsContext gc = background.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, background.getWidth(), background.getHeight());
    }

    @Override
    public void update(Observable o, Object arg) {
        
        if (o instanceof Tetris) {
            GameState g = ((Tetris) o).getCurrentState();
            if (g.gameOver) {
                stopGame();              
                updateHallOfFame(g);            
            }
            
            currentScoreLabel.setText("score : " + g.score);
            hiScoreLabel.setText("top " + Math.max(hiscores.getGamers()[0].getScore(), g.score) );
            levelLabel.setText(Integer.toString(g.level));
            pausedImage.setVisible(g.gamePaused);
            canvasFX.drawTetrimino(g.tetrimino);
        } 
    }
   
    public void startGame() {
        animationTimer.start();
    }

    public void stopGame() {
        animationTimer.stop();
    }

    public void showScoresDialog(final GameState gameState, final int row) {       
        final ObservableList<Gamer> gamersData = FXCollections.observableArrayList();
                
            //fill the tableview with the names of best gamers
            final TableView scoresTable = (TableView) scoresDialogBox.lookup("#scoresTable");
            final TextField gamerName = (TextField) scoresDialogBox.lookup("#gamerName");
            Button okButton = (Button) scoresDialogBox.lookup("#saveBtn");
            Button cancelButton = (Button) scoresDialogBox.lookup("#cancelBtn");
            gamerName.setText("");           
            okButton.setOnAction((ActionEvent event) -> {
                if (!gamerName.getText().isEmpty() && gameState!=null) {
                    Gamer gamer = new Gamer(gamerName.getText(), gameState.score, gameState.level);  
                    gamersData.remove(HiScores.WALL_OF_FAME_LENGTH-1);
                    gamersData.add(row, gamer);
                    
                    Gamer[] g=new Gamer[gamersData.size()];
                    for (int i = 0; i < gamersData.size(); i++) {
                        g[i]=gamersData.get(i);
                    }                    
                    hiscores.setGamers(g);
                    hiscores.saveTopGamers();
                }
               hideScoresDialogBox();
               tetris.HandleAction(GameAction.RESET);  
               startGame();
            });

            cancelButton.setOnAction((ActionEvent event) -> {
                hideScoresDialogBox();                
               tetris.HandleAction(GameAction.RESET);  
               startGame();
            });
            Gamer[] t = hiscores.getGamers();
            gamersData.addAll(t);
            scoresTable.setItems(gamersData);  
            mainPane.getChildren().add(scoresDialogBox);
            
    }

    private void updateHallOfFame(GameState gameState) {
        int pos;
        if ((pos = hiscores.findGamerPosition(gameState.score)) != -1) {
            showScoresDialog(gameState, pos);           
        }
    }

    Tetris getTetris() {
        return tetris;
    }

    private void hideScoresDialogBox() {
         mainPane.getChildren().remove(scoresDialogBox);
    }
    
}
