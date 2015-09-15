package com.logicCorp.games.tetrisfx;

/**
 *
 * @author hakim
 */

public class GameState {

    public GameState() {
        tetrimino=null;
        score=0;
        level=1;
        linesCleared=0;
        gameOver=false;
        gamePaused=false;
        
    }
    
   Tetrimino tetrimino ;
   int score;
   int level;
   int linesCleared;
   boolean gameOver;
   boolean gamePaused;
}
