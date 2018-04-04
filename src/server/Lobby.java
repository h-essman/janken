package server;

import game.Game;
import game.Player;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Lobby implements Runnable{

    private String name;
    private ArrayList<Player> players;

    public Lobby(String name, Player player) {
        players = new ArrayList<>();
        player.setLobby(this);
        this.players.add(player);
        this.name = name;
    }

    public void run() {
        this.players.get(0).sendOut("Waiting for opponent... skip");
        while(this.players.size() < 1){
            try{
                sleep(1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.players.get(0).sendOut("Opponent found !");
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
}