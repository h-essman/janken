package server;

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

    public void run() {
        this.creator.sendOut("/waiting");
        while(this.players.size() < 2){
            try{
                sleep(1000);
            }catch (Exception e) {
                System.out.println("ThreadLobby "+e.getMessage());
            }
        }
        new Game(this.players);
    }

    public void joinLobby(Player player){
        this.players.add(player);
    }

    public int getId(){
        return this.players.size() + 1;
    }

    public String getName() {
        return name;
    }
    public String getPlayers() {
        String message = "";
        for(Player player:players){
            message += "Joueur "+player.getId()+" "+player.getPseudo();
        }
        return message;
    }
    public ArrayList<Player> getArrayPlayers() {
        return this.players;
    }
}