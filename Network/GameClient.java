package Network;

import java.io.*;
import java.net.*;

/**
 * Client de jeu qui se connecte au serveur et synchronise l'état du jeu
 */
public class GameClient {
    private String serverIP;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private int playerId;
    private boolean connected;
    private ClientMessageListener listener;
    private Thread receiveThread;

    public GameClient(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.connected = false;
    }

    /**
     * Se connecte au serveur
     */
    public boolean connect() {
        try {
            socket = new Socket(serverIP, serverPort);
            
            // Initialiser les streams (l'ordre est important : OutputStream d'abord)
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            
            connected = true;
            System.out.println("Connecté au serveur : " + serverIP + ":" + serverPort);

            // Démarrer le thread de réception des messages
            receiveThread = new Thread(this::receiveMessages);
            receiveThread.setDaemon(true);
            receiveThread.start();

            return true;
        } catch (IOException e) {
            System.err.println("Erreur de connexion au serveur : " + e.getMessage());
            connected = false;
            return false;
        }
    }

    /**
     * Écoute les messages du serveur
     */
    private void receiveMessages() {
        try {
            while (connected) {
                try {
                    NetworkMessage message = (NetworkMessage) inputStream.readObject();
                    
                    // Si c'est un message de handshake, sauvegarder l'ID joueur
                    if (message.getType() == NetworkMessage.MessageType.HANDSHAKE) {
                        // Extraire l'ID du message
                        String msg = message.getMessage();
                        if (msg.contains("joueur")) {
                            String[] parts = msg.split("joueur ");
                            if (parts.length > 1) {
                                playerId = Integer.parseInt(parts[1]);
                                System.out.println("ID joueur assigné : " + playerId);
                            }
                        }
                    }
                    
                    // Notifier le listener
                    if (listener != null) {
                        listener.onMessageReceived(message);
                    }
                } catch (EOFException e) {
                    System.out.println("Déconnecté du serveur");
                    connected = false;
                    break;
                } catch (ClassNotFoundException e) {
                    System.err.println("Erreur de désérialisation : " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la réception des messages : " + e.getMessage());
            connected = false;
        }
    }

    /**
     * Envoie un message au serveur
     */
    public synchronized boolean sendMessage(NetworkMessage message) {
        try {
            if (outputStream != null && connected) {
                message.setPlayerId(playerId);
                outputStream.writeObject(message);
                outputStream.flush();
                return true;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du message : " + e.getMessage());
            connected = false;
        }
        return false;
    }

    /**
     * Envoie les inputs du joueur
     */
    public void sendPlayerInput(boolean[] keysPressed) {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.PLAYER_INPUT, playerId);
        message.setKeysPressed(keysPressed);
        sendMessage(message);
    }

    /**
     * Envoie l'état de la balle
     */
    public void sendBallUpdate(double x, double y, double vx, double vy) {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.BALL_UPDATE, playerId);
        message.setBallX(x);
        message.setBallY(y);
        message.setBallVelocityX(vx);
        message.setBallVelocityY(vy);
        sendMessage(message);
    }

    /**
     * Envoie l'état d'un paddle
     */
    public void sendPaddleUpdate(double x, double y, double angle) {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.PADDLE_UPDATE, playerId);
        message.setPaddleX(x);
        message.setPaddleY(y);
        message.setPaddleAngle(angle);
        sendMessage(message);
    }

    /**
     * Envoie que la balle a été lancée
     */
    public void sendBallLaunched(double x, double y, double vx, double vy) {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.BALL_LAUNCHED, playerId);
        message.setBallX(x);
        message.setBallY(y);
        message.setBallVelocityX(vx);
        message.setBallVelocityY(vy);
        sendMessage(message);
    }

    /**
     * Envoie que le jeu est terminé
     */
    public void sendGameOver(String winner) {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.GAME_OVER, playerId);
        message.setWinner(winner);
        message.setGameOver(true);
        sendMessage(message);
    }

    /**
     * Envoie les dégâts à une pièce
     */
    public void sendPieceDamaged(int row, int col, int damage) {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.PIECE_DAMAGED, playerId);
        message.setPieceRow(row);
        message.setPieceCol(col);
        message.setDamage(damage);
        sendMessage(message);
    }

    /**
     * Envoie qu'une pièce a été détruite
     */
    public void sendPieceDestroyed(int row, int col) {
        NetworkMessage message = new NetworkMessage(NetworkMessage.MessageType.PIECE_DESTROYED, playerId);
        message.setPieceRow(row);
        message.setPieceCol(col);
        sendMessage(message);
    }

    /**
     * Déconnecte du serveur
     */
    public void disconnect() {
        connected = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la déconnexion : " + e.getMessage());
        }
    }

    /**
     * Vérifie si le client est connecté
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Obtient l'ID du joueur
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Définit le listener pour les messages reçus
     */
    public void setMessageListener(ClientMessageListener listener) {
        this.listener = listener;
    }

    /**
     * Interface pour écouter les messages du serveur
     */
    public interface ClientMessageListener {
        void onMessageReceived(NetworkMessage message);
    }

    public static void main(String[] args) {
        // Test de connexion
        String serverIP = (args.length > 0) ? args[0] : "localhost";
        GameClient client = new GameClient(serverIP, 5555);
        
        if (client.connect()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            client.disconnect();
        }
    }
}
