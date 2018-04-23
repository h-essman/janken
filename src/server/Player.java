package server;

public class Player {

    private String pseudo, status;
    private boolean ready = false;
    private ThreadServer threadServer;
    private int id;
    private Lobby lobby;
    private int choice = 0;
    private int score = 0;

    Player(ThreadServer threadServer) {
        this.threadServer = threadServer;
        this.status = "new";
    }

    void setLobby(Lobby lobby) {
        this.lobby = lobby;
        lobby.getPlayers().add(this);
    }

    void quitLobby(){

        if(!this.status.equals("new")) {
            this.lobby.getPlayers().remove(this);
            if(this.lobby.isInGame()){
                this.lobby.getGame().quitGame();
            }
        }

        if (this.status.equals("creator")) {
            this.lobby.kill();
        }

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

    int getChoice() { return choice; }

    void setReady(boolean ready) { this.ready = ready; }

    void goServer(){this.threadServer.setCommand("server");}

    void setChoice(int choice) { this.choice = choice; }

    int getScore() { return score; }

    void incrScore() { this.score = this.score + 1; }

    void setScore(int score) { this.score = score; }

    void setCommand(String command){ this.threadServer.setCommand(command); }

    void setArgument(Object argument){ this.threadServer.setArgument(argument); }
}
