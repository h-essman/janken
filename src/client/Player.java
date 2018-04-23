package client;

public class Player {

    private String pseudo, status, gameResult;
    private boolean ready = false;
    private boolean waiting = false;
    private int id, score;
    private int choice = 0;

    Player(String pseudo){
        this.pseudo = pseudo;
    }

    String getPseudo() { return this.pseudo; }

    int getId() { return id; }

    void setId(int id) { this.id = id; }

    public boolean isReady() { return ready; }

    void setReady(boolean ready) { this.ready = ready; }

    void setStatus(String status) { this.status = status; }

    public String getStatus() { return status; }

    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

    public boolean isWaiting() { return waiting; }

    public void setWaiting(boolean waiting) { this.waiting = waiting; }

    public void setGameResult(String gameResult) { this.gameResult = gameResult; }

    public String getGameResult() { return gameResult; }

    public void setChoice(int choice) { this.choice = choice; }

    public int getChoice() { return choice; }
}
