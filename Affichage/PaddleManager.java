package Affichage;

public class PaddleManager {
    private Paddle whitePaddle;
    private Paddle blackPaddle;

    public void initializePaddles(int minX, int maxX, int cellSize, int cols) {
        int paddleWidth = cellSize;  // 1 case de largeur
        int paddleHeight = cellSize / 2;  // Garder la hauteur d'avant
        
        // Paddle blanc - 1 case au-dessus des pions blancs
        int whitePaddleY = 5 * cellSize + cellSize / 2;
        int centerX = minX + (maxX - minX) / 2;
        whitePaddle = new Paddle(centerX, whitePaddleY, paddleWidth, paddleHeight, true);
        
        // Paddle noir - 1 case au-dessus des pions noirs
        int blackPaddleY = 2 * cellSize + cellSize / 2;
        blackPaddle = new Paddle(centerX, blackPaddleY, paddleWidth, paddleHeight, false);
    }

    public Paddle getWhitePaddle() {
        return whitePaddle;
    }

    public Paddle getBlackPaddle() {
        return blackPaddle;
    }

    public void drawPaddles(java.awt.Graphics2D g2d) {
        if (whitePaddle != null) {
            whitePaddle.draw(g2d);
        }
        if (blackPaddle != null) {
            blackPaddle.draw(g2d);
        }
    }
}
