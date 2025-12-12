import Network.*;
import javax.swing.*;

/**
 * Lanceur du serveur de jeu Chess-Pong
 */
public class ServerLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess-Pong - Serveur de Jeu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));

            JLabel titleLabel = new JLabel("Serveur de Jeu Chess-Pong");
            titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
            panel.add(javax.swing.Box.createVerticalStrut(20));
            panel.add(titleLabel);
            panel.add(javax.swing.Box.createVerticalStrut(20));

            JLabel statusLabel = new JLabel("En attente de joueurs...");
            statusLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
            panel.add(statusLabel);
            panel.add(javax.swing.Box.createVerticalStrut(20));

            JTextArea logArea = new JTextArea(15, 50);
            logArea.setEditable(false);
            logArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(logArea);
            panel.add(scrollPane);
            panel.add(javax.swing.Box.createVerticalStrut(10));

            JButton startButton = new JButton("Démarrer le Serveur");
            startButton.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
            startButton.addActionListener(e -> {
                logArea.append("Démarrage du serveur...\n");
                startButton.setEnabled(false);
                
                // Lancer le serveur dans un thread séparé
                Thread serverThread = new Thread(() -> {
                    GameServer server = new GameServer();
                    server.start();
                });
                serverThread.setDaemon(true);
                serverThread.start();
            });
            panel.add(startButton);

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
