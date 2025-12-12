# Changelog - Chess-Pong

Toutes les modifications notables de ce projet seront documentées dans ce fichier.

## [2.0.0] - 2025-12-11

### ✨ Ajouté - Mode Multijoueur Réseau

#### Infrastructure Réseau
- **GameServer.java** : Serveur TCP pour gérer jusqu'à 2 joueurs simultanés
- **GameClient.java** : Client TCP avec gestion de connexion et synchronisation
- **NetworkMessage.java** : Format de message sérialisé pour la communication
- **NetworkGameSync.java** : Wrapper pour intégration réseau dans le jeu

#### Interfaces de Lancement
- **ServerLauncher.java** : Interface GUI pour démarrer le serveur
- **ClientLauncher.java** : Interface GUI pour connecter un client
- **start_server.sh** : Script bash pour démarrage rapide du serveur
- **start_client.sh** : Script bash pour démarrage rapide du client
- **test_multiplayer.sh** : Script pour tester le mode multijoueur en local

#### Documentation
- **NETWORK_GUIDE.md** : Guide complet du système réseau
- **ARCHITECTURE.md** : Diagrammes et explications de l'architecture
- **QUICKSTART.md** : Guide de démarrage rapide avec exemples
- **README.md** : Mise à jour avec instructions multijoueur

#### Fonctionnalités Réseau
- Synchronisation en temps réel des positions (ball, paddles)
- Broadcast des événements de jeu (dégâts, destructions, victoire)
- Gestion des connexions multiples avec threads
- Messages typés avec sérialisation Java
- Système de timestamps pour gérer la latence
- Fréquence de mise à jour configurable (50ms par défaut)

### 🔧 Caractéristiques Techniques
- **Protocole** : TCP/IP avec Java Sockets
- **Port** : 5555 (configurable)
- **Capacité** : 2 joueurs maximum
- **Latence** : Optimisé pour LAN (<50ms)
- **Sérialisation** : Java Object Serialization

---

## [1.2.0] - 2025-12-11

### ✨ Ajouté - Système de Lancement

#### LaunchManager
- **LaunchManager.java** : Gestionnaire du mode lancement de balle
- Mode lancement au démarrage du jeu
- Contrôle de l'angle de lancement (-90° à +90°)
- Position de la balle sur la surface du paddle
- Interface utilisateur visuelle pour le mode lancement

#### Fonctionnalités
- Ajustement d'angle avec W/S ou ↑/↓
- Barre d'angle visuelle avec indicateur
- Lancement avec SPACE ou ENTER
- Réinitialisation d'angle avec R
- Balle suit le paddle pendant le positionnement

### 🔄 Modifié
- **ChessBoard.java** : Intégration du mode lancement dans la boucle de jeu
- **Ball.java** : Ajout de setters pour position et vélocité
- Séparation logique entre mode lancement et mode jeu

---

## [1.1.0] - 2025-12-10

### ✨ Ajouté - Système de Paddles avec Inclinaison

#### Paddles
- **Paddle.java** : Classe pour les paddles contrôlables
- Système d'inclinaison toggle (A/E et ↑/↓)
- Mise à jour progressive de l'angle (interpolation)
- Rebond de balle basé sur l'angle du paddle
- Dimension : 1 case largeur × 1/2 case hauteur

#### Game Logic
- **GameLogic.java** : Logique de jeu séparée
- **GameState.java** : État du jeu centralisé
- **PaddleManager.java** : Gestionnaire de paddles
- **BallManager.java** : Gestionnaire de balle

### 🔧 Modifié
- Refactorisation du code en architecture OOP
- Séparation des responsabilités (MVC-like)

---

## [1.0.0] - 2025-12-09

### ✨ Ajouté - Version Initiale

#### Jeu de Base
- **Ball.java** : Balle avec physique de rebond
- **ChessBoard.java** : Plateau de jeu avec rendu
- Collision rectangulaire précise (1 case = 1 hitbox)
- Système de dégâts aux pièces

#### Pièces d'Échecs
- **Piece.java** : Classe abstraite de base
- 6 types : Pion, Tour, Cavalier, Fou, Reine, Roi
- Points de vie configurables (validation > 0)
- Barres de vie visuelles

#### Configuration
- **InputDonnee.java** : Formulaire de configuration
- Nombre de colonnes configurable (2, 4, 6, 8)
- HP personnalisable par type de pièce
- Validation des entrées

#### Conditions de Victoire
- Détection de mort du Roi
- Écran de victoire avec overlay
- Messages de fin de partie

#### Interface
- Rendu avec Swing
- Couleurs officielles chess.com
- Images PNG pour les pièces
- Fenêtre redimensionnable

---

## Types de Changements
- ✨ **Ajouté** : Nouvelles fonctionnalités
- 🔄 **Modifié** : Changements dans les fonctionnalités existantes
- 🐛 **Corrigé** : Corrections de bugs
- 🗑️ **Supprimé** : Fonctionnalités retirées
- 🔒 **Sécurité** : Corrections de vulnérabilités
- 🔧 **Technique** : Changements techniques sans impact utilisateur

---

## Roadmap Future (Idées)

### Version 3.0 (Potentielle)
- [ ] Mode spectateur
- [ ] Chat intégré
- [ ] Replay de parties
- [ ] Statistiques de jeu
- [ ] Classement/Leaderboard
- [ ] Personnalisation des skins
- [ ] Effets sonores et musique
- [ ] Modes de jeu alternatifs
- [ ] Support de plus de 2 joueurs
- [ ] Mode tournoi

### Améliorations Réseau
- [ ] UDP pour positions (latence réduite)
- [ ] Compression des messages
- [ ] Reconnexion automatique
- [ ] Prédiction côté client améliorée
- [ ] Interpolation des positions
- [ ] Support NAT traversal
- [ ] Serveur dédié cloud

---

**Mainteneur** : Rasolofoharilala
**Dernière mise à jour** : 11 décembre 2025
