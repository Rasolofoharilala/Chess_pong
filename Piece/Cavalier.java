package Piece;

public class Cavalier extends Piece {
    public Cavalier(boolean isWhite, int pointDeVie) {
        super("Cavalier", pointDeVie, isWhite, isWhite ? "♘" : "♞",
              isWhite ? "Images/Cavalier_blanche.png" : "Images/Cavalier_noir.png");
    }
}
