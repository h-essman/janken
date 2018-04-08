package client;
import client.gui.Frame;

import javax.swing.*;

public class Client {

    private String pseudo;
    private Frame frame;
    private Thread thread;

    public Client(){
        this.frame = new Frame(this);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    public boolean connexion(String host, int port){
        try{
            this.thread = new Thread(new ThreadCient(host,port,this));
            this.thread.start();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    

    public String getPseudo() {
        return this.pseudo;
    }
    public void setPseudo(String pseudo){ this.pseudo=pseudo; }
}
