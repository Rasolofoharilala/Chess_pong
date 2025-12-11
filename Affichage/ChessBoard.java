package Affichage;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import Piece.*;
import Game.LaunchManager;

public class ChessBoard {

    private final int width;
    private final int length;
    private final int row;
    private final int col;
    private Piece[][] pieces;  // Grille pour stocker les pièces
    private Map<String, BufferedImage> imageCache;  // Cache pour les images
    private Map<String, Integer> maxHP;  // Points de vie maximum par type de pièce
    private Ball ball;  // La balle qui rebondit
    private Timer gameTimer;  // Timer pour l'animation
    private JPanel boardPanel;  // Référence au panel pour le rafraîchissement
    private boolean gameOver;  // Indicateur de fin de partie
    private String winner;  // Le gagnant ("Blanc" ou "Noir")
    private Paddle whitePaddle;  // Paddle du camp blanc (bas)
    private Paddle blackPaddle;  // Paddle du camp noir (haut)
    private boolean[] keysPressed = new boolean[256];  // Suivi des touches appuyées
    private LaunchManager launchManager;  // Gestionnaire de lancement

    // Couleurs officielles chess.com (extraites du site en 2025)
    private static final Color CASE_CLAIRE = new Color(240, 217, 181);  // beige clair
    private static final Color CASE_FONCEE = new Color(181, 136, 99);   // marron/orange foncé
    private static final Color BORDURE     = new Color(100, 100, 100);  // gris foncé discret

    public ChessBoard(int width, int length, int col) {
        this.width = width;
        this.length = length;
        this.row = 8;
        
        if ( 0 <= col && col <= 8) {
            this.col = col;
        } else {
            this.col = 8; 
        }
        
        this.pieces = new Piece[8][8];  // Grille 8x8 pour les pièces
        this.imageCache = new HashMap<>();  // Initialiser le cache d'images
        this.maxHP = new HashMap<>();  // Initialiser les HP max
        
        // La balle sera initialisée plus tard avec les bonnes coordonnées
        this.ball = null;
        
        // Les paddles seront initialisés plus tard
        this.whitePaddle = null;
        this.blackPaddle = null;
        
        // Initialiser l'état du jeu
        this.gameOver = false;
        this.winner = null;
    }
    
    // Méthode pour charger une image
    private BufferedImage loadImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }
        
        try {
            BufferedImage img = ImageIO.read(new File(path));
            imageCache.put(path, img);
            return img;
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'image: " + path);
            e.printStackTrace();
            return null;
        }
    }
    
    // Méthode pour placer une pièce sur l'échiquier
    public void setPiece(int row, int col, Piece piece) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            pieces[row][col] = piece;
        }
    }
    
    // Méthode pour obtenir une pièce
    public Piece getPiece(int row, int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            return pieces[row][col];
        }
        return null;
    }
    
    // Méthode pour initialiser les pièces en fonction du nombre de colonnes
    public void initializePieces(int pionsHP, int reineHP, int roiHP, int fousHP, int cavaliersHP, int toursHP) {
        // Sauvegarder les HP max pour chaque type de pièce
        maxHP.put("Pion", pionsHP);
        maxHP.put("Reine", reineHP);
        maxHP.put("Roi", roiHP);
        maxHP.put("Fou", fousHP);
        maxHP.put("Cavalier", cavaliersHP);
        maxHP.put("Tour", toursHP);
        
        // Placement des pièces selon le nombre de colonnes, centrées sur l'échiquier
        // col=2: Reine et Roi au centre (colonnes 3-4)
        // col=4: Fou, Reine, Roi, Fou au centre (colonnes 2-5)
        // col=6: Cavalier, Fou, Reine, Roi, Fou, Cavalier (colonnes 1-6)
        // col=8: Tour, Cavalier, Fou, Reine, Roi, Fou, Cavalier, Tour (colonnes 0-7)
        
        // Calculer la position de départ pour centrer les pièces
        int startCol = (8 - col) / 2;
        
        if (col >= 2) {
            // Centre : Reine et Roi
            int pos = startCol + (col / 2) - 1;  // Position centrale pour Reine et Roi
            
            // Pièces noires
            setPiece(0, pos, new Reine(false, reineHP));
            setPiece(0, pos + 1, new Roi(false, roiHP));
            setPiece(1, pos, new Pion(false, pionsHP));
            setPiece(1, pos + 1, new Pion(false, pionsHP));
            
            // Pièces blanches
            setPiece(7, pos, new Reine(true, reineHP));
            setPiece(7, pos + 1, new Roi(true, roiHP));
            setPiece(6, pos, new Pion(true, pionsHP));
            setPiece(6, pos + 1, new Pion(true, pionsHP));
        }
        
        if (col >= 4) {
            // Ajouter les Fous de part et d'autre
            int pos1 = startCol + (col / 2) - 2;  // Fou à gauche
            int pos2 = startCol + (col / 2) + 1;  // Fou à droite
            
            // Pièces noires
            setPiece(0, pos1, new Fou(false, fousHP));
            setPiece(0, pos2, new Fou(false, fousHP));
            setPiece(1, pos1, new Pion(false, pionsHP));
            setPiece(1, pos2, new Pion(false, pionsHP));
            
            // Pièces blanches
            setPiece(7, pos1, new Fou(true, fousHP));
            setPiece(7, pos2, new Fou(true, fousHP));
            setPiece(6, pos1, new Pion(true, pionsHP));
            setPiece(6, pos2, new Pion(true, pionsHP));
        }
        
        if (col >= 6) {
            // Ajouter les Cavaliers
            int pos1 = startCol + (col / 2) - 3;  // Cavalier à gauche
            int pos2 = startCol + (col / 2) + 2;  // Cavalier à droite
            
            // Pièces noires
            setPiece(0, pos1, new Cavalier(false, cavaliersHP));
            setPiece(0, pos2, new Cavalier(false, cavaliersHP));
            setPiece(1, pos1, new Pion(false, pionsHP));
            setPiece(1, pos2, new Pion(false, pionsHP));
            
            // Pièces blanches
            setPiece(7, pos1, new Cavalier(true, cavaliersHP));
            setPiece(7, pos2, new Cavalier(true, cavaliersHP));
            setPiece(6, pos1, new Pion(true, pionsHP));
            setPiece(6, pos2, new Pion(true, pionsHP));
        }
        
        if (col >= 8) {
            // Ajouter les Tours aux extrémités
            int pos1 = startCol;  // Tour à gauche
            int pos2 = startCol + col - 1;  // Tour à droite
            
            // Pièces noires
            setPiece(0, pos1, new Tour(false, toursHP));
            setPiece(0, pos2, new Tour(false, toursHP));
            setPiece(1, pos1, new Pion(false, pionsHP));
            setPiece(1, pos2, new Pion(false, pionsHP));
            
            // Pièces blanches
            setPiece(7, pos1, new Tour(true, toursHP));
            setPiece(7, pos2, new Tour(true, toursHP));
            setPiece(6, pos1, new Pion(true, pionsHP));
            setPiece(6, pos2, new Pion(true, pionsHP));
        }
    }
    
    // Méthode pour dessiner la barre de vie d'une pièce
    private void drawHealthBar(Graphics2D g2d, Piece piece, int x, int y, int imgSize, int cellSize) {
        int currentHP = piece.getPointDeVie();
        int maxHPValue = maxHP.getOrDefault(piece.getNom(), 100);
        
        // Position de la barre de vie (en haut de la pièce)
        int barWidth = imgSize;
        int barHeight = (int)(cellSize * 0.08);  // 8% de la taille de la case
        int barX = x;
        int barY = y - barHeight - 2;  // 2 pixels au-dessus de l'image
        
        // Calculer le pourcentage de vie
        float hpPercent = Math.max(0, Math.min(1, (float)currentHP / maxHPValue));
        
        // Couleur de fond (rouge foncé)
        g2d.setColor(new Color(139, 0, 0));
        g2d.fillRect(barX, barY, barWidth, barHeight);
        
        // Couleur de la barre selon le pourcentage
        Color hpColor;
        if (hpPercent > 0.6) {
            hpColor = new Color(0, 200, 0);  // Vert
        } else if (hpPercent > 0.3) {
            hpColor = new Color(255, 165, 0);  // Orange
        } else {
            hpColor = new Color(255, 0, 0);  // Rouge
        }
        
        g2d.setColor(hpColor);
        g2d.fillRect(barX, barY, (int)(barWidth * hpPercent), barHeight);
        
        // Bordure de la barre
        g2d.setColor(Color.BLACK);
        g2d.drawRect(barX, barY, barWidth, barHeight);
        
        // Afficher le texte HP
        g2d.setColor(Color.WHITE);
        Font oldFont = g2d.getFont();
        Font smallFont = new Font("Arial", Font.BOLD, (int)(cellSize * 0.12));
        g2d.setFont(smallFont);
        String hpText = currentHP + "/" + maxHPValue;
        FontMetrics fm = g2d.getFontMetrics();
        int textX = barX + (barWidth - fm.stringWidth(hpText)) / 2;
        int textY = barY + ((barHeight - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(hpText, textX, textY);
        g2d.setFont(oldFont);
    }

    // Méthode pour vérifier si un roi est mort et déterminer le gagnant
    private void checkGameOver() {
        boolean whiteKingAlive = false;
        boolean blackKingAlive = false;
        
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
        
        // Vérifier si un roi est mort
        if (!whiteKingAlive && blackKingAlive) {
            gameOver = true;
            winner = "Noir";
            gameTimer.stop();
        } else if (!blackKingAlive && whiteKingAlive) {
            gameOver = true;
            winner = "Blanc";
            gameTimer.stop();
        }
    }
    
    // Méthode pour vérifier les collisions avec les paddles
    private void checkPaddleCollisions() {
        if (whitePaddle != null && whitePaddle.checkCollisionWithBall(ball)) {
            whitePaddle.bounceOffBall(ball);
            return;
        }
        
        if (blackPaddle != null && blackPaddle.checkCollisionWithBall(ball)) {
            blackPaddle.bounceOffBall(ball);
            return;
        }
    }
    
    // Méthode pour vérifier les collisions et infliger des dégâts
    private void checkBallCollisions(int cellSize) {
        // Vérifier d'abord les collisions avec les paddles
        checkPaddleCollisions();
        
        // Ensuite vérifier les collisions avec les pièces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = pieces[row][col];
                if (piece != null && ball.checkCollisionWithPiece(row, col, cellSize)) {
                    // La balle rebondit sur la pièce
                    ball.bounceOff(row, col, cellSize);
                    
                    // Infliger 1 point de dégât à la pièce
                    piece.takeDamage(1);
                    
                    // Supprimer la pièce si elle n'a plus de points de vie
                    if (!piece.isAlive()) {
                        pieces[row][col] = null;
                        // Vérifier si le jeu est terminé
                        checkGameOver();
                    }
                    
                    // Sortir après la première collision détectée
                    return;
                }
            }
        }
    }

    public JPanel getPanel() {
        // Capture des valeurs de l'instance ChessBoard pour utilisation dans la classe anonyme
        final int rows = this.row;
        final int cols = this.col;
        
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int cellSize = Math.min(getWidth(), getHeight()) / 8;
                
                // Calculer le décalage pour centrer l'échiquier
                int startCol = (8 - cols) / 2;

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        // Position réelle sur la grille de 8 colonnes
                        int realCol = startCol + col;
                        
                        // Sur chess.com : case a1 (en bas à gauche) est foncée
                        // Donc si (row + col) est pair → foncé, impair → clair
                        if ((row + realCol) % 2 == 0) {
                            g.setColor(CASE_FONCEE);
                        } else {
                            g.setColor(CASE_CLAIRE);
                        }
                        g.fillRect(realCol * cellSize, row * cellSize, cellSize, cellSize);
                    }
                }

                // Dessiner les pièces
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        Piece piece = pieces[row][col];
                        if (piece != null) {
                            // Charger l'image de la pièce
                            BufferedImage img = loadImage(piece.getImagePath());
                            
                            if (img != null) {
                                // Calculer la position et la taille pour dessiner l'image
                                int padding = (int)(cellSize * 0.1);  // 10% de marge
                                int imgSize = cellSize - (2 * padding);
                                int x = col * cellSize + padding;
                                int y = row * cellSize + padding;
                                
                                // Dessiner l'image redimensionnée
                                g2d.drawImage(img, x, y, imgSize, imgSize, null);
                                
                                // Dessiner la barre de vie
                                ChessBoard.this.drawHealthBar(g2d, piece, x, y, imgSize, cellSize);
                            }
                        }
                    }
                }

                // Dessiner les paddles
                if (whitePaddle != null && !gameOver) {
                    whitePaddle.draw(g2d);
                }
                if (blackPaddle != null && !gameOver) {
                    blackPaddle.draw(g2d);
                }
                
                // Dessiner la balle
                if (ball != null && !gameOver) {
                    ball.draw(g2d);
                }

                // Bordure fine autour de l'échiquier (comme sur chess.com)
                g.setColor(BORDURE);
                int borderX = startCol * cellSize;
                int borderY = 0;
                int borderWidth = cellSize * cols;
                int borderHeight = cellSize * rows;
                g.drawRect(borderX, borderY, borderWidth - 1, borderHeight - 1);
                
                // Afficher l'interface de lancement si en mode lancement
                if (launchManager != null && launchManager.isLaunchMode()) {
                    launchManager.drawLaunchUI(g2d, getWidth(), getHeight());
                }
                
                // Afficher le message de victoire si le jeu est terminé
                if (gameOver && winner != null) {
                    // Fond semi-transparent
                    g2d.setColor(new Color(0, 0, 0, 180));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Texte de victoire
                    g2d.setColor(Color.WHITE);
                    Font titleFont = new Font("Arial", Font.BOLD, 60);
                    g2d.setFont(titleFont);
                    
                    String victoryText = "Le camp " + winner + " gagne !";
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(victoryText);
                    int textX = (getWidth() - textWidth) / 2;
                    int textY = getHeight() / 2;
                    
                    // Ombre du texte
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(victoryText, textX + 3, textY + 3);
                    
                    // Texte principal
                    if (winner.equals("Blanc")) {
                        g2d.setColor(new Color(255, 215, 0)); // Or pour blanc
                    } else {
                        g2d.setColor(new Color(192, 192, 192)); // Argent pour noir
                    }
                    g2d.drawString(victoryText, textX, textY);
                    
                    // Message additionnel
                    Font subFont = new Font("Arial", Font.PLAIN, 30);
                    g2d.setFont(subFont);
                    g2d.setColor(Color.WHITE);
                    String subText = "Le roi adverse a été détruit !";
                    fm = g2d.getFontMetrics();
                    textWidth = fm.stringWidth(subText);
                    textX = (getWidth() - textWidth) / 2;
                    g2d.drawString(subText, textX, textY + 60);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(width, length);
            }
        };
        
        // Ajouter le KeyListener pour contrôler les paddles
        boardPanel.setFocusable(true);
        boardPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (gameOver) return;
                keysPressed[e.getKeyCode()] = true;
            }
            
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                keysPressed[e.getKeyCode()] = false;
            }
        });
        
        // Démarrer le timer pour l'animation
        gameTimer = new Timer(16, e -> {  // ~60 FPS
            int cellSize = Math.min(boardPanel.getWidth(), boardPanel.getHeight()) / 8;
            int startCol = (8 - cols) / 2;
            
            // Calculer les limites de l'échiquier
            int minX = startCol * cellSize;
            int minY = 0;
            int maxX = minX + (cellSize * cols);
            int maxY = cellSize * rows;
            
            // Initialiser la balle au centre de l'échiquier si elle n'existe pas encore
            if (ball == null) {
                int centerX = minX + (maxX - minX) / 2;
                int centerY = minY + (maxY - minY) / 2;
                ball = new Ball(centerX, centerY, 10);
            }
            
            // Initialiser les paddles s'ils n'existent pas encore
            if (whitePaddle == null && blackPaddle == null) {
                int paddleWidth = cellSize; // 1 case de largeur
                int paddleHeight = cellSize / 4; // 1 case de hauteur
                
                // Paddle blanc - 1 case au-dessus des pions blancs (ligne 5)
                int whitePaddleY = 5 * cellSize + cellSize / 2;
                int centerX = minX + (maxX - minX) / 2;
                whitePaddle = new Paddle(centerX, whitePaddleY, paddleWidth, paddleHeight, true);
                
                // Paddle noir - 1 case au-dessus des pions noirs (ligne 2)
                int blackPaddleY = 2 * cellSize + cellSize / 2;
                blackPaddle = new Paddle(centerX, blackPaddleY, paddleWidth, paddleHeight, false);
            }
            
            // Initialiser le gestionnaire de lancement s'il n'existe pas encore
            if (launchManager == null) {
                launchManager = new LaunchManager();
                launchManager.setControlledPaddle(whitePaddle);
            }
            
            // Vérifier si on est en mode lancement
            if (launchManager.isLaunchMode() && !gameOver) {
                // Gestion des entrées en mode lancement
                int cellSize2 = Math.min(boardPanel.getWidth(), boardPanel.getHeight()) / 8;
                int startCol2 = (8 - cols) / 2;
                int minX2 = startCol2 * cellSize2;
                int maxX2 = minX2 + (cellSize2 * cols);
                
                // Déplacement du paddle blanc en mode lancement
                if (keysPressed[java.awt.event.KeyEvent.VK_Q]) {
                    whitePaddle.moveLeft(minX2);
                }
                if (keysPressed[java.awt.event.KeyEvent.VK_D]) {
                    whitePaddle.moveRight(maxX2);
                }
                
                // Ajustement de l'angle de lancement
                if (keysPressed[java.awt.event.KeyEvent.VK_A]) {
                    launchManager.increaseLaunchAngle();
                }
                if (keysPressed[java.awt.event.KeyEvent.VK_E]) {
                    launchManager.decreaseLaunchAngle();
                }
                
                // Réinitialiser l'angle
                if (keysPressed[java.awt.event.KeyEvent.VK_R]) {
                    launchManager.resetAngle();
                }
                
                // Mettre à jour la position de la balle sur le paddle
                launchManager.updateBallPosition(ball);
                
                // Lancer la balle
                if (keysPressed[java.awt.event.KeyEvent.VK_SPACE] || 
                    keysPressed[java.awt.event.KeyEvent.VK_ENTER]) {
                    launchManager.launchBall(ball, whitePaddle);
                    
                    // Réinitialiser les touches pour éviter les lancements multiples
                    keysPressed[java.awt.event.KeyEvent.VK_SPACE] = false;
                    keysPressed[java.awt.event.KeyEvent.VK_ENTER] = false;
                }
            } else if (!gameOver) {
                // Mode jeu normal
                int cellSize2 = Math.min(boardPanel.getWidth(), boardPanel.getHeight()) / 8;
                int startCol2 = (8 - cols) / 2;
                int minX2 = startCol2 * cellSize2;
                int maxX2 = minX2 + (cellSize2 * cols);
                
                // Paddle blanc
                if (keysPressed[java.awt.event.KeyEvent.VK_Q]) {
                    whitePaddle.moveLeft(minX2);
                }
                if (keysPressed[java.awt.event.KeyEvent.VK_D]) {
                    whitePaddle.moveRight(maxX2);
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
                    blackPaddle.moveLeft(minX2);
                }
                if (keysPressed[java.awt.event.KeyEvent.VK_RIGHT]) {
                    blackPaddle.moveRight(maxX2);
                }
                if (keysPressed[java.awt.event.KeyEvent.VK_UP]) {
                    blackPaddle.tiltLeft();
                } else if (keysPressed[java.awt.event.KeyEvent.VK_DOWN]) {
                    blackPaddle.tiltRight();
                } else {
                    blackPaddle.stopTilting();
                }
                
                // Mettre à jour l'inclinaison des paddles
                whitePaddle.updateTilt();
                blackPaddle.updateTilt();
                
                // Mettre à jour la balle avec les limites de l'échiquier
                ball.update(minX, minY, maxX, maxY);
                checkBallCollisions(cellSize);
            }
            boardPanel.repaint();
        });
        gameTimer.start();
        
        return boardPanel;
    }
}