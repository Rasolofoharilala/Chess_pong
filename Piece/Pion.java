package Piece;

public class Pion extends Piece {
    public Pion(boolean isWhite, int pointDeVie) {
        super("Pion", pointDeVie, isWhite, isWhite ? "♙" : "♟",
              isWhite ? "Images/Pion_blanche.png" : "Images/Pion_noir.png");
    }
}
