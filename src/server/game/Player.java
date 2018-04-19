package server.game;

import server.Lobby;
import server.ThreadServer;

public class Player {

    private String pseudo, status;
    private ThreadServer threadServer;
    private int id;
    private Lobby lobby;

    public Player(ThreadServer threadServer) {
        this.threadServer = threadServer;
        this.status = "new";
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
        lobby.getPlayers().add(this);
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

    public void goServer(){this.threadServer.setCommand("server");}
}
