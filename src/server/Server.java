package server;

import org.json.JSONArray;
import org.json.JSONObject;
import server.game.Player;

import java.net.*;
import java.util.ArrayList;

public class Server {
    private String name = "hesserver";
    private ArrayList<Player> players;
    private ArrayList<Lobby> lobbies;
    private int nbClient = 0;
    private int lastIdClient = 0;
    private int lastIdlobby = 0;

    public Server(int port) {
        this.lobbies = new ArrayList<>();
        this.players = new ArrayList<>();

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Serveur à l'écoute...");
            while(true) {
                Socket socket = server.accept();
                System.out.println("Nouveau client est connecté !");
                new Thread(new ThreadServer(socket,this)).start();
            }
        }
        catch (Exception e)
        {
            System.out.println("Déconnexion");
        }
    }

    public String createLobby(String name, Player player){
        Lobby lobby = new Lobby(name, player, this);
        player.setStatus("creator");
        player.setLobby(lobby);
        giveIdLobby(lobby);
        this.lobbies.add(lobby);
        return "Création du lobby "+lobby.getName()+" par "+player.getPseudo()+" "+player.getId();
   }

   public String removeLobby(Lobby lobby){
        String message = "Lobby "+lobby.getName()+" effacé !";
        this.lobbies.remove(lobby);
        return message;
   }

    public String joinLobby(int id, Player player){
        for(Lobby lobby:lobbies){
            if(id == lobby.getId()){
                player.setStatus("opponent");
                player.setLobby(lobby);
                lobby.setFull(true);
                return player.getPseudo()+" "+player.getId()+" a rejoint le lobby "+lobby.getName();
            }
        }
        return "Erreur joinLobby";
    }

    public JSONArray getLobbies(){
        JSONArray jsonLobbies = new JSONArray();
        for (Lobby lobby : this.lobbies) {
            JSONObject jsonLobby = new JSONObject();
            jsonLobby.put("name", lobby.getName());
            jsonLobby.put("id", lobby.getId());
            jsonLobby.put("full", lobby.isFull());
            jsonLobby.put("creator", lobby.getCreator().toString());
            JSONArray jsonPlayers = new JSONArray();
            for (Player player : lobby.getPlayers()) {
                JSONObject jsonPlayer = new JSONObject();
                jsonPlayer.put("pseudo", player.getPseudo());
                jsonPlayer.put("id", player.getId());
                jsonPlayer.put("status", player.getStatus());
                jsonPlayers.put(jsonPlayer);
            }
            jsonLobby.put("players", jsonPlayers.toString());
            jsonLobbies.put(jsonLobby);
        }
        return jsonLobbies;
    }

    public int giveIdClient(Player player){
        this.lastIdClient++;
        player.setId(this.lastIdClient);
        return this.lastIdClient;
    }

    public int giveIdLobby(Lobby lobby){
        this.lastIdlobby++;
        lobby.setId(this.lastIdlobby);
        return this.lastIdlobby;
    }

    public String addClient(Player player) {
        this.players.add(player);
        this.nbClient++;
        return "Ajout du joueur ID "+player.getId();
    }

    public String removeClient(Player player){
        String message = "Suppression de "+ player.getPseudo() + " " + player.getId();
        this.nbClient--;
        this.players.remove(player);
        return message;
    }

    public int getClient() { return this.nbClient; }

    public String getName() { return name; }

    public ArrayList<Lobby> getArrayLobbies(){ return this.lobbies; }

    public ArrayList<Player> getArrayPlayers(){ return this.players; }



     /*
    public void close(Socket socket, Player player){
        System.out.println("Le thread s'arrête...");
        this.removeClient();
        this.players.remove(player);
        player.closeOut();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void close(ThreadServer lobby){
        //killer tout proprement
    }*/


}
