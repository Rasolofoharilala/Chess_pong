# Guide du Mode Multijoueur Réseau - Chess-Pong

## Architecture Client-Serveur

Le jeu Chess-Pong supporte maintenant le mode multijoueur réseau avec une architecture client-serveur :

- **Serveur** : Gère jusqu'à 2 joueurs et synchronise l'état du jeu
- **Clients** : Se connectent au serveur et jouent en temps réel

## Comment démarrer une partie

### 1. Démarrer le Serveur

Option 1 : Via le launcher GUI
```bash
java ServerLauncher
```

Option 2 : Directement
```bash
java -cp . Network.GameServer
```

Le serveur démarrera sur le port **5555** et attendra 2 joueurs.

### 2. Connecter les Clients

Sur chaque machine cliente :
```bash
java ClientLauncher
```

Entrez :
- **IP du serveur** : L'adresse IP de la machine serveur (ou `localhost` si sur le même PC)
- **Port** : 5555 (par défaut)

Cliquez sur "Se connecter"

### 3. Démarrer une partie

Une fois les 2 clients connectés, le serveur affichera "Tous les joueurs sont connectés !"

Vous pouvez alors démarrer le jeu normal avec la configuration des pièces.

## Architecture des Messages Réseau

### Types de Messages

| Type | Description |
|------|-------------|
| `HANDSHAKE` | Salutation initiale client-serveur |
| `PLAYER_INPUT` | État des touches du joueur |
| `BALL_UPDATE` | Position et vélocité de la balle |
| `BALL_LAUNCHED` | La balle a été lancée |
| `PADDLE_UPDATE` | Position et angle du paddle |
| `PIECE_DAMAGED` | Une pièce a pris des dégâts |
| `PIECE_DESTROYED` | Une pièce a été détruite |
| `GAME_OVER` | Fin de partie |

### Format des Messages

```java
NetworkMessage message = new NetworkMessage(MessageType.BALL_UPDATE);
message.setBallX(x);
message.setBallY(y);
message.setBallVelocityX(vx);
message.setBallVelocityY(vy);
```

## Intégration dans ChessBoard

Pour un jeu multijoueur, ChessBoard peut utiliser `NetworkGameSync` :

```java
// Créer la synchronisation réseau
NetworkGameSync netSync = new NetworkGameSync("192.168.1.100", 5555);

// Se connecter au serveur
if (netSync.connectToServer()) {
    // Envoyer les mises à jour
    netSync.sendBallUpdate(ball);
    netSync.sendPaddleUpdate(paddle);
}
```

## Fréquence de Synchronisation

- **Ball Update** : Tous les 50ms
- **Paddle Update** : Tous les 50ms
- **Player Input** : À chaque changement
- **Events** : Immédiats (destruction de pièce, fin de partie, etc.)

## Latence et Synchronisation

Le système utilise des timestamps pour gérer la latence :
- Chaque message inclut un timestamp
- Les positions sont calculées en fonction du timestamp pour éviter les désynchronisations
- La fréquence de synchronisation aide à réduire les écarts

## Adresses IP

Pour trouver l'adresse IP du serveur :

### Linux/Mac
```bash
ifconfig
```

### Windows
```bash
ipconfig
```

Cherchez l'adresse IPv4 (par exemple : `192.168.1.100`)

## Dépannage

### Connexion refusée
- Vérifiez que le serveur est en cours d'exécution
- Vérifiez l'adresse IP et le port
- Vérifiez le firewall

### Déconnexion fréquente
- Vérifiez la connexion réseau
- Réduisez la fréquence d'envoi des messages
- Vérifiez la bande passante disponible

### Désynchronisation des positions
- Réduisez le délai d'UPDATE_INTERVAL
- Vérifiez les timestamps des messages
- Vérifiez que tous les clients reçoivent les mises à jour

## Classes Principales

- `GameServer.java` : Serveur TCP principal
- `GameClient.java` : Client TCP qui se connecte au serveur
- `NetworkMessage.java` : Format des messages sérialisés
- `NetworkGameSync.java` : Wrapper pour intégration dans ChessBoard
- `ServerLauncher.java` : Interface pour démarrer le serveur
- `ClientLauncher.java` : Interface pour démarrer le client

## Exemple d'Utilisation Complète

```java
// Côté serveur
GameServer server = new GameServer();
server.start(); // Écoute les connexions

// Côté client
GameClient client = new GameClient("192.168.1.100", 5555);
if (client.connect()) {
    // Envoyer les positions de la balle
    client.sendBallUpdate(x, y, vx, vy);
    
    // Écouter les messages
    client.setMessageListener(message -> {
        System.out.println("Reçu : " + message.getType());
    });
}
```

## Performances

- **Connexion** : ~100-500ms selon la latence réseau
- **Synchronisation** : 50ms d'intervalle base
- **Max 2 joueurs** pour maintenir la performance
- **Port** : 5555 (TCP)

---

**Note** : Assurez-vous que les deux machines sont sur le même réseau ou que le serveur est accessible via internet.
