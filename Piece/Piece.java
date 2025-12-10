package Piece;

public class Piece {
    public String nom;
    public int pointDeVie;
    protected boolean isWhite;  // true = blanc, false = noir
    protected String symbole;   // Symbole Unicode de la pièce
    protected String imagePath; // Chemin vers l'image de la pièce

    public Piece(String nom, int pointDeVie, boolean isWhite, String symbole, String imagePath) {
        this.nom = nom;
        this.pointDeVie = pointDeVie;
        this.isWhite = isWhite;
        this.symbole = symbole;
        this.imagePath = imagePath;
    }

    public String getNom() {
        return nom;
    }

    public int getPointDeVie() {
        return pointDeVie;
    }
    
    public boolean isWhite() {
        return isWhite;
    }
    
    public String getSymbole() {
        return symbole;
    }
    
    public String getImagePath() {
        return imagePath;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPointDeVie(int pointDeVie) {
        this.pointDeVie = pointDeVie;
    }
}