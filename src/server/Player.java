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

    public int getChoice() { return choice; }

    void setReady(boolean ready) { this.ready = ready; }

    void goServer(){this.threadServer.setCommand("server");}

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public int getScore() {
        return score;
    }

    public void incrScore() {
        this.score ++;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void quitLobby(){

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

    void setCommand(String command){
        this.threadServer.setCommand(command);
    }

    void setArgument(Object argument){
        this.threadServer.setArgument(argument);
    }
}
