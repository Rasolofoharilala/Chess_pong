package Piece;

public class Fou extends Piece {
    public Fou(boolean isWhite, int pointDeVie) {
        super("Fou", pointDeVie, isWhite, isWhite ? "♗" : "♝",
              isWhite ? "Images/Fou_blanc.png" : "Images/Fou_noir.png");
    }
    
}
