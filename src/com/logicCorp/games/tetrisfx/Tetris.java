package com.logicCorp.games.tetrisfx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author hakim
 */

public class Tetris extends Observable {

    private static final int SCORE_ONE_ROW_CLEARED = 100;
    private static final int TETRIS = 800;
    private static final int NB_ROWS_PER_LEVEL = 10;
    private int cols;
    private int lines;
    double score = 0;
    private int rowsCleared;
    private int level;
    private Cell[][] field;
    private Tetrimino curTetrimino;
    private Tetrimino nextTetrimino;
    private Position curPos;
    private JavaFxCanvas canvas;
    private Boolean gameOver;
    private Position initialPosition = new Position(5, 0);
    long startTime = 0;
    public long displayTimePerFrameMillis = 500;
    private boolean keyTyped;
    private int padding;  //for keybord input +1 go to right -1 go to left
    private int totalLines;
    InputManager inputManager;
    Observer observer;
    SoundPlayer soundPlayer;
    boolean gamePaused;

    
    public Tetris(int cols, int lines, JavaFxCanvas canvas) throws IOException {
        this.cols = cols;
        this.lines = lines;
        this.canvas = canvas;
        inputManager = new InputManager(this);
        soundPlayer = new SoundPlayer("resources/tetris.mp3");
        resetGame();
        notifyObserver();
    }

    private void resetGame() {
        soundPlayer.close();
        soundPlayer.play();
        gamePaused = false;
        gameOver = false;
        field = new Cell[lines][cols];
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < cols; j++) {
                field[i][j] = new Cell();
            }
        }
        score = 0;
        rowsCleared = 0;
        level = 1;
        canvas.init(cols, lines);
        totalLines = 0;
        AddNewTetriminos();
    }

    public void AddNewTetriminos() {
        curPos = new Position(initialPosition.x, initialPosition.y);
        if (nextTetrimino != null) {
            curTetrimino = nextTetrimino;
        } else {
            curTetrimino = Tetriminos.types[(int) Math.round(Math.random() * (Tetriminos.types.length - 1))];

        }
        nextTetrimino = Tetriminos.types[(int) Math.round(Math.random() * (Tetriminos.types.length - 1))];
        notifyObserver();
    }

    public void drawTheField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (!field[i][j].empty) {
                    canvas.drawCell(j, i, field[i][j].type);
                }
            }
        }

    }

    private boolean isRowFull(int row) {
        for (int i = 0; i < field[row].length; i++) {
            if (field[row][i].empty == true) {
                return false;
            }
        }
        return true;
    }

    public void drawcurrentTetrimino() {
        int nbBlankLines = nbBlankLines(curTetrimino);
        int nbBlankCols = nbBlankCols(curTetrimino);
        for (int i = nbBlankLines; i < curTetrimino.cells.length; i++) {
            for (int j = 0; j < curTetrimino.cells[i].length; j++) {
                if (curTetrimino.cells[i][j] == 1) {
                    canvas.drawCell(j + curPos.x - nbBlankCols, i + curPos.y - nbBlankLines, curTetrimino.type);
                }
            }
        }
    }

    public void fixTetrimino(Tetrimino tetrimino, Position pos) {
        int nbBlankLines = nbBlankLines(tetrimino);
        int nbBlankCols = nbBlankCols(tetrimino);
        for (int i = nbBlankLines; i < tetrimino.cells.length; i++) {
            for (int j = 0; j < tetrimino.cells[i].length; j++) {
                if (tetrimino.cells[i][j] == 1) {
                    field[ i + pos.y - nbBlankLines][j + pos.x - nbBlankCols].empty = false;
                    field[ i + pos.y - nbBlankLines][j + pos.x - nbBlankCols].type = tetrimino.type;
                }
            }
        }
    }

    public int nbBlankLines(Tetrimino tetrimino) {
        int nbBlankLines = 0;
        int nbBlankCells = 0;

        for (byte[] cell : tetrimino.cells) {
            nbBlankCells = 0;
            for (int j = 0; j < cell.length; j++) {
                if (cell[j] == 0) {
                    nbBlankCells++;
                } else {
                    return nbBlankLines;
                }
            }
            if (nbBlankCells == tetrimino.cells.length) {
                nbBlankLines++;
                nbBlankCells = 0;
            }
            if (nbBlankCells != 0) {
                break;
            }
        }
        return nbBlankLines;
    }

    public int nbBlankCols(Tetrimino tetrimino) {
        int nbBlanckCols = 0;
        int nbBlankCells = 0;

        for (int col = 0; col < tetrimino.cells.length; col++) {
            nbBlankCells = 0;
            for (byte[] cell : tetrimino.cells) {
                if (cell[col] != 0) {
                    return nbBlanckCols;
                } else {
                    nbBlankCells++;
                }
            }
            if (nbBlankCells == tetrimino.cells.length) {
                nbBlanckCols++;
            }
        }
        return nbBlanckCols;
    }

    public boolean isPossibleToDrawTetriminoAt(Tetrimino tetrimino, Position pos) {
        int nbBlankLines = nbBlankLines(tetrimino);
        int nbBlankCols = nbBlankCols(tetrimino);
        for (int i = nbBlankLines; i < tetrimino.cells.length; i++) {
            for (int j = 0; j < tetrimino.cells[i].length; j++) {

                if ((pos.x < 0 || pos.y < 0)) {
                    return false;
                }

                if (tetrimino.cells[i][j] == 1 && lines <= pos.y + i - nbBlankLines) {
                    return false;
                }

                if (tetrimino.cells[i][j] == 1 && cols <= pos.x + j - nbBlankCols) {
                    return false;
                }

                if (tetrimino.cells[i][j] == 1 && field[pos.y + i - nbBlankLines][pos.x + j - nbBlankCols].empty == false) {
                    return false;
                }

            }
        }
        return true;
    }

    public void clearFullLines() {
        List<Integer> lines = new ArrayList();
        for (int i = field.length - 1; i >= 0; i--) {
            if (isRowFull(i)) {
                lines.add(0, i);
            }
        }

        if (lines.size() > 0) {
            updateScore(lines.size());
            totalLines += lines.size();
            updateLevel();
            rowsCleared += lines.size();
        }


        for (Integer i : lines) {
            for (int line = i; line > 0; line--) {
                for (int j = 0; j < field[i].length; j++) {
                    field[line][j].empty = field[line - 1][j].empty;
                    field[line][j].type = field[line - 1][j].type;
                }
            }
        }
    }

    public void updateScore(int nbRowsCleared) {
        if (nbRowsCleared <= 4) {
            score += SCORE_ONE_ROW_CLEARED * nbRowsCleared;
        } else {
            score = TETRIS;
        }
        notifyObserver();
    }

    public void updateLevel() {
        level = totalLines / NB_ROWS_PER_LEVEL + 1;
        notifyObserver();
    }

    public long getGameSpeed() {
        return (displayTimePerFrameMillis - level * 50) * 1000000;
    }

    public void enterGameLoop(long now) {
        if (startTime == 0) {
            startTime = now;
        }
        notifyObserver();
        long elapsed = now - startTime;
        if (!gamePaused && (elapsed > getGameSpeed() || keyTyped)) {
            canvas.clearBackground();
            drawTheField();

            if (!isPossibleToDrawTetriminoAt(nextTetrimino, initialPosition)) {
                gameOver = true;
            } else if (isPossibleToDrawTetriminoAt(curTetrimino, new Position(curPos.x + padding, curPos.y + 1))) {
                curPos.x = curPos.x + padding;
                curPos.y = curPos.y + 1;
                drawcurrentTetrimino();
            } else if (isPossibleToDrawTetriminoAt(curTetrimino, new Position(curPos.x, curPos.y + 1))) {
                curPos.y = curPos.y + 1;
                drawcurrentTetrimino();
            } else {
                fixTetrimino(curTetrimino, curPos);
                clearFullLines();
                canvas.clearBackground();
                drawTheField();
                AddNewTetriminos();
            }

            if (padding != 0) {
                padding = 0;
            }

            if (keyTyped) {
                keyTyped = false;
            }

            if (gameOver) {
                notifyObserver();
            }
            startTime = 0;
        }
    }

    public void HandleAction(GameAction gameAction) {
        keyTyped = true;
        if (gameAction == GameAction.GO_LEFT) {
            padding = -1;

        } else if (gameAction == GameAction.GO_RIGHT) {
            padding = 1;
        } else if (gameAction == GameAction.ROTATE) {
            Tetrimino t = curTetrimino.clone();
            if (isPossibleToDrawTetriminoAt(t.rotateClockWise(),
                    new Position(curPos.x, curPos.y))) {
                curTetrimino.rotateClockWise();
            }

        } else if (gameAction == GameAction.GO_DOWN) {
            while (isPossibleToDrawTetriminoAt(curTetrimino,
                    new Position(curPos.x, curPos.y + 1))) {
                curPos.y = curPos.y + 1;
            }
        } else if (gameAction == GameAction.PAUSE) {
            gamePaused = !gamePaused;
            if (gamePaused) {
                soundPlayer.suspend();
            }else {
                soundPlayer.resume();
            }

        } else if (gameAction == GameAction.RESET) {
            resetGame();
        }
    }

    public GameState getCurrentState() {
        GameState gameState = new GameState();
        gameState.tetrimino = nextTetrimino;
        gameState.linesCleared = rowsCleared;
        gameState.score = (int) score;
        gameState.level = level;
        gameState.gameOver = gameOver;
        gameState.gamePaused = gamePaused;
        return gameState;
    }

    private void notifyObserver() {
        if (observer != null) {
            observer.update(this, null);
        }
    }

    public JavaFxCanvas getJavaFxCanvas() {
        return canvas;
    }

    public void setJavaFxCanvas(JavaFxCanvas canvas) {
        this.canvas = canvas;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    public void setSoundPlayer(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    class Cell {

        boolean empty = true;
        int type = -1;
    }
}
