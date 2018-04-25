package server;

import java.util.ArrayList;

//Class Game pour la gestion logique du jeu Pierre Feuille Ciseaux

class Game {

        private ArrayList<Player> players;
        private Lobby lobby;

        Game(Lobby lobby){
            this.lobby = lobby;
            this.players = this.lobby.getPlayers();
            launchGame();
        }

        //Fonction permettant de déterminer le gagnant
        Player winner(){
            if(this.players.get(0).getChoice() != this.players.get(1).getChoice()) {
                switch (this.players.get(0).getChoice()) {
                    case 1:
                        if (this.players.get(1).getChoice() == 3) {
                            return this.players.get(0);
                        }else{
                            return this.players.get(1);
                        }
                    case 2:
                        if (this.players.get(1).getChoice() == 1) {
                            return this.players.get(0);
                        }else {
                            return this.players.get(1);
                        }
                    case 3:
                        if (this.players.get(1).getChoice() == 2) {
                            return this.players.get(0);
                        }else {
                            return this.players.get(1);
                        }
                }
            }
            return null;
        }

        //Fonction permettant de déterminer si tous les joueurs ont joué
        boolean done(){
            for(Player player:players){
                if(player.getChoice() == 0){
                    return false;
                }
            }
            return true;
        }

        //Fonction permettant le lancement d'une nouvelle partie
        private void launchGame(){
            for(Player player:this.players){
                player.setCommand("game");
            }
            this.lobby.setInGame(true);
        }

        //Fonction permettant la continuation d'une partie existante
        void continueGame(){
            for(Player player:this.players){
            player.setCommand("game");
            player.setChoice(0);
            }
        }

        //Fonction de fin de partie avec envoie des commandes adequates suivant le résultat
        void endGame(){
            Player winner = winner();
            boolean equality = true;
            for(Player player:players) {
                player.setReady(false);
                if (player.equals(winner)) {
                    equality = false;
                }
            }
            if(!equality){
                for(Player player:players) {
                    player.setCommand("end");
                    if (player.equals(winner)) {
                        player.incrScore();
                        player.setArgument("win");
                    }else{
                        player.setArgument("loose");
                    }
                }
            }else{
                for(Player player:players) {
                    player.setCommand("end");
                    player.setArgument("equality");
                }
            }
        }

        //Fonction pour quitter le jeu de manière propre
        void quitGame(){
            for(Player player:players) {
                player.setChoice(0);
                player.setScore(0);
                player.setReady(false);
                player.setCommand("quit");
                player.setArgument("lobby");
            }
            this.lobby.setInGame(false);
        }

        //Fonction pour déterminer si tous les joueurs sont prêts pour continuer la partie
        boolean ready(){
            for (Player player:players){
                if(!player.isReady()){
                    return false;
                }
            }
            return true;
        }

}
