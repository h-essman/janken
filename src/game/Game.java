package game;

import java.util.ArrayList;

public class Game {
    public Game(ArrayList<Player> players){
        for(Player player:players){
            player.sendOut("Game launched !");
        }
    }
}
