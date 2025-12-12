#!/bin/bash

# Script pour démarrer le jeu en mode HÔTE (Joueur 1)
# L'hôte démarre le serveur et joue en tant que Joueur 1 (Blanc)

echo "======================================"
echo "  Chess-Pong - Mode HÔTE (Joueur 1)  "
echo "======================================"
echo ""
echo "Démarrage du serveur et du jeu..."
echo "Vous serez le Joueur 1 (Blanc - paddle du bas)"
echo "En attente d'un client (Joueur 2)..."
echo ""

# Compiler si nécessaire
if [ ! -d "bin" ] || [ -z "$(ls -A bin)" ]; then
    echo "Compilation du projet..."
    javac -d bin -sourcepath . HostLauncher.java Network/*.java Affichage/*.java Game/*.java Piece/*.java
    if [ $? -ne 0 ]; then
        echo "Erreur de compilation!"
        exit 1
    fi
fi

# Lancer l'hôte
java -cp bin HostLauncher

