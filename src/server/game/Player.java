package server.game;

//import server.ThreadLobby;
import server.ThreadServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Player {

    private String pseudo;
    //private ThreadLobby lobby;
    private ThreadServer threadServer;
    private int id;

    public Player(ThreadServer threadServer){
        this.threadServer = threadServer;
    }
/*
    public Player(ThreadServer threadServer, String pseudo, ThreadLobby lobby){
        this.threadServer = threadServer;
        this.pseudo = pseudo;
        //this.lobby = lobby;
    }
*/
    public String getPseudo() {
        return this.pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

   // public void setLobby(ThreadLobby lobby){
        //this.lobby = lobby;
        //this.id = this.lobby.getId();
    //}
}
