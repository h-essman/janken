package server;

import game.Game;
import game.Player;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Lobby implements Runnable{

    private String name;
    private Player creator;
    private ArrayList<Player> players;

    public Lobby(String name, Player player) {
        players = new ArrayList<>();
        player.setLobby(this);
        this.creator = player;
        this.players.add(player);
        this.name = name;
    }

    public void run() {
        this.creator.sendOut("/waiting");
        while(this.players.size() < 2){
            try{
                sleep(1000);
            }catch (Exception e) {
                System.out.println(e.getMessage());
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
}