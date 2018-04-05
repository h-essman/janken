package server.game;

import server.ThreadLobby;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Player {

    private PrintWriter out;
    private BufferedReader in;
    private String pseudo;
    private ThreadLobby lobby;
    private int id;

    public Player(String pseudo, BufferedReader in, PrintWriter out){
        this.pseudo = pseudo;
        this.in = in;
        this.out = out;
    }

    public Player(String pseudo, BufferedReader in, PrintWriter out, ThreadLobby lobby){
        this.pseudo = pseudo;
        this.in = in;
        this.out = out;
        this.lobby = lobby;
        this.id = this.lobby.getId();
    }

    public void sendOut(String message) {
        this.out.println(message);
    }

    public String getIn() {
        try {
            return this.in.readLine();
        }catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public int getId() {
        return this.id;
    }

    public void setLobby(ThreadLobby lobby){
        this.lobby = lobby;
        this.id = this.lobby.getId();
    }
    public void closeOut(){
        this.out.close();
    }
}
