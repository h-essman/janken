package server;

import game.Player;
import java.net.*;
import java.util.ArrayList;

public class Server {

    private ArrayList<Player> players;
    private ArrayList<Lobby> lobbies;
    private int nbClient = 0;

    public Server(int port) {
        this.lobbies = new ArrayList<>();
        this.players = new ArrayList<>();

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Serveur à l'écoute...");
            while(true) {
                Socket socket = server.accept();
                System.out.println("Nouveau client est connecté !");
                addClient();
                new Thread(new ThreadServer(socket,this)).start();
            }
        }
        catch (Exception e)
        {
            System.out.println("Problème : "+e.getMessage());
        }
    }
    public void joinLobby(String name, Player player){
        for(Lobby lobby:lobbies){
            if(lobby.getName().equals(name)){
                player.setLobby(lobby);
                lobby.joinLobby(player);
            }
        }
    }
    public void createLobby(String name, Player player){
        Lobby lobby = new Lobby(name, player);
        this.lobbies.add(lobby);
        new Thread(lobby).start();
    }
    public String getLobbies(){
        String message = "Liste des lobbies : ";
        for(Lobby lobby:lobbies){
            message += lobby.getName()+" / "+lobby.getPlayers()+" ||| ";
        }
        return message;
    }
    public void addClient() {
        this.nbClient++;
    }
    public void removeClient(){
        this.nbClient--;
    }
    public int getClient() {
        return this.nbClient;
    }
}
