package com.logicCorp.games.tetrisfx;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author hakim
 */
public class InputManager implements EventHandler<KeyEvent>{
    private final Tetris tetris;
    
    public InputManager(Tetris tetris) {
        this.tetris=tetris;
    }
    

    public GameAction mapKeysToGameAction(KeyEvent t) {

        if (t.getCode() == KeyCode.LEFT) {
            return GameAction.GO_LEFT;
        } else if (t.getCode() == KeyCode.RIGHT) {
            return GameAction.GO_RIGHT;
        } else if (t.getCode() == KeyCode.SPACE) {
            return GameAction.ROTATE;
        } else if (t.getCode() == KeyCode.DOWN) {
            return GameAction.GO_DOWN;
        }
        else if (t.getCode() == KeyCode.CONTROL) {
            return GameAction.PAUSE;
        }
        else if (t.getCode() == KeyCode.F2) {
            return GameAction.RESET;
        }
        return GameAction.NONE;
    }

    @Override
    public void handle(KeyEvent event) {        
        tetris.HandleAction(mapKeysToGameAction(event));
    }
}
