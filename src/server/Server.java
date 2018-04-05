package server;

import server.game.Player;
import java.net.*;
import java.util.ArrayList;

public class Server {

    private ArrayList<Player> players;
    private ArrayList<ThreadLobby> lobbies;
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
                new Thread(new ThreadClient(socket,this)).start();
            }
        }
        catch (Exception e)
        {
            System.out.println("Problème : "+e.getMessage());
        }
    }
    public boolean joinLobby(String name, Player player){
        for(ThreadLobby lobby:lobbies){
            if(lobby.getName().equals(name)){
                if(lobby.getArrayPlayers().size() < 2) {
                    player.setLobby(lobby);
                    lobby.joinLobby(player);
                    System.out.println(player.getPseudo()+" a rejoint le lobby "+ lobby.getName()+".");
                    return true;
                }else{
                    player.sendOut("Erreur");
                    return false;
                }
            }
        }
        return false;
    }
    public void createLobby(String name, Player player){
        ThreadLobby lobby = new ThreadLobby(name, player);
        this.lobbies.add(lobby);
        new Thread(lobby).start();
    }
    public String getLobbies(){
        String message = "Liste des lobbies : ";
        for(ThreadLobby lobby:lobbies){
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
