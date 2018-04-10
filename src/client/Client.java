package client;

import client.gui.Frame;
import org.json.JSONObject;

import java.io.IOException;

public class Client {

    private String pseudo, state, commande;
    private JSONObject jsonServer;
    private int id;
    private Frame frame;

    public Client(Frame frame){
        this.frame = frame;
    }

    public boolean connexion(String host, int port, String pseudo){

        try {
            new Thread(new ThreadCient(host,port,this, this.frame)).start();
            this.pseudo = pseudo;
            this.commande = "rien";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }
    

    public String getPseudo() {
        return this.pseudo;
    }

    public void setPseudo(String pseudo){ this.pseudo=pseudo; }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCommande() {
        return commande;
    }

    public void setCommande(String commande) {
        this.commande = commande;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJsonServer(JSONObject jsonServer) {
        this.jsonServer = jsonServer;
    }

    public JSONObject getJsonServer() {
        return this.jsonServer;
    }
}
