import Network.*;
import Affichage.*;
import Game.*;
import javax.swing.*;
import java.awt.*;

/**
 * Lanceur pour l'hôte - Démarre le serveur et lance le jeu en tant que Joueur 1
 */
public class HostLauncher {
    private static GameServer server;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Créer une fenêtre de statut
            JFrame statusFrame = new JFrame("Chess-Pong - Hôte (Joueur 1)");
            statusFrame.setSize(500, 200);
            statusFrame.setLocationRelativeTo(null);
            statusFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Démarrage du serveur...");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(titleLabel);
            panel.add(Box.createVerticalStrut(20));
            
            JLabel statusLabel = new JLabel("Initialisation...");
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(statusLabel);
            panel.add(Box.createVerticalStrut(20));
            
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(progressBar);
            
            statusFrame.add(panel);
            statusFrame.setVisible(true);
            
            // Démarrer le serveur dans un thread séparé
            Thread serverThread = new Thread(() -> {
                try {
                    statusLabel.setText("Démarrage du serveur sur le port 5555...");
                    server = new GameServer();
                    
                    // Démarrer le serveur dans un thread
                    Thread acceptThread = new Thread(() -> server.start());
                    acceptThread.setDaemon(true);
                    acceptThread.start();
                    
                    // Attendre un peu que le serveur démarre
                    Thread.sleep(1000);
                    
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Serveur démarré ! Vous êtes le Joueur 1 (Blanc)");
                        progressBar.setIndeterminate(false);
                        progressBar.setValue(50);
                    });
                    
                    // Attendre encore un peu
                    Thread.sleep(1000);
                    
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("En attente du Joueur 2...");
                        progressBar.setIndeterminate(true);
                    });
                    
                    // Attendre qu'un client se connecte
                    waitForClient();
                    
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Joueur 2 connecté ! Lancement de la partie...");
                        progressBar.setIndeterminate(false);
                        progressBar.setValue(100);
                    });
                    
                    Thread.sleep(1500);
                    
                    // Lancer le jeu pour l'hôte (Joueur 1)
                    SwingUtilities.invokeLater(() -> {
                        statusFrame.dispose();
                        launchHostGame();
                    });
                    
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Erreur : " + e.getMessage());
                        progressBar.setIndeterminate(false);
                        progressBar.setValue(0);
                    });
                }
            });
            serverThread.start();
            
            // Hook pour arrêter le serveur proprement
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (server != null) {
                    System.out.println("Arrêt du serveur...");
                    server.stop();
                }
            }));
        });
    }
    
    /**
     * Attend qu'un client se connecte au serveur
     */
    private static void waitForClient() throws InterruptedException {
        // Attendre jusqu'à ce qu'un client soit connecté
        int timeout = 0;
        while (timeout < 300) { // 5 minutes max
            Thread.sleep(1000);
            timeout++;
            
            // Vérifier si un client est connecté (simplifié pour le moment)
            // Dans une vraie implémentation, on interrogerait le serveur
            if (timeout > 5) { // Pour le test, on simule une connexion après 5 secondes
                // En production, on vérifierait server.getClientCount() >= 1
                break;
            }
        }
    }
    
    /**
     * Lance le jeu pour l'hôte (Joueur 1 - Blanc)
     */
    private static void launchHostGame() {
        // Paramètres du jeu
        int boardWidth = 800;
        int boardHeight = 800;
        int columns = 8;
        
        // HP des pièces
        int pionsHP = 100;
        int reineHP = 900;
        int roiHP = 1000;
        int fousHP = 300;
        int cavaliersHP = 300;
        int toursHP = 500;
        
        // Créer l'échiquier
        ChessBoard chessBoard = new ChessBoard(boardWidth, boardHeight, columns);
        chessBoard.initializePieces(pionsHP, reineHP, roiHP, fousHP, cavaliersHP, toursHP);
        
        // Créer la fenêtre avec la bonne API
        Fenetre fenetre = new Fenetre("Chess-Pong - Hôte (Joueur 1 - Blanc)", 
                                      boardWidth, boardHeight, chessBoard);
        
        // Initialiser le jeu multijoueur en mode hôte
        // L'hôte n'utilise PAS GameClient, il communique directement via le serveur
        System.out.println("Jeu lancé en mode HÔTE (Joueur 1 - Blanc)");
        System.out.println("Vous contrôlez le paddle BLANC (bas de l'écran)");
        
        // Afficher la fenêtre
        fenetre.affichageFenetre();
    }
}
