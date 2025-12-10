import Affichage.Fenetre;
import Affichage.ChessBoard;
import Affichage.InputDonnee;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Lancer le formulaire de configuration dans le thread Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                InputDonnee inputForm = new InputDonnee();
                
                // Définir le listener pour recevoir la configuration
                inputForm.setConfigurationListener(new InputDonnee.ConfigurationListener() {
                    @Override
                    public void onConfigurationConfirmed(int nbPieces, int pionsHP, int reineHP, 
                                                          int roiHP, int fousHP, int cavaliersHP, int toursHP) {
                        // Créer l'échiquier avec la configuration
                        ChessBoard board = new ChessBoard(600, 600, nbPieces);
                        
                        // Initialiser les pièces avec les points de vie personnalisés
                        board.initializePieces(pionsHP, reineHP, roiHP, fousHP, cavaliersHP, toursHP);
                        
                        // Afficher la fenêtre de jeu
                        Fenetre fenetre = new Fenetre("Jeu d'échecs", 800, 700, board);
                        fenetre.affichageFenetre();
                    }
                });
                
                inputForm.setVisible(true);
            }
        });
    }
}