package Game;

import Affichage.Ball;
import Affichage.Paddle;
import Piece.Piece;

public class GameState {
    private Ball ball;
    private Paddle whitePaddle;
    private Paddle blackPaddle;
    private Piece[][] pieces;
    private boolean gameOver;
    private String winner;
    private boolean[] keysPressed;
    private LaunchManager launchManager;

    public GameState() {
        this.gameOver = false;
        this.winner = null;
        this.keysPressed = new boolean[256];
        this.launchManager = new LaunchManager();
    }

    // Getters et setters
    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Paddle getWhitePaddle() {
        return whitePaddle;
    }

    public void setWhitePaddle(Paddle whitePaddle) {
        this.whitePaddle = whitePaddle;
    }

    public Paddle getBlackPaddle() {
        return blackPaddle;
    }

    public void setBlackPaddle(Paddle blackPaddle) {
        this.blackPaddle = blackPaddle;
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[][] pieces) {
        this.pieces = pieces;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public boolean[] getKeysPressed() {
        return keysPressed;
    }

    public void setKeyPressed(int keyCode, boolean pressed) {
        keysPressed[keyCode] = pressed;
    }

    public LaunchManager getLaunchManager() {
        return launchManager;
    }

    public void setLaunchManager(LaunchManager launchManager) {
        this.launchManager = launchManager;
    }
}
