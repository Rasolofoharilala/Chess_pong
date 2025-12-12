#!/bin/bash

# Script de test local du mode multijoueur
# Lance automatiquement un serveur et deux clients sur la même machine

echo "=========================================="
echo "  Test Mode Multijoueur - Chess-Pong"
echo "=========================================="
echo ""

# Compiler les fichiers
echo "📦 Compilation du projet..."
javac -d bin $(find . -name "*.java") 2>&1

if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation"
    exit 1
fi

echo "✓ Compilation réussie"
echo ""

# Créer un dossier pour les logs
mkdir -p logs

# Lancer le serveur en arrière-plan
echo "🖥️  Démarrage du serveur..."
cd bin
java Network.GameServer > ../logs/server.log 2>&1 &
SERVER_PID=$!
echo "   PID Serveur: $SERVER_PID"

# Attendre que le serveur démarre
sleep 2

# Vérifier que le serveur est lancé
if ! ps -p $SERVER_PID > /dev/null; then
    echo "❌ Le serveur n'a pas pu démarrer"
    cat ../logs/server.log
    exit 1
fi

echo "✓ Serveur démarré sur le port 5555"
echo ""

# Instructions pour l'utilisateur
echo "🎮 Le serveur est prêt !"
echo ""
echo "Vous pouvez maintenant lancer les clients manuellement :"
echo "   1. Ouvrir un nouveau terminal"
echo "   2. Exécuter : cd $(pwd)"
echo "   3. Exécuter : java ClientLauncher"
echo "   4. Entrer 'localhost' comme adresse IP"
echo "   5. Répéter pour le 2ème joueur"
echo ""
echo "Ou exécuter dans deux terminaux séparés :"
echo "   Terminal 2 : cd $(pwd) && java ClientLauncher"
echo "   Terminal 3 : cd $(pwd) && java ClientLauncher"
echo ""
echo "📋 Logs du serveur : logs/server.log"
echo ""
echo "Pour arrêter le serveur, appuyez sur Ctrl+C"
echo ""

# Fonction de nettoyage
cleanup() {
    echo ""
    echo "🛑 Arrêt du serveur..."
    kill $SERVER_PID 2>/dev/null
    echo "✓ Serveur arrêté"
    exit 0
}

# Capturer Ctrl+C
trap cleanup SIGINT SIGTERM

# Afficher les logs du serveur en temps réel
echo "════════════════════════════════════════"
echo "         Logs du Serveur"
echo "════════════════════════════════════════"
tail -f ../logs/server.log
