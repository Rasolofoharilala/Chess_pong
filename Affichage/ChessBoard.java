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

public class ChessBoard {

    private final int width;
    private final int length;
    private final int row;
    private final int col;
    private Piece[][] pieces;  // Grille pour stocker les pièces
    private Map<String, BufferedImage> imageCache;  // Cache pour les images
    private Map<String, Integer> maxHP;  // Points de vie maximum par type de pièce

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

    public JPanel getPanel() {
        // Capture des valeurs de l'instance ChessBoard pour utilisation dans la classe anonyme
        final int rows = this.row;
        final int cols = this.col;
        
        return new JPanel() {
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

                // Bordure fine autour de l'échiquier (comme sur chess.com)
                g.setColor(BORDURE);
                int borderX = startCol * cellSize;
                int borderY = 0;
                int borderWidth = cellSize * cols;
                int borderHeight = cellSize * rows;
                g.drawRect(borderX, borderY, borderWidth - 1, borderHeight - 1);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(width, length);
            }
        };
    }
}