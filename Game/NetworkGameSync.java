package Game;

import Network.GameClient;
import Network.NetworkMessage;
import Affichage.Ball;
import Affichage.Paddle;

/**
 * Gestionnaire de synchronisation réseau pour le jeu en multijoueur
 */
public class NetworkGameSync implements GameClient.ClientMessageListener {
    private GameClient client;
    private boolean isNetworkGame;
    private int localPlayerId;
    private long lastBallUpdateTime = 0;
    private long lastPaddleUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 50; // ms entre les mises à jour

    public NetworkGameSync(String serverIP, int serverPort) {
        this.client = new GameClient(serverIP, serverPort);
        this.isNetworkGame = false;
    }

    /**
     * Connecte au serveur de jeu
     */
    public boolean connectToServer() {
        if (client.connect()) {
            client.setMessageListener(this);
            isNetworkGame = true;
            localPlayerId = client.getPlayerId();
            return true;
        }
        return false;
    }

    /**
     * Vérifie si c'est un jeu réseau
     */
    public boolean isNetworkGame() {
        return isNetworkGame;
    }

    /**
     * Obtient le client de jeu
     */
    public GameClient getClient() {
        return client;
    }

    /**
     * Obtient l'ID du joueur local
     */
    public int getLocalPlayerId() {
        return localPlayerId;
    }

    /**
     * Reçoit les messages du serveur
     */
    @Override
    public void onMessageReceived(NetworkMessage message) {
        if (message == null) return;

        switch (message.getType()) {
            case PLAYER_INPUT:
                // Un autre joueur a changé ses inputs
                handleRemotePlayerInput(message);
                break;

            case BALL_UPDATE:
                // Mise à jour de la position de la balle
                handleBallUpdate(message);
                break;

            case BALL_LAUNCHED:
                // La balle a été lancée
                handleBallLaunched(message);
                break;

            case PADDLE_UPDATE:
                // Mise à jour de la position du paddle
                handlePaddleUpdate(message);
                break;

            case PIECE_DAMAGED:
                // Une pièce a pris des dégâts
                handlePieceDamaged(message);
                break;

            case PIECE_DESTROYED:
                // Une pièce a été détruite
                handlePieceDestroyed(message);
                break;

            case GAME_OVER:
                // Fin de partie
                handleGameOver(message);
                break;

            default:
                break;
        }
    }

    /**
     * Envoie la mise à jour de la balle
     */
    public void sendBallUpdate(Ball ball) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBallUpdateTime > UPDATE_INTERVAL) {
            client.sendBallUpdate(ball.getX(), ball.getY(), 
                                 ball.getVelocityX(), ball.getVelocityY());
            lastBallUpdateTime = currentTime;
        }
    }

    /**
     * Envoie la mise à jour du paddle
     */
    public void sendPaddleUpdate(Paddle paddle) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPaddleUpdateTime > UPDATE_INTERVAL) {
            client.sendPaddleUpdate(paddle.getX(), paddle.getY(), paddle.getAngle());
            lastPaddleUpdateTime = currentTime;
        }
    }

    /**
     * Envoie que la balle a été lancée
     */
    public void sendBallLaunched(Ball ball) {
        client.sendBallLaunched(ball.getX(), ball.getY(),
                               ball.getVelocityX(), ball.getVelocityY());
    }

    /**
     * Envoie les inputs du joueur
     */
    public void sendPlayerInput(boolean[] keysPressed) {
        client.sendPlayerInput(keysPressed);
    }

    /**
     * Envoie que le jeu est terminé
     */
    public void sendGameOver(String winner) {
        client.sendGameOver(winner);
    }

    /**
     * Envoie les dégâts à une pièce
     */
    public void sendPieceDamaged(int row, int col, int damage) {
        client.sendPieceDamaged(row, col, damage);
    }

    /**
     * Envoie qu'une pièce a été détruite
     */
    public void sendPieceDestroyed(int row, int col) {
        client.sendPieceDestroyed(row, col);
    }

    /**
     * Traite un input du joueur distant
     */
    private void handleRemotePlayerInput(NetworkMessage message) {
        // À intégrer dans ChessBoard pour appliquer les inputs des autres joueurs
        boolean[] remoteKeys = message.getKeysPressed();
        if (remoteKeys != null) {
            // Mettre à jour l'état du joueur distant
            System.out.println("Input reçu du joueur " + message.getPlayerId());
        }
    }

    /**
     * Traite une mise à jour de balle distante
     */
    private void handleBallUpdate(NetworkMessage message) {
        // À intégrer dans ChessBoard pour synchroniser la position de la balle
        System.out.println("Mise à jour balle : (" + message.getBallX() + ", " + message.getBallY() + ")");
    }

    /**
     * Traite un lancement de balle distant
     */
    private void handleBallLaunched(NetworkMessage message) {
        // À intégrer dans ChessBoard pour synchroniser le lancement
        System.out.println("Balle lancée par joueur " + message.getPlayerId());
    }

    /**
     * Traite une mise à jour de paddle distant
     */
    private void handlePaddleUpdate(NetworkMessage message) {
        // À intégrer dans ChessBoard pour synchroniser la position du paddle
        System.out.println("Mise à jour paddle : (" + message.getPaddleX() + ", " + message.getPaddleY() + ")");
    }

    /**
     * Traite les dégâts à une pièce distante
     */
    private void handlePieceDamaged(NetworkMessage message) {
        System.out.println("Pièce en (" + message.getPieceRow() + "," + message.getPieceCol() + ") a subi " + 
                          message.getDamage() + " dégâts");
    }

    /**
     * Traite la destruction d'une pièce distante
     */
    private void handlePieceDestroyed(NetworkMessage message) {
        System.out.println("Pièce en (" + message.getPieceRow() + "," + message.getPieceCol() + ") détruite");
    }

    /**
     * Traite la fin de partie
     */
    private void handleGameOver(NetworkMessage message) {
        System.out.println("Fin de partie ! Gagnant : " + message.getWinner());
    }

    /**
     * Déconnecte du serveur
     */
    public void disconnect() {
        if (client != null) {
            client.disconnect();
            isNetworkGame = false;
        }
    }

    /**
     * Vérifie si le client est connecté
     */
    public boolean isConnected() {
        return client != null && client.isConnected();
    }
}
