package Network;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Serveur de jeu pour Chess-Pong en mode 2 joueurs
 * Le serveur héberge le joueur 1 (hôte) et attend un client (joueur 2)
 */
public class GameServer {
    private static final int PORT = 5555;
    private static final int MAX_CLIENTS = 1; // Un seul client, l'hôte est le joueur 1
    
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private boolean isRunning;
    private ExecutorService threadPool;
    private ServerGameState gameState;
    
    public GameServer() {
        this.clients = new CopyOnWriteArrayList<>();
        this.isRunning = false;
        this.threadPool = Executors.newCachedThreadPool();
        this.gameState = new ServerGameState();
    }
    
    /**
     * Démarre le serveur
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            System.out.println("Serveur démarré sur le port " + PORT);
            System.out.println("Joueur 1 (Hôte) prêt. En attente du joueur 2...");
            
            acceptClients();
        } catch (IOException e) {
            System.err.println("Erreur lors du démarrage du serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Accepte les connexions des clients
     */
    private void acceptClients() {
        while (isRunning && clients.size() < MAX_CLIENTS) {
            try {
                Socket clientSocket = serverSocket.accept();
                
                if (clients.size() < MAX_CLIENTS) {
                    // Assigner l'ID 2 au client (l'hôte est le joueur 1)
                    int playerId = 2;
                    ClientHandler handler = new ClientHandler(clientSocket, playerId, this);
                    clients.add(handler);
                    threadPool.execute(handler);
                    
                    System.out.println("Joueur " + playerId + " connecté depuis " + 
                                     clientSocket.getInetAddress().getHostAddress());
                    
                    // Envoyer le message de bienvenue
                    handler.sendMessage(new NetworkMessage(NetworkMessage.MessageType.HANDSHAKE, 0) {{
                        setMessage("Bienvenue joueur " + playerId);
                    }});
                    
                    // Si on a tous les joueurs, démarrer la partie
                    if (clients.size() == MAX_CLIENTS) {
                        System.out.println("Tous les joueurs sont connectés ! La partie peut commencer.");
                        broadcastGameStart();
                    }
                } else {
                    // Refuser la connexion si le serveur est plein
                    clientSocket.close();
                    System.out.println("Connexion refusée : serveur plein");
                }
            } catch (IOException e) {
                if (isRunning) {
                    System.err.println("Erreur lors de l'acceptation d'un client : " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Diffuse un message à tous les clients
     */
    public void broadcast(NetworkMessage message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
    
    /**
     * Diffuse un message à tous les clients sauf l'émetteur
     */
    public void broadcastExcept(NetworkMessage message, int senderPlayerId) {
        for (ClientHandler client : clients) {
            if (client.getPlayerId() != senderPlayerId) {
                client.sendMessage(message);
            }
        }
    }
    
    /**
     * Notifie tous les joueurs que la partie commence
     */
    private void broadcastGameStart() {
        NetworkMessage startMsg = new NetworkMessage(NetworkMessage.MessageType.HANDSHAKE, 0);
        startMsg.setMessage("La partie commence !");
        broadcast(startMsg);
    }
    
    /**
     * Traite un message reçu d'un client
     */
    public void handleClientMessage(NetworkMessage message, int playerId) {
        if (message == null) return;
        
        // Mettre à jour l'état du jeu côté serveur
        gameState.updateFromMessage(message, playerId);
        
        // Retransmettre le message aux autres joueurs
        broadcastExcept(message, playerId);
        
        // Log pour debug
        if (message.getType() != NetworkMessage.MessageType.PADDLE_UPDATE && 
            message.getType() != NetworkMessage.MessageType.BALL_UPDATE) {
            System.out.println("Message reçu de joueur " + playerId + " : " + message.getType());
        }
    }
    
    /**
     * Retire un client déconnecté
     */
    public void removeClient(ClientHandler handler) {
        clients.remove(handler);
        System.out.println("Joueur " + handler.getPlayerId() + " déconnecté");
        
        // Notifier les autres joueurs
        NetworkMessage disconnectMsg = new NetworkMessage(NetworkMessage.MessageType.ERROR, 0);
        disconnectMsg.setMessage("Joueur " + handler.getPlayerId() + " s'est déconnecté");
        broadcast(disconnectMsg);
    }
    
    /**
     * Arrête le serveur
     */
    public void stop() {
        isRunning = false;
        
        // Fermer toutes les connexions clients
        for (ClientHandler client : clients) {
            client.close();
        }
        clients.clear();
        
        // Fermer le serveur socket
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la fermeture du serveur : " + e.getMessage());
        }
        
        // Arrêter le pool de threads
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
        
        System.out.println("Serveur arrêté");
    }
    
    /**
     * Classe interne pour gérer chaque client connecté
     */
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private int playerId;
        private GameServer server;
        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;
        private boolean running;
        
        public ClientHandler(Socket socket, int playerId, GameServer server) {
            this.socket = socket;
            this.playerId = playerId;
            this.server = server;
            this.running = true;
        }
        
        public int getPlayerId() {
            return playerId;
        }
        
        @Override
        public void run() {
            try {
                // Initialiser les streams (l'ordre est important)
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();
                inputStream = new ObjectInputStream(socket.getInputStream());
                
                // Boucle de réception des messages
                while (running) {
                    try {
                        NetworkMessage message = (NetworkMessage) inputStream.readObject();
                        server.handleClientMessage(message, playerId);
                    } catch (EOFException e) {
                        System.out.println("Fin de connexion pour le joueur " + playerId);
                        running = false;
                    } catch (ClassNotFoundException e) {
                        System.err.println("Erreur de désérialisation : " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("Erreur de communication avec le joueur " + playerId + " : " + e.getMessage());
                }
            } finally {
                close();
                server.removeClient(this);
            }
        }
        
        /**
         * Envoie un message au client
         */
        public synchronized void sendMessage(NetworkMessage message) {
            try {
                if (outputStream != null && !socket.isClosed()) {
                    outputStream.writeObject(message);
                    outputStream.flush();
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de l'envoi au joueur " + playerId + " : " + e.getMessage());
                running = false;
            }
        }
        
        /**
         * Ferme la connexion
         */
        public void close() {
            running = false;
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
    
    /**
     * Classe interne pour gérer l'état du jeu côté serveur
     */
    private static class ServerGameState {
        private double ballX, ballY, ballVx, ballVy;
        private Map<Integer, PaddleState> paddles;
        private boolean isLaunchMode;
        
        public ServerGameState() {
            this.paddles = new ConcurrentHashMap<>();
            this.isLaunchMode = true;
        }
        
        public void updateFromMessage(NetworkMessage msg, int playerId) {
            switch (msg.getType()) {
                case BALL_UPDATE:
                    ballX = msg.getBallX();
                    ballY = msg.getBallY();
                    ballVx = msg.getBallVelocityX();
                    ballVy = msg.getBallVelocityY();
                    break;
                    
                case PADDLE_UPDATE:
                    PaddleState paddle = paddles.get(playerId);
                    if (paddle == null) {
                        paddle = new PaddleState();
                        paddles.put(playerId, paddle);
                    }
                    paddle.x = msg.getPaddleX();
                    paddle.y = msg.getPaddleY();
                    paddle.angle = msg.getPaddleAngle();
                    break;
                    
                case BALL_LAUNCHED:
                    isLaunchMode = false;
                    ballX = msg.getBallX();
                    ballY = msg.getBallY();
                    ballVx = msg.getBallVelocityX();
                    ballVy = msg.getBallVelocityY();
                    break;
                    
                default:
                    break;
            }
        }
        
        private static class PaddleState {
            double x, y, angle;
        }
    }
    
    /**
     * Point d'entrée pour tests
     */
    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.start();
        
        // Ajouter un hook pour arrêter proprement le serveur
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nArrêt du serveur...");
            server.stop();
        }));
    }
}
