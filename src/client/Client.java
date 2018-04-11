package client;

import client.gui.Frame;
import org.json.JSONObject;

public class Client {

    private String pseudo, state, command, argumentString;
    private JSONObject jsonServer;
    private int id, argumentInt;
    private Frame frame;

    public Client(Frame frame){
        this.frame = frame;
    }

    public boolean connexion(String host, int port, String pseudo){

        try {
            this.pseudo = pseudo;
            this.command = "";

            new Thread(new ThreadCient(host,port,this, this.frame)).start();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    public String getCommand() {
        return command;
    }

    public void setCommand(String commande) {
        this.command = commande;
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

    public String getArgumentString() { return argumentString; }

    public void setArgumentString(String argument) { this.argumentString = argument; }

    public int getArgumentInt() { return argumentInt; }

    public void setArgumentInt(int argumentInt) { this.argumentInt = argumentInt; }
}
