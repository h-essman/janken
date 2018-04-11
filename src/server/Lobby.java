package server;

import server.game.Player;

import java.util.ArrayList;

public class Lobby {

    private String name;
    private Server server;
    private int id;
    private Player creator;
    private boolean full;
    private ArrayList<Player> players;


    public Lobby(String name, Player creator, Server server){
        this.players = new ArrayList<>();
        this.name = name;
        this.creator = creator;
        this.server = server;
        this.players.add(this.creator);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setId(int id) { this.id = id; }

    public int getId() {
        return id;
    }

    public Player getCreator() {
        return creator;
    }

    public boolean isFull() { return full; }

    public void setFull(boolean full) { this.full = full; }
}
