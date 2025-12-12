#!/bin/bash

# Script de démarrage du client Chess-Pong

echo "=========================================="
echo "  Client Chess-Pong - Mode Multijoueur"
echo "=========================================="
echo ""

# Demander l'IP du serveur
read -p "Entrez l'IP du serveur (ou appuyez sur Entrée pour localhost) : " SERVER_IP

if [ -z "$SERVER_IP" ]; then
    SERVER_IP="localhost"
fi

echo ""
echo "Connexion au serveur : $SERVER_IP:5555"
echo ""

# Compiler si nécessaire
echo "Compilation des fichiers Java..."
javac -d bin $(find . -name "*.java")

if [ $? -eq 0 ]; then
    echo "✓ Compilation réussie"
    echo ""
    echo "Démarrage du client..."
    echo ""
    
    # Lancer le client
    cd bin
    java ClientLauncher
else
    echo "✗ Erreur de compilation"
    exit 1
fi
