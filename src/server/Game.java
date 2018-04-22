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
            if(this.players.get(1).getChoice() != this.players.get(2).getChoice()) {
                switch (this.players.get(1).getChoice()) {
                    case 1:
                        if (this.players.get(2).getChoice() == 3) {
                            return this.players.get(1);
                        }else{
                            return this.players.get(2);
                        }
                    case 2:
                        if (this.players.get(2).getChoice() == 1) {
                            return this.players.get(1);
                        }else {
                            return this.players.get(2);
                        }
                    case 3:
                        if (this.players.get(2).getChoice() == 2) {
                            return this.players.get(1);
                        }else {
                            return this.players.get(2);
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

        void endGame(Player winner){
            for(Player player:players) {
                player.setReady(false);
                player.setCommand("end");
                if (winner.equals(null)) {
                    player.setArgument("equality");
                } else if (player.equals(winner)) {
                    player.incrScore();
                    player.setArgument("win");
                } else {
                    player.setArgument("loose");
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
