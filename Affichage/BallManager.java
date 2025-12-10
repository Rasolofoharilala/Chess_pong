package Affichage;

public class BallManager {
    private Ball ball;

    public void initializeBall(int minX, int minY, int maxX, int maxY) {
        int centerX = minX + (maxX - minX) / 2;
        int centerY = minY + (maxY - minY) / 2;
        ball = new Ball(centerX, centerY, 10);
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void drawBall(java.awt.Graphics2D g2d) {
        if (ball != null) {
            ball.draw(g2d);
        }
    }
}
