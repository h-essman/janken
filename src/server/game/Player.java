package server.game;

import server.Lobby;
import server.Server;
import server.ThreadServer;

public class Player {

    private String pseudo, status;
    private ThreadServer threadServer;
    private int id;
    private Lobby lobby;
    private Server server;

    public Player(ThreadServer threadServer){
        this.threadServer = threadServer;
        this.server = this.threadServer.getServer();
        this.server.getArrayPlayers().add(this);
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return getPseudo();
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}
