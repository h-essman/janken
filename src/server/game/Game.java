package server.game;

import java.util.ArrayList;

public class Game {
    private Player creator, opponent;
    private ArrayList<Player> players;

    public Game(ArrayList<Player> players){
        this.creator = players.get(0);
        this.opponent = players.get(1);
    }

}
