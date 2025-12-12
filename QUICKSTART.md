# 🚀 Démarrage Rapide - Chess-Pong Multijoueur

## Option 1 : Test Local (sur la même machine)

### Méthode Automatique
```bash
./test_multiplayer.sh
```

Puis dans **2 nouveaux terminaux** :
```bash
cd bin
java ClientLauncher
# Entrez "localhost" comme IP
```

---

## Option 2 : Jeu en Réseau Local (LAN)

### Sur la Machine Serveur (ex: PC1)

1. **Trouver votre IP**
```bash
ifconfig    # Linux/Mac
ipconfig    # Windows
```
Exemple : `192.168.1.100`

2. **Démarrer le serveur**
```bash
./start_server.sh
```

Le serveur affichera :
```
========================================
  Serveur Chess-Pong - Mode Multijoueur
========================================

Adresse IP du serveur : 192.168.1.100
Port : 5555

Les clients peuvent se connecter avec cette adresse IP
```

### Sur les Machines Clientes (ex: PC2 et PC3)

1. **Démarrer le client**
```bash
./start_client.sh
```

2. **Se connecter**
- Entrez l'IP du serveur : `192.168.1.100`
- Port : `5555`
- Cliquez sur "Se connecter"

3. **Répéter pour le 2ème joueur**

---

## Option 3 : Compilation et Lancement Manuel

### Compiler
```bash
# Créer le dossier bin
mkdir -p bin

# Compiler tous les fichiers Java
javac -d bin $(find . -name "*.java")
```

### Lancer le Serveur
```bash
cd bin
java ServerLauncher
# Ou directement :
java Network.GameServer
```

### Lancer le Client
```bash
cd bin
java ClientLauncher
# Ou directement :
java Network.GameClient <IP_SERVEUR>
```

---

## 🎮 Une Fois Connecté

1. **Les 2 joueurs doivent être connectés** avant de pouvoir jouer
2. Le serveur affichera : `"Tous les joueurs sont connectés !"`
3. Sur chaque client, configurez le jeu (nombre de pièces, points de vie)
4. **Jouez !**

### Contrôles

**Joueur 1 (Blanc - Bas)**
- Q/D : Déplacer paddle
- A/E : Incliner paddle
- W/S : Ajuster angle de lancement
- SPACE : Lancer la balle

**Joueur 2 (Noir - Haut)**
- ←/→ : Déplacer paddle
- ↑/↓ : Incliner paddle

---

## 📊 Vérification de la Connexion

### Test de Ping
```bash
ping <IP_SERVEUR>
```

### Vérifier le Port
```bash
# Linux
netstat -tuln | grep 5555

# Windows
netstat -an | findstr 5555
```

### Vérifier le Firewall
```bash
# Linux - Autoriser le port
sudo ufw allow 5555/tcp

# Windows
# Panneau de configuration > Pare-feu > Autoriser application
```

---

## 🐛 Résolution de Problèmes

### "Connection refused"
- ✅ Le serveur est-il démarré ?
- ✅ L'IP est-elle correcte ?
- ✅ Le firewall bloque-t-il le port 5555 ?

### "Impossible de compiler"
```bash
# Vérifier Java
java --version
javac --version

# Devrait afficher Java 17 ou supérieur
```

### Les joueurs ne se synchronisent pas
- ✅ Les 2 clients sont-ils connectés ?
- ✅ La connexion réseau est-elle stable ?
- ✅ Vérifier les logs : `tail -f logs/server.log`

---

## 💡 Conseils

### Latence Faible
- Utilisez un **câble Ethernet** plutôt que WiFi
- Fermez les applications utilisant beaucoup de bande passante
- Jouez sur le **même réseau local**

### Performance
- Assurez-vous que les deux machines ont Java 17+
- Fermez les applications inutiles
- Utilisez un réseau avec faible latence (<50ms)

---

## 📝 Exemples d'Adresses IP

### Localhost (même machine)
```
IP : localhost
ou
IP : 127.0.0.1
```

### Réseau Local (LAN)
```
IP : 192.168.1.100
ou
IP : 10.0.0.50
```

### Réseau d'Entreprise
```
IP : 172.16.0.100
```

---

## 🎯 Checklist de Démarrage

- [ ] Java 17+ installé
- [ ] Projet compilé (`javac -d bin ...`)
- [ ] Serveur démarré sur une machine
- [ ] IP du serveur notée
- [ ] Firewall configuré (port 5555 ouvert)
- [ ] Client 1 connecté
- [ ] Client 2 connecté
- [ ] Les 2 clients voient "Connecté en tant que Joueur X"
- [ ] Configuration du jeu effectuée
- [ ] **Prêt à jouer !** 🎮

---

## 📚 Documentation Complète

- [`README.md`](README.md) - Vue d'ensemble du projet
- [`NETWORK_GUIDE.md`](NETWORK_GUIDE.md) - Guide détaillé du système réseau
- [`ARCHITECTURE.md`](ARCHITECTURE.md) - Architecture technique

---

**Bon jeu !** 🎮♟️
