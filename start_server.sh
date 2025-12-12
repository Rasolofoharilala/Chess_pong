#!/bin/bash

# Script de démarrage du serveur Chess-Pong

echo "=========================================="
echo "  Serveur Chess-Pong - Mode Multijoueur"
echo "=========================================="
echo ""

# Récupérer l'adresse IP locale
IP_ADDRESS=$(hostname -I | awk '{print $1}')
echo "Adresse IP du serveur : $IP_ADDRESS"
echo "Port : 5555"
echo ""
echo "Les clients peuvent se connecter avec cette adresse IP"
echo ""

# Compiler si nécessaire
echo "Compilation des fichiers Java..."
javac -d bin $(find . -name "*.java")

if [ $? -eq 0 ]; then
    echo "✓ Compilation réussie"
    echo ""
    echo "Démarrage du serveur..."
    echo ""
    
    # Lancer le serveur
    cd bin
    java ServerLauncher
else
    echo "✗ Erreur de compilation"
    exit 1
fi
