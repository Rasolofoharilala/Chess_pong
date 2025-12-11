package Piece;

public class Piece {
    public String nom;
    public int pointDeVie;
    protected boolean isWhite;  
    protected String symbole;   
    protected String imagePath; 

    public Piece(String nom, int pointDeVie, boolean isWhite, String symbole, String imagePath) {
        this.nom = nom;
        this.pointDeVie = Math.max(1, pointDeVie);
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
        // Assurer que les PV sont toujours > 0
        this.pointDeVie = Math.max(1, pointDeVie);
    }
    
    // Méthode pour infliger des dégâts à la pièce
    public void takeDamage(int damage) {
        this.pointDeVie = Math.max(1, this.pointDeVie - damage);
    }
    
    // Vérifier si la pièce est encore en vie
    public boolean isAlive() {
        return this.pointDeVie > 0;
    }
}