package Game;

import Affichage.Ball;
import Affichage.Paddle;
import Piece.Piece;

public class GameLogic {
    private GameState gameState;

    public GameLogic(GameState gameState) {
        this.gameState = gameState;
    }

    // Vérifier si un roi est mort et déterminer le gagnant
    public void checkGameOver() {
        boolean whiteKingAlive = false;
        boolean blackKingAlive = false;
        
        Piece[][] pieces = gameState.getPieces();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = pieces[row][col];
                if (piece != null && piece.getNom().equals("Roi")) {
                    if (piece.isWhite()) {
                        whiteKingAlive = true;
                    } else {
                        blackKingAlive = true;
                    }
                }
            }
        }
        
        if (!whiteKingAlive && blackKingAlive) {
            gameState.setGameOver(true);
            gameState.setWinner("Noir");
        } else if (!blackKingAlive && whiteKingAlive) {
            gameState.setGameOver(true);
            gameState.setWinner("Blanc");
        }
    }

    // Vérifier les collisions avec les paddles
    public void checkPaddleCollisions() {
        Ball ball = gameState.getBall();
        Paddle whitePaddle = gameState.getWhitePaddle();
        Paddle blackPaddle = gameState.getBlackPaddle();
        
        if (whitePaddle != null && whitePaddle.checkCollisionWithBall(ball)) {
            whitePaddle.bounceOffBall(ball);
            return;
        }
        
        if (blackPaddle != null && blackPaddle.checkCollisionWithBall(ball)) {
            blackPaddle.bounceOffBall(ball);
            return;
        }
    }

    // Vérifier les collisions avec les pièces
    public void checkPieceCollisions(int cellSize) {
        Ball ball = gameState.getBall();
        Piece[][] pieces = gameState.getPieces();
        
        // Vérifier d'abord les collisions avec les paddles
        checkPaddleCollisions();
        
        // Ensuite vérifier les collisions avec les pièces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = pieces[row][col];
                if (piece != null && ball.checkCollisionWithPiece(row, col, cellSize)) {
                    ball.bounceOff(row, col, cellSize);
                    piece.takeDamage(1);
                    
                    if (!piece.isAlive()) {
                        pieces[row][col] = null;
                        checkGameOver();
                    }
                    
                    return;
                }
            }
        }
    }

    // Gérer les entrées clavier
    public void handleInput(int cellSize, int cols) {
        Paddle whitePaddle = gameState.getWhitePaddle();
        Paddle blackPaddle = gameState.getBlackPaddle();
        boolean[] keysPressed = gameState.getKeysPressed();
        
        if (gameState.isGameOver()) return;
        
        // Calculer les limites
        int startCol = (8 - cols) / 2;
        int minX = startCol * cellSize;
        int maxX = minX + (cellSize * cols);
        
        // Paddle blanc
        if (keysPressed[java.awt.event.KeyEvent.VK_Q]) {
            whitePaddle.moveLeft(minX);
        }
        if (keysPressed[java.awt.event.KeyEvent.VK_D]) {
            whitePaddle.moveRight(maxX);
        }
        if (keysPressed[java.awt.event.KeyEvent.VK_A]) {
            whitePaddle.tiltLeft();
        } else if (keysPressed[java.awt.event.KeyEvent.VK_E]) {
            whitePaddle.tiltRight();
        } else {
            whitePaddle.stopTilting();
        }
        
        // Paddle noir
        if (keysPressed[java.awt.event.KeyEvent.VK_LEFT]) {
            blackPaddle.moveLeft(minX);
        }
        if (keysPressed[java.awt.event.KeyEvent.VK_RIGHT]) {
            blackPaddle.moveRight(maxX);
        }
        if (keysPressed[java.awt.event.KeyEvent.VK_UP]) {
            blackPaddle.tiltLeft();
        } else if (keysPressed[java.awt.event.KeyEvent.VK_DOWN]) {
            blackPaddle.tiltRight();
        } else {
            blackPaddle.stopTilting();
        }
    }

    // Mettre à jour l'état du jeu
    public void update(int cellSize, int cols) {
        if (!gameState.isGameOver()) {
            Ball ball = gameState.getBall();
            Paddle whitePaddle = gameState.getWhitePaddle();
            Paddle blackPaddle = gameState.getBlackPaddle();
            
            // Calculer les limites
            int startCol = (8 - cols) / 2;
            int minX = startCol * cellSize;
            int maxX = minX + (cellSize * cols);
            int maxY = cellSize * 8;
            
            // Mettre à jour la balle
            ball.update(minX, 0, maxX, maxY);
            checkPieceCollisions(cellSize);
            handleInput(cellSize, cols);
            
            // Mettre à jour l'inclinaison des paddles
            if (whitePaddle != null) {
                whitePaddle.updateTilt();
            }
            if (blackPaddle != null) {
                blackPaddle.updateTilt();
            }
        }
    }
}
