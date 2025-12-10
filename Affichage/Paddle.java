package Affichage;

import java.awt.*;

public class Paddle {
    private double x;  // Position centrale du paddle
    private double y;  // Position verticale (fixe)
    private int width;  // Largeur du paddle (1/4 de la largeur de l'échiquier)
    private int height; // Hauteur du paddle (1 case)
    private double angle; // Angle d'inclinaison en radians (-45° à +45°)
    private boolean isWhite; // true = paddle blanc (bas), false = paddle noir (haut)
    private boolean tiltingLeft;  // État toggle pour inclinaison gauche
    private boolean tiltingRight; // État toggle pour inclinaison droite
    private static final double MAX_ANGLE = Math.PI / 4; // 45 degrés
    private static final double ANGLE_STEP = 0.1; // Pas d'inclinaison à chaque frame
    private static final double MOVE_SPEED = 10.0; // Vitesse de déplacement

    public Paddle(int x, int y, int width, int height, boolean isWhite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = 0; // Horizontal par défaut
        this.isWhite = isWhite;
        this.tiltingLeft = false;
        this.tiltingRight = false;
    }

    public void moveLeft(int minX) {
        x -= MOVE_SPEED;
        if (x - width / 2 < minX) {
            x = minX + width / 2;
        }
    }

    public void moveRight(int maxX) {
        x += MOVE_SPEED;
        if (x + width / 2 > maxX) {
            x = maxX - width / 2;
        }
    }

    public void tiltLeft() {
        tiltingLeft = true;
        tiltingRight = false;
    }

    public void tiltRight() {
        tiltingLeft = false;
        tiltingRight = true;
    }

    public void stopTilting() {
        tiltingLeft = false;
        tiltingRight = false;
    }

    public void updateTilt() {
        // Mettre à jour l'angle en fonction de l'état du toggle
        if (tiltingLeft) {
            angle = Math.max(-MAX_ANGLE, angle - ANGLE_STEP);
        } else if (tiltingRight) {
            angle = Math.min(MAX_ANGLE, angle + ANGLE_STEP);
        } else {
            // Retour progressif à 0 quand aucune touche n'est enfoncée
            if (Math.abs(angle) > 0.02) {
                angle *= 0.85;
            } else {
                angle = 0;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        // Sauvegarder la transformation actuelle
        Graphics2D g2 = (Graphics2D) g2d.create();
        
        // Activer l'antialiasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Se déplacer au centre du paddle
        g2.translate(x, y);
        
        // Appliquer la rotation
        g2.rotate(angle);
        
        // Dessiner le paddle centré sur l'origine
        if (isWhite) {
            g2.setColor(new Color(240, 240, 240));
        } else {
            g2.setColor(new Color(40, 40, 40));
        }
        
        // Rectangle du paddle
        g2.fillRoundRect(-width / 2, -height / 2, width, height, 10, 10);
        
        // Bordure
        g2.setColor(isWhite ? Color.BLACK : Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(-width / 2, -height / 2, width, height, 10, 10);
        
        // Restaurer la transformation
        g2.dispose();
    }

    // Vérifier la collision avec la balle
    public boolean checkCollisionWithBall(Ball ball) {
        double ballX = ball.getX();
        double ballY = ball.getY();
        int ballRadius = ball.getRadius();
        
        // Transformer les coordonnées de la balle dans le repère du paddle
        double dx = ballX - x;
        double dy = ballY - y;
        
        // Rotation inverse pour ramener dans le repère du paddle horizontal
        double cosA = Math.cos(-angle);
        double sinA = Math.sin(-angle);
        double localX = dx * cosA - dy * sinA;
        double localY = dx * sinA + dy * cosA;
        
        // Vérifier si la balle touche le rectangle du paddle
        double halfWidth = width / 2.0;
        double halfHeight = height / 2.0;
        
        // Trouver le point le plus proche du rectangle
        double closestX = Math.max(-halfWidth, Math.min(halfWidth, localX));
        double closestY = Math.max(-halfHeight, Math.min(halfHeight, localY));
        
        // Calculer la distance entre le centre de la balle et le point le plus proche
        double distX = localX - closestX;
        double distY = localY - closestY;
        double distance = Math.sqrt(distX * distX + distY * distY);
        
        return distance < ballRadius;
    }

    // Modifier la direction de la balle en fonction de l'inclinaison du paddle
    public void bounceOffBall(Ball ball) {
        double ballX = ball.getX();
        
        // Calculer la position relative de la balle sur le paddle (-1 à 1)
        double relativeX = (ballX - x) / (width / 2.0);
        relativeX = Math.max(-1, Math.min(1, relativeX));
        
        // Calculer la nouvelle direction en fonction de l'inclinaison et de la position
        double bounceAngle = angle + relativeX * 0.3; // Ajout de l'effet de position
        
        // Récupérer la vitesse actuelle
        double currentSpeed = Math.sqrt(ball.getVelocityX() * ball.getVelocityX() + 
                                       ball.getVelocityY() * ball.getVelocityY());
        
        // Calculer la nouvelle direction
        if (isWhite) {
            // Paddle blanc (bas) - la balle repart vers le haut
            ball.setVelocityX(currentSpeed * Math.sin(bounceAngle));
            ball.setVelocityY(-Math.abs(currentSpeed * Math.cos(bounceAngle)));
        } else {
            // Paddle noir (haut) - la balle repart vers le bas
            ball.setVelocityX(currentSpeed * Math.sin(bounceAngle));
            ball.setVelocityY(Math.abs(currentSpeed * Math.cos(bounceAngle)));
        }
        
        // Repositionner la balle pour éviter les collisions multiples
        if (isWhite) {
            ball.setY(y - height / 2.0 - ball.getRadius() - 2);
        } else {
            ball.setY(y + height / 2.0 + ball.getRadius() + 2);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getAngle() {
        return angle;
    }

    public boolean isWhite() {
        return isWhite;
    }
}
