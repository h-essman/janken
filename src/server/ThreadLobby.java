/*package server;

import server.game.Game;
import server.game.Player;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class ThreadLobby implements Runnable{

    private String name;
    private Player creator;
    private ArrayList<Player> players;

    public ThreadLobby(String name, Player player) {
        players = new ArrayList<>();
        player.setLobby(this);
        this.creator = player;
        this.players.add(player);
        this.name = name;
        System.out.println("Lobby "+this.name+" créé par "+this.creator.getPseudo());
    }
}*/