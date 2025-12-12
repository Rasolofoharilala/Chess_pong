# Chess-Pong 🎮♟️

Un jeu hybride combinant les échecs et Pong, avec support du mode multijoueur en réseau !

## 🎯 Concept du Jeu

Chess-Pong est un jeu innovant où :
- Deux joueurs contrôlent des **paddles** pour faire rebondir une balle
- Les pièces d'échecs des deux camps sont disposées sur le plateau
- La balle **détruit progressivement** les pièces en leur infligeant des dégâts
- Le premier joueur à détruire le **Roi** adverse gagne !

## ✨ Fonctionnalités

### Mode Solo
- Configuration personnalisée des points de vie des pièces
- Système de lancement de balle avec contrôle d'angle
- Paddles avec inclinaison pour contrôler la trajectoire
- Barres de vie visuelles pour chaque pièce
- Physique de collision précise (1 case = 1 pièce)

### Mode Multijoueur Réseau 🌐
- Architecture **Client-Serveur** TCP
- Jusqu'à **2 joueurs** simultanés
- Synchronisation en temps réel des positions et événements
- Jeu possible sur différentes machines (même réseau ou via IP)

## 🎮 Contrôles

### Joueur Blanc (Bas)
- **Q / D** : Déplacer le paddle gauche/droite
- **A / E** : Incliner le paddle
- **W / S** : Ajuster l'angle de lancement (mode lancement)
- **SPACE / ENTER** : Lancer la balle

### Joueur Noir (Haut)
- **← / →** : Déplacer le paddle
- **↑ / ↓** : Incliner le paddle

## 🚀 Installation et Lancement

### Mode Solo (Local)

```bash
# Compiler le projet
javac -d bin $(find . -name "*.java")

# Lancer le jeu
cd bin
java Main
```

### Mode Multijoueur

#### Démarrer le Serveur
```bash
./start_server.sh
```
Ou :
```bash
java -cp bin ServerLauncher
```

Le serveur affichera son **adresse IP** et attendra les connexions sur le port **5555**.

#### Connecter les Clients

Sur chaque machine cliente :
```bash
./start_client.sh
```
Ou :
```bash
java -cp bin ClientLauncher
```

Entrez l'adresse IP du serveur et cliquez sur "Se connecter".

## 📁 Structure du Projet

```
Chess_pong/
├── Affichage/          # Interface graphique et rendu
│   ├── Ball.java
│   ├── ChessBoard.java
│   ├── Paddle.java
│   └── ...
├── Game/               # Logique de jeu
│   ├── GameLogic.java
│   ├── GameState.java
│   ├── LaunchManager.java
│   └── NetworkGameSync.java
├── Network/            # Système réseau
│   ├── GameServer.java
│   ├── GameClient.java
│   └── NetworkMessage.java
├── Piece/              # Pièces d'échecs
│   ├── Piece.java
│   ├── Pion.java
│   ├── Roi.java
│   └── ...
├── Main.java           # Point d'entrée (solo)
├── ServerLauncher.java # Point d'entrée serveur
├── ClientLauncher.java # Point d'entrée client
└── NETWORK_GUIDE.md    # Guide détaillé du mode réseau
```

## 📊 Configuration des Pièces

Au démarrage, vous pouvez configurer :
- Nombre de colonnes de pièces (2, 4, 6 ou 8)
- Points de vie de chaque type de pièce :
  - Pion
  - Tour
  - Cavalier
  - Fou
  - Reine
  - Roi

## 🔧 Paramètres Réseau

- **Port par défaut** : 5555
- **Max joueurs** : 2
- **Fréquence de synchronisation** : 50ms
- **Protocol** : TCP avec sérialisation Java

## 📖 Documentation Réseau

Pour des informations détaillées sur le mode multijoueur, consultez :
- [`NETWORK_GUIDE.md`](NETWORK_GUIDE.md) - Guide complet du système réseau

## 🛠️ Technologies

- **Java 17+**
- **Swing** pour l'interface graphique
- **Java Sockets** pour le réseau (TCP)
- **Sérialisation Java** pour les messages réseau
- **Multithreading** pour la gestion des connexions

## 🎯 Stratégie de Jeu

1. **Mode Lancement** : Positionnez votre paddle et ajustez l'angle de lancement
2. **Contrôle de trajectoire** : Inclinez le paddle pour modifier la direction de la balle
3. **Ciblez le Roi** : Concentrez vos attaques sur le Roi adverse pour gagner rapidement
4. **Protégez votre Roi** : Utilisez votre paddle pour bloquer la balle avant qu'elle n'atteigne vos pièces importantes

## 🐛 Dépannage

### Erreur de compilation
```bash
# Assurez-vous d'avoir Java 17+
java --version

# Nettoyez et recompilez
rm -rf bin
mkdir bin
javac -d bin $(find . -name "*.java")
```

### Connexion réseau refusée
- Vérifiez que le serveur est lancé
- Vérifiez l'adresse IP et le port
- Vérifiez le firewall (autorisez le port 5555)

### Désynchronisation en réseau
- Réduisez la latence réseau
- Assurez-vous que les deux machines sont sur le même réseau local
- Vérifiez la bande passante disponible

## 👥 Auteurs

- Rasolofoharilala

## 📜 Licence

Ce projet est sous licence libre. Voir LICENSE pour plus de détails.

---

**Amusez-vous bien !** 🎮♟️