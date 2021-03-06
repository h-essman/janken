package server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//Class Lobby

class Lobby {

    private String name;
    private Server server;
    private int id;
    private Player creator;
    private boolean inGame = false;
    private ArrayList<Player> players;
    private Game game;


    Lobby(String name, Player creator, Server server) {
        this.players = new ArrayList<>();
        this.name = name;
        this.creator = creator;
        this.server = server;
    }

    //Fonction permettant de déterminer si les joueurs sont prêts à lancer le jeu
    boolean isLaunchable() {
        for(Player player:players){
            if(!player.isReady()){
                return false;
            }
        }
        return isFull();
    }

    //Fonction permettant de fermer proprement le lobby
    void kill(){
        for(Player player:players){
            player.goServer();
        }
        this.server.removeLobby(this);
    }

    //Fonction permettant de fermer proprement le lobby
    void newGame() {
        this.game = new Game(this);
        this.inGame = true;
    }

    //Fonction recupérer les infos sur les joueurs du lobby au format JSONArray afin de l'intégrer dans le JSON à envoyer au client
    JSONArray getJSONArrayPlayers(){
        JSONArray jsonPlayers = new JSONArray();
        for (Player p : players) {
            JSONObject jsonPlayer = new JSONObject();
            jsonPlayer.put("pseudo", p.getPseudo());
            jsonPlayer.put("id", p.getId());
            jsonPlayer.put("status", p.getStatus());
            jsonPlayer.put("ready", p.isReady());
            jsonPlayer.put("score", p.getScore());
            jsonPlayers.put(jsonPlayer);
        }
        return jsonPlayers;
    }

    boolean isInGame() {
        return inGame;
    }

    Game getGame() {
        return game;
    }

    String getName() {
        return name;
    }

    void setId(int id) { this.id = id; }

    int getId() { return id; }

    Player getCreator() {
        return creator;
    }

    ArrayList<Player> getPlayers() { return players; }

    void setInGame(boolean inGame) { this.inGame = inGame; }

    boolean isFull() {
        return this.players.size() >= 2;
    }
}
