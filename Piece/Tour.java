package Piece;

public class Tour extends Piece {
    public Tour(boolean isWhite, int pointDeVie) {
        super("Tour", pointDeVie, isWhite, isWhite ? "♖" : "♜", 
              isWhite ? "Images/Tour_blanche.png" : "Images/Tour_noir.png");
    }
}
