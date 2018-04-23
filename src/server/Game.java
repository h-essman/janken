package server;

import java.util.ArrayList;

class Game {

        private ArrayList<Player> players;
        private Lobby lobby;

        Game(Lobby lobby){
            this.lobby = lobby;
            this.players = this.lobby.getPlayers();
            launchGame();
        }

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

        boolean done(){
            for(Player player:players){
                if(player.getChoice() == 0){
                    return false;
                }
            }
            return true;
        }

        private void launchGame(){
            for(Player player:this.players){
                player.setCommand("game");
            }
            this.lobby.setInGame(true);
        }

        void continueGame(){
            for(Player player:this.players){
            player.setCommand("game");
            player.setChoice(0);
            }
        }

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

        boolean ready(){
            for (Player player:players){
                if(!player.isReady()){
                    return false;
                }
            }
            return true;
        }

}
