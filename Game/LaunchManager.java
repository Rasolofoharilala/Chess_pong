package Game;

import Affichage.Ball;
import Affichage.Paddle;
import java.awt.event.KeyEvent;

public class LaunchManager {
    private boolean launchMode;
    private Paddle controlledPaddle;
    private double launchAngle;
    private static final double MIN_ANGLE = -Math.PI / 2;  // -90°
    private static final double MAX_ANGLE = Math.PI / 2;   // 90°
    private static final double ANGLE_STEP = 0.05;
    private static final double LAUNCH_SPEED = 5.0;

    public LaunchManager() {
        this.launchMode = true;  // Mode lancement au démarrage
        this.launchAngle = 0;
    }

    public boolean isLaunchMode() {
        return launchMode;
    }

    public void setLaunchMode(boolean mode) {
        this.launchMode = mode;
    }

    public Paddle getControlledPaddle() {
        return controlledPaddle;
    }

    public void setControlledPaddle(Paddle paddle) {
        this.controlledPaddle = paddle;
    }

    public double getLaunchAngle() {
        return launchAngle;
    }

    // Augmenter l'angle de lancement
    public void increaseLaunchAngle() {
        launchAngle = Math.min(MAX_ANGLE, launchAngle + ANGLE_STEP);
    }

    // Diminuer l'angle de lancement
    public void decreaseLaunchAngle() {
        launchAngle = Math.max(MIN_ANGLE, launchAngle - ANGLE_STEP);
    }

    // Réinitialiser l'angle
    public void resetAngle() {
        launchAngle = 0;
    }

    // Mettre à jour la position de la balle sur la surface du paddle
    public void updateBallPosition(Ball ball) {
        if (ball == null || controlledPaddle == null) return;

        if (controlledPaddle.isWhite()) {
            // Paddle blanc : balle sur la surface supérieure du paddle
            ball.setX(controlledPaddle.getX());
            ball.setY(controlledPaddle.getY() - controlledPaddle.getHeight() / 2.0 - ball.getRadius());
        } else {
            // Paddle noir : balle sur la surface inférieure du paddle
            ball.setX(controlledPaddle.getX());
            ball.setY(controlledPaddle.getY() + controlledPaddle.getHeight() / 2.0 + ball.getRadius());
        }
    }

    // Lancer la balle
    public void launchBall(Ball ball, Paddle fromPaddle) {
        if (ball == null || fromPaddle == null) return;

        // La balle est déjà positionnée sur la surface du paddle
        // Il suffit de lui donner la vélocité
        
        if (fromPaddle.isWhite()) {
            // Paddle blanc : lancer vers le haut
            double velocityX = LAUNCH_SPEED * Math.sin(launchAngle);
            double velocityY = -Math.abs(LAUNCH_SPEED * Math.cos(launchAngle));
            
            ball.setVelocityX(velocityX);
            ball.setVelocityY(velocityY);
        } else {
            // Paddle noir : lancer vers le bas
            double velocityX = LAUNCH_SPEED * Math.sin(launchAngle);
            double velocityY = Math.abs(LAUNCH_SPEED * Math.cos(launchAngle));
            
            ball.setVelocityX(velocityX);
            ball.setVelocityY(velocityY);
        }

        // Quitter le mode lancement
        launchMode = false;
    }

    // Gérer l'input en mode lancement
    public void handleLaunchInput(boolean[] keysPressed, Paddle whitePaddle, Paddle blackPaddle, Ball ball) {
        if (!launchMode) return;

        // Déterminer quel paddle contrôle
        if (controlledPaddle == null) {
            // Par défaut, c'est le paddle blanc qui contrôle le lancement
            controlledPaddle = whitePaddle;
        }

        // Mouvement du paddle
        int minX = 0;  // À calculer selon l'échiquier
        int maxX = 800; // À calculer selon l'échiquier

        if (keysPressed[KeyEvent.VK_Q] || keysPressed[KeyEvent.VK_LEFT]) {
            controlledPaddle.moveLeft(minX);
        }
        if (keysPressed[KeyEvent.VK_D] || keysPressed[KeyEvent.VK_RIGHT]) {
            controlledPaddle.moveRight(maxX);
        }

        // Contrôle de l'angle de lancement
        if (keysPressed[KeyEvent.VK_UP] || keysPressed[KeyEvent.VK_W]) {
            increaseLaunchAngle();
        }
        if (keysPressed[KeyEvent.VK_DOWN] || keysPressed[KeyEvent.VK_S]) {
            decreaseLaunchAngle();
        }

        // Lancer la balle avec SPACE ou ENTER
        if (keysPressed[KeyEvent.VK_SPACE] || keysPressed[KeyEvent.VK_ENTER]) {
            launchBall(ball, controlledPaddle);
            keysPressed[KeyEvent.VK_SPACE] = false;
            keysPressed[KeyEvent.VK_ENTER] = false;
        }

        // Réinitialiser l'angle avec R
        if (keysPressed[KeyEvent.VK_R]) {
            resetAngle();
            keysPressed[KeyEvent.VK_R] = false;
        }
    }

    public void drawLaunchUI(java.awt.Graphics2D g2d, int width, int height) {
        if (!launchMode) return;

        // Afficher le texte de mode lancement
        g2d.setColor(java.awt.Color.WHITE);
        java.awt.Font titleFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 30);
        g2d.setFont(titleFont);
        g2d.drawString("MODE LANCEMENT", width / 2 - 150, 50);

        // Afficher l'angle de lancement
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 20));
        double angleDegrees = Math.toDegrees(launchAngle);
        g2d.drawString(String.format("Angle: %.1f°", angleDegrees), width / 2 - 80, 90);

        // Afficher les contrôles
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        g2d.drawString("Flèches/WASD: Déplacer", 20, height - 100);
        g2d.drawString("Haut/Bas: Ajuster angle", 20, height - 75);
        g2d.drawString("SPACE: Lancer", 20, height - 50);
        g2d.drawString("R: Réinitialiser angle", 20, height - 25);

        // Afficher une barre d'angle visuelle
        int barWidth = 200;
        int barHeight = 30;
        int barX = width / 2 - barWidth / 2;
        int barY = 130;

        // Fond de la barre
        g2d.setColor(new java.awt.Color(50, 50, 50));
        g2d.fillRect(barX, barY, barWidth, barHeight);

        // Bordure
        g2d.setColor(java.awt.Color.WHITE);
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawRect(barX, barY, barWidth, barHeight);

        // Indicateur d'angle
        int indicatorX = (int)(barX + barWidth / 2 + (launchAngle / MAX_ANGLE) * barWidth / 2);
        g2d.setColor(new java.awt.Color(0, 255, 0));
        g2d.fillRect(indicatorX - 2, barY, 4, barHeight);
    }
}
