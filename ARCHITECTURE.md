# Architecture du Système Réseau - Chess-Pong

## Vue d'Ensemble

```
┌─────────────────────────────────────────────────────────────────┐
│                     ARCHITECTURE RÉSEAU                          │
└─────────────────────────────────────────────────────────────────┘

           Machine 1 (Serveur)              Machine 2 (Client)
    ┌──────────────────────────┐      ┌──────────────────────────┐
    │                          │      │                          │
    │    ServerLauncher.java   │      │   ClientLauncher.java    │
    │           │              │      │           │              │
    │           ▼              │      │           ▼              │
    │    GameServer.java       │      │     GameClient.java      │
    │           │              │      │           │              │
    │           │ Port 5555    │      │           │              │
    │           ├─────────────TCP─────┼───────────┤              │
    │           │ (Messages)   │      │           │              │
    │           │              │      │           ▼              │
    │    ClientHandler ×2      │      │   NetworkGameSync.java   │
    │           │              │      │           │              │
    │           ▼              │      │           ▼              │
    │    GameStateSync         │      │     ChessBoard.java      │
    │                          │      │           │              │
    └──────────────────────────┘      └───────────┼──────────────┘
                                                  │
                                      ┌───────────┴───────────┐
                                      │                       │
                                      ▼                       ▼
                                Ball.java               Paddle.java
```

## Flux de Communication

### 1. Connexion Initiale

```
Client                     Serveur
  │                          │
  ├──── connect() ──────────>│
  │                          ├─ Accepte connexion
  │                          ├─ Assigne ID (1 ou 2)
  │<──── HANDSHAKE ──────────┤
  │                          │
  │                          ├─ Attend 2ème joueur
  │                          │
```

### 2. Synchronisation de Jeu

```
Joueur 1 (Client)          Serveur           Joueur 2 (Client)
       │                      │                      │
       ├── PADDLE_UPDATE ────>│                      │
       │                      ├──── PADDLE_UPDATE ──>│
       │                      │                      │
       │                      │<──── BALL_UPDATE ────┤
       │<──── BALL_UPDATE ────┤                      │
       │                      │                      │
       ├── PIECE_DAMAGED ───->│                      │
       │                      ├──── PIECE_DAMAGED ──>│
       │                      │                      │
```

### 3. Fin de Partie

```
Joueur 1                   Serveur           Joueur 2
    │                         │                   │
    ├── Roi détruit          │                   │
    ├── GAME_OVER ──────────>│                   │
    │                         ├── GAME_OVER ────>│
    │                         │                   │
    │<──── ACK ───────────────┤                   │
    │                         │<──── ACK ─────────┤
```

## Classes Principales

### Network Package

#### NetworkMessage.java
```
┌─────────────────────────────┐
│     NetworkMessage          │
├─────────────────────────────┤
│ - type: MessageType         │
│ - playerId: int             │
│ - timestamp: long           │
│ - ballX, ballY: double      │
│ - paddleX, paddleY: double  │
│ - pieceStates: int[][]      │
│ - keysPressed: boolean[]    │
├─────────────────────────────┤
│ + getters/setters           │
│ + serialize()               │
└─────────────────────────────┘
```

#### GameServer.java
```
┌─────────────────────────────┐
│       GameServer            │
├─────────────────────────────┤
│ - serverSocket: Socket      │
│ - clients: Map<Int,Handler> │
│ - gameStateSync             │
├─────────────────────────────┤
│ + start()                   │
│ + broadcastMessage()        │
│ + handleClientMessage()     │
│ + removeClient()            │
└─────────────────────────────┘
```

#### GameClient.java
```
┌─────────────────────────────┐
│       GameClient            │
├─────────────────────────────┤
│ - socket: Socket            │
│ - inputStream: ObjectIn     │
│ - outputStream: ObjectOut   │
│ - playerId: int             │
│ - listener: Listener        │
├─────────────────────────────┤
│ + connect()                 │
│ + sendMessage()             │
│ + sendBallUpdate()          │
│ + sendPaddleUpdate()        │
│ + disconnect()              │
└─────────────────────────────┘
```

### Game Package

#### NetworkGameSync.java
```
┌─────────────────────────────┐
│    NetworkGameSync          │
├─────────────────────────────┤
│ - client: GameClient        │
│ - isNetworkGame: boolean    │
│ - localPlayerId: int        │
├─────────────────────────────┤
│ + connectToServer()         │
│ + sendBallUpdate()          │
│ + sendPaddleUpdate()        │
│ + onMessageReceived()       │
└─────────────────────────────┘
```

## Types de Messages

| MessageType        | Contenu                  | Fréquence    |
|--------------------|--------------------------|--------------|
| HANDSHAKE          | ID joueur, message       | Une fois     |
| PLAYER_INPUT       | boolean[] keysPressed    | À chaque key |
| BALL_UPDATE        | x, y, vx, vy             | 50ms         |
| PADDLE_UPDATE      | x, y, angle              | 50ms         |
| PIECE_DAMAGED      | row, col, damage         | Sur event    |
| PIECE_DESTROYED    | row, col                 | Sur event    |
| BALL_LAUNCHED      | x, y, vx, vy             | Sur event    |
| GAME_OVER          | winner, isGameOver       | Sur event    |
| ACK                | Confirmation             | Sur demande  |

## Schéma de Synchronisation

```
┌─────────────────────────────────────────────────────────┐
│                    LOOP DE JEU (60 FPS)                 │
└─────────────────────────────────────────────────────────┘
         │
         ▼
   ┌─────────────┐
   │ Input Local │ ──────> Envoyer PLAYER_INPUT
   └─────────────┘
         │
         ▼
   ┌─────────────┐
   │Update Paddle│ ──────> Envoyer PADDLE_UPDATE (50ms)
   └─────────────┘
         │
         ▼
   ┌─────────────┐
   │ Update Ball │ ──────> Envoyer BALL_UPDATE (50ms)
   └─────────────┘
         │
         ▼
   ┌─────────────┐
   │ Collisions  │ ──────> Envoyer PIECE_DAMAGED/DESTROYED
   └─────────────┘
         │
         ▼
   ┌─────────────┐
   │Check Victory│ ──────> Envoyer GAME_OVER
   └─────────────┘
         │
         ▼
   ┌─────────────┐
   │   Render    │
   └─────────────┘
```

## Gestion des Conflits

### Autorité du Serveur
- Le **serveur** fait autorité sur l'état du jeu
- Les clients envoient leurs inputs/actions
- Le serveur broadcast l'état validé à tous les clients
- Les clients appliquent l'état reçu du serveur

### Prédiction Côté Client
- Le client applique ses inputs localement immédiatement
- Le serveur valide et corrige si nécessaire
- Permet une réactivité malgré la latence

## Configuration Réseau

### Ports
- **5555** : Port TCP principal
- Configurable dans `GameServer.java`

### Timeouts
- Connexion : 30 secondes
- Read/Write : Aucun (TCP maintient la connexion)

### Sérialisation
- **Java Serialization** pour les messages
- Tous les messages implémentent `Serializable`

---

**Note** : Ce système est conçu pour un réseau local (LAN) avec faible latence.
Pour un jeu sur Internet, considérer UDP pour les positions et TCP pour les events critiques.
