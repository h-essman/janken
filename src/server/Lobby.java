package server;

import java.util.ArrayList;

class Lobby {

    private String name;
    private Server server;
    private int id;
    private Player creator;
    private ArrayList<Player> players;


    Lobby(String name, Player creator, Server server) {
        this.players = new ArrayList<>();
        this.name = name;
        this.creator = creator;
        this.server = server;
    }

    boolean isFull() {
        return this.players.size() >= 2;
    }

    boolean isLaunchable() {
        for(Player player:players){
            if(!player.isReady()){
                return false;
            }
        }
        return isFull();
    }

    String getName() {
        return name;
    }

    void setId(int id) { this.id = id; }

    int getId() { return id; }

    Player getCreator() {
        return creator;
    }

    void kill(){
        for(Player player:players){
            player.goServer();
        }
        this.server.removeLobby(this);
    }

    ArrayList<Player> getPlayers() { return players; }

}
