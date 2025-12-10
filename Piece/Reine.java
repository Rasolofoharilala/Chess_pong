package Piece;

public class Reine extends Piece {
    public Reine(boolean isWhite, int pointDeVie) {
        super("Reine", pointDeVie, isWhite, isWhite ? "♕" : "♛",
              isWhite ? "Images/Reine_blanche.png" : "Images/Reine_noir.png");
    }
}