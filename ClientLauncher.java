import Network.GameClient;
import Affichage.*;
import javax.swing.*;
import java.awt.*;

/**
 * Lanceur du client de jeu Chess-Pong (Joueur 2)
 */
public class ClientLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess-Pong - Client (Joueur 2)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel("Client de Jeu Chess-Pong");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(titleLabel);
            panel.add(Box.createVerticalStrut(20));

            // Champ pour l'IP du serveur
            JPanel ipPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel ipLabel = new JLabel("IP du serveur :");
            JTextField ipField = new JTextField("localhost", 15);
            ipPanel.add(ipLabel);
            ipPanel.add(ipField);
            panel.add(ipPanel);
            panel.add(Box.createVerticalStrut(10));

            // Champ pour le port
            JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel portLabel = new JLabel("Port :");
            JTextField portField = new JTextField("5555", 10);
            portPanel.add(portLabel);
            portPanel.add(portField);
            panel.add(portPanel);
            panel.add(Box.createVerticalStrut(20));

            JLabel statusLabel = new JLabel("En attente de connexion...");
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(statusLabel);
            panel.add(Box.createVerticalStrut(20));

            JTextArea logArea = new JTextArea(10, 40);
            logArea.setEditable(false);
            logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(logArea);
            panel.add(scrollPane);
            panel.add(Box.createVerticalStrut(10));

            JButton connectButton = new JButton("Se connecter et Jouer");
            connectButton.setFont(new Font("Arial", Font.PLAIN, 14));
            connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            connectButton.addActionListener(e -> {
                String serverIP = ipField.getText();
                final int port;
                try {
                    port = Integer.parseInt(portField.getText());
                } catch (NumberFormatException ex) {
                    logArea.append("Port invalide!\n");
                    return;
                }

                logArea.append("Connexion à " + serverIP + ":" + port + "...\n");
                connectButton.setEnabled(false);
                ipField.setEnabled(false);
                portField.setEnabled(false);

                // Lancer la connexion dans un thread séparé
                Thread connectionThread = new Thread(() -> {
                    GameClient client = new GameClient(serverIP, port);
                    if (client.connect()) {
                        SwingUtilities.invokeLater(() -> {
                            logArea.append("✓ Connecté au serveur!\n");
                            logArea.append("Vous êtes le Joueur 2 (Noir)\n");
                            logArea.append("Lancement du jeu...\n");
                        });
                        
                        // Attendre un peu pour lire les messages
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        
                        // Lancer le jeu
                        SwingUtilities.invokeLater(() -> {
                            frame.dispose();
                            launchClientGame(client);
                        });
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            logArea.append("✗ Erreur de connexion au serveur\n");
                            connectButton.setEnabled(true);
                            ipField.setEnabled(true);
                            portField.setEnabled(true);
                        });
                    }
                });
                connectionThread.setDaemon(true);
                connectionThread.start();
            });
            panel.add(connectButton);

            frame.add(panel);
            frame.setVisible(true);
        });
    }
    
    /**
     * Lance le jeu pour le client (Joueur 2 - Noir)
     */
    private static void launchClientGame(GameClient client) {
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
        
        // Créer la fenêtre
        Fenetre fenetre = new Fenetre("Chess-Pong - Client (Joueur 2 - Noir)", 
                                      boardWidth, boardHeight, chessBoard);
        
        // Initialiser le jeu multijoueur côté client
        System.out.println("Jeu lancé en mode CLIENT (Joueur 2 - Noir)");
        System.out.println("Vous contrôlez le paddle NOIR (haut de l'écran)");
        System.out.println("Utilisez les flèches pour jouer");
        
        // TODO: Intégrer la synchronisation réseau avec le client
        // Pour l'instant, le jeu fonctionne en local
        
        // Afficher la fenêtre
        fenetre.affichageFenetre();
    }
}
