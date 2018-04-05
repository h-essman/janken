package game;

import java.util.ArrayList;

public class Game {
    private Player creator, opponent;
    public Game(ArrayList<Player> players){
        this.creator = players.get(0);
        this.opponent = players.get(1);
        this.creator.sendOut("/stopwaiting");
        this.opponent.sendOut("Game launched");
    }
}
