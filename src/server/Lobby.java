package server;

import server.game.Player;

import java.util.ArrayList;

public class Lobby {

    private String name;
    private Server server;
    private int id;
    private Player creator;
    private ArrayList<Player> players;


    public Lobby(String name, Player creator, Server server) {
        this.players = new ArrayList<>();
        this.name = name;
        this.creator = creator;
        this.server = server;
    }

    public boolean isFull() {
        if (this.players.size() < 2){
            return false;
        }else{
            return true;
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Player getCreator() {
        return creator;
    }

}
