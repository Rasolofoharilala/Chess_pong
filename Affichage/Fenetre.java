package Affichage;

import javax.swing.*;

public class Fenetre {

    private String title;
    private int width;
    private int length;
    private ChessBoard chessBoard;  // ton échiquier

    public Fenetre(String title, int width, int length, ChessBoard chessBoard) {
        this.title = title;
        this.width = width;
        this.length = length;
        this.chessBoard = chessBoard;
    }

    public void affichageFenetre() {
        JFrame frame = new JFrame(this.title);
        frame.setSize(this.width, this.length);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.add(chessBoard.getPanel());

        frame.setVisible(true);
    }
}