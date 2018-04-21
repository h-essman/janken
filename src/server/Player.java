package server;

public class Player {

    private String pseudo, status;
    private boolean ready = false;
    private ThreadServer threadServer;
    private int id;
    private Lobby lobby;

    Player(ThreadServer threadServer) {
        this.threadServer = threadServer;
        this.status = "new";
    }

    void setLobby(Lobby lobby) {
        this.lobby = lobby;
        lobby.getPlayers().add(this);
    }

    String getPseudo() {
        return this.pseudo;
    }

    void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return getPseudo();
    }

    Lobby getLobby() {
        return lobby;
    }

    boolean isReady() { return ready; }

    void setReady(boolean ready) { this.ready = ready; }

    void goServer(){this.threadServer.setCommand("server");}

    public void quitLobby(){
        if(!this.status.equals("new")) {
            this.lobby.getPlayers().remove(this);
        }
        if (this.status.equals("creator")) {
            this.lobby.kill();
        }
    }
}
