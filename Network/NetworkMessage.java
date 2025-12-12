package Network;

import java.io.Serializable;

/**
 * Message réseau pour synchroniser l'état du jeu entre client et serveur
 */
public class NetworkMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum MessageType {
        // Types de messages
        PLAYER_INPUT,           // Entrée joueur (mouvements paddle, angles, etc.)
        GAME_STATE,             // État complet du jeu
        BALL_UPDATE,            // Mise à jour position balle
        PADDLE_UPDATE,          // Mise à jour position paddle
        PIECE_DAMAGED,          // Une pièce a pris des dégâts
        PIECE_DESTROYED,        // Une pièce a été détruite
        GAME_OVER,              // Fin de partie
        LAUNCH_READY,           // Mode lancement prêt
        BALL_LAUNCHED,          // Balle lancée
        HANDSHAKE,              // Salutation client-serveur
        ACK,                    // Accusé de réception
        ERROR                   // Message d'erreur
    }

    private MessageType type;
    private int playerId;              // 0 = serveur, 1 = joueur blanc, 2 = joueur noir
    private long timestamp;            // Timestamp du message pour synchronisation
    
    // Données de position
    private double ballX;
    private double ballY;
    private double ballVelocityX;
    private double ballVelocityY;
    
    private double paddleX;
    private double paddleY;
    private double paddleAngle;
    
    // Données de jeu
    private int[][] pieceStates;       // État des pièces (HP)
    private boolean isGameOver;
    private String winner;
    private boolean isLaunchMode;
    private int damage;                // Dégâts infligés
    private int pieceRow;
    private int pieceCol;
    
    // Données d'input joueur
    private boolean[] keysPressed;     // État des touches
    
    // Message de statut
    private String message;

    public NetworkMessage(MessageType type) {
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public NetworkMessage(MessageType type, int playerId) {
        this.type = type;
        this.playerId = playerId;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters et Setters
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Positon de la balle
    public double getBallX() {
        return ballX;
    }

    public void setBallX(double ballX) {
        this.ballX = ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public void setBallY(double ballY) {
        this.ballY = ballY;
    }

    public double getBallVelocityX() {
        return ballVelocityX;
    }

    public void setBallVelocityX(double ballVelocityX) {
        this.ballVelocityX = ballVelocityX;
    }

    public double getBallVelocityY() {
        return ballVelocityY;
    }

    public void setBallVelocityY(double ballVelocityY) {
        this.ballVelocityY = ballVelocityY;
    }

    // Position du paddle
    public double getPaddleX() {
        return paddleX;
    }

    public void setPaddleX(double paddleX) {
        this.paddleX = paddleX;
    }

    public double getPaddleY() {
        return paddleY;
    }

    public void setPaddleY(double paddleY) {
        this.paddleY = paddleY;
    }

    public double getPaddleAngle() {
        return paddleAngle;
    }

    public void setPaddleAngle(double paddleAngle) {
        this.paddleAngle = paddleAngle;
    }

    // État des pièces
    public int[][] getPieceStates() {
        return pieceStates;
    }

    public void setPieceStates(int[][] pieceStates) {
        this.pieceStates = pieceStates;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public boolean isLaunchMode() {
        return isLaunchMode;
    }

    public void setLaunchMode(boolean launchMode) {
        isLaunchMode = launchMode;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getPieceRow() {
        return pieceRow;
    }

    public void setPieceRow(int pieceRow) {
        this.pieceRow = pieceRow;
    }

    public int getPieceCol() {
        return pieceCol;
    }

    public void setPieceCol(int pieceCol) {
        this.pieceCol = pieceCol;
    }

    // Input joueur
    public boolean[] getKeysPressed() {
        return keysPressed;
    }

    public void setKeysPressed(boolean[] keysPressed) {
        this.keysPressed = keysPressed;
    }

    // Message de statut
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NetworkMessage{" +
                "type=" + type +
                ", playerId=" + playerId +
                ", timestamp=" + timestamp +
                '}';
    }
}
