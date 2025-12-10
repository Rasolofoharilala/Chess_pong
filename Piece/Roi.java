package Piece;

public class Roi extends Piece {
    public Roi(boolean isWhite, int pointDeVie) {
        super("Roi", pointDeVie, isWhite, isWhite ? "♔" : "♚",
              isWhite ? "Images/Roi_blanc.png" : "Images/Roi_noir.png");
    }
}
