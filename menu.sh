#!/bin/bash

# Menu principal pour Chess-Pong

clear
echo "╔════════════════════════════════════════╗"
echo "║      CHESS-PONG - Menu Principal       ║"
echo "╚════════════════════════════════════════╝"
echo ""
echo "Que voulez-vous faire ?"
echo ""
echo "  1) Jouer en mode solo (local)"
echo "  2) Démarrer un serveur (multijoueur)"
echo "  3) Se connecter à un serveur (client)"
echo "  4) Test multijoueur (local)"
echo "  5) Compiler le projet"
echo "  6) Voir les logs du serveur"
echo "  7) Quitter"
echo ""
read -p "Votre choix (1-7) : " choice

case $choice in
    1)
        echo ""
        echo "📦 Compilation..."
        javac -d bin $(find . -name "*.java") 2>/dev/null
        if [ $? -eq 0 ]; then
            echo "✓ Compilation réussie"
            echo "🎮 Lancement du jeu..."
            cd bin
            java Main
        else
            echo "❌ Erreur de compilation"
        fi
        ;;
    2)
        echo ""
        echo "🖥️  Démarrage du serveur..."
        ./start_server.sh
        ;;
    3)
        echo ""
        echo "🎮 Connexion au serveur..."
        ./start_client.sh
        ;;
    4)
        echo ""
        echo "🧪 Test multijoueur local..."
        ./test_multiplayer.sh
        ;;
    5)
        echo ""
        echo "📦 Compilation du projet..."
        mkdir -p bin
        javac -d bin $(find . -name "*.java")
        if [ $? -eq 0 ]; then
            echo "✓ Compilation réussie"
        else
            echo "❌ Erreur de compilation"
        fi
        ;;
    6)
        echo ""
        if [ -f logs/server.log ]; then
            echo "📋 Logs du serveur :"
            echo "══════════════════════════════"
            tail -n 50 logs/server.log
        else
            echo "❌ Aucun log serveur trouvé"
        fi
        ;;
    7)
        echo ""
        echo "👋 Au revoir !"
        exit 0
        ;;
    *)
        echo ""
        echo "❌ Choix invalide"
        ;;
esac

echo ""
read -p "Appuyez sur Entrée pour continuer..."
