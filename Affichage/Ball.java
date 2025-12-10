package Affichage;

import java.awt.*;

public class Ball {
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private int radius;
    private Color color;
    private static final double SPEED = 3.0;

    public Ball(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = Color.RED;
        
        // Initialiser la vélocité avec un angle aléatoire
        double angle = Math.random() * 2 * Math.PI;
        this.velocityX = SPEED * Math.cos(angle);
        this.velocityY = SPEED * Math.sin(angle);
    }

    public void update(int minX, int minY, int maxX, int maxY) {
        x += velocityX;
        y += velocityY;

        // Rebond sur les bords gauche et droite de l'échiquier
        if (x - radius <= minX || x + radius >= maxX) {
            velocityX = -velocityX;
            x = Math.max(minX + radius, Math.min(maxX - radius, x));
        }

        // Rebond sur les bords haut et bas de l'échiquier
        if (y - radius <= minY || y + radius >= maxY) {
            velocityY = -velocityY;
            y = Math.max(minY + radius, Math.min(maxY - radius, y));
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
        
        // Bordure noire pour mieux voir la balle
        g2d.setColor(Color.BLACK);
        g2d.drawOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
    }

    // Vérifier la collision avec une pièce basée sur la case exacte
    public boolean checkCollisionWithPiece(int pieceRow, int pieceCol, int cellSize) {
        // Calculer les limites de la case de la pièce
        double caseLeft = pieceCol * cellSize;
        double caseRight = (pieceCol + 1) * cellSize;
        double caseTop = pieceRow * cellSize;
        double caseBottom = (pieceRow + 1) * cellSize;
        
        // Vérifier si le centre de la balle est dans la case ou si la balle touche la case
        boolean ballInOrTouchesCase = 
            (x + radius > caseLeft && x - radius < caseRight) &&
            (y + radius > caseTop && y - radius < caseBottom);
        
        return ballInOrTouchesCase;
    }

    // Inverser la direction après collision en fonction de la face touchée
    public void bounceOff(int pieceRow, int pieceCol, int cellSize) {
        // Calculer les limites de la case
        double caseLeft = pieceCol * cellSize;
        double caseRight = (pieceCol + 1) * cellSize;
        double caseTop = pieceRow * cellSize;
        double caseBottom = (pieceRow + 1) * cellSize;
        
        // Calculer le centre de la case
        double centerX = (caseLeft + caseRight) / 2.0;
        double centerY = (caseTop + caseBottom) / 2.0;
        
        // Calculer les distances aux bords
        double distLeft = Math.abs(x - caseLeft);
        double distRight = Math.abs(x - caseRight);
        double distTop = Math.abs(y - caseTop);
        double distBottom = Math.abs(y - caseBottom);
        
        // Trouver le côté le plus proche pour déterminer la direction du rebond
        double minDist = Math.min(Math.min(distLeft, distRight), Math.min(distTop, distBottom));
        
        if (minDist == distLeft || minDist == distRight) {
            // Collision horizontale (gauche ou droite)
            velocityX = -velocityX;
            // Repositionner la balle
            if (x < centerX) {
                x = caseLeft - radius - 1;
            } else {
                x = caseRight + radius + 1;
            }
        } else {
            // Collision verticale (haut ou bas)
            velocityY = -velocityY;
            // Repositionner la balle
            if (y < centerY) {
                y = caseTop - radius - 1;
            } else {
                y = caseBottom + radius + 1;
            }
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
}
