package server.game;

import java.util.ArrayList;

public class Game {
    private Player creator, opponent;
    private ArrayList<Player> players;

    public Game(ArrayList<Player> players){
        this.creator = players.get(0);
        this.opponent = players.get(1);
        this.players = players;
        this.creator.sendOut("/stopwaiting");
        this.opponent.sendOut("Game launched");
        this.start();
    }

    public void start() {
        for(Player player:this.players){
            String echo =player.getIn();
            player.sendOut(echo);
        }
    }
}
