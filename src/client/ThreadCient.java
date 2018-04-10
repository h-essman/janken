package client;
import client.gui.Frame;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ThreadCient implements Runnable{

    private JSONObject jsonClient;
    private JSONObject jsonServer;
    private PrintWriter out;
    private BufferedReader in;
    private String reception;
    private Client client;
    private Frame frame;
    private String status = "reception";

    public ThreadCient(String host, int port, Client client, Frame frame) {
        this.frame = frame;
        this.client = client;
        try {
            Socket socket = new Socket(host, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        this.jsonClient = new JSONObject();
        this.jsonServer = new JSONObject();

        this.jsonClient = formJSON();
        this.out.println(jsonClient.toString());
    }
    public void run() {
        while (true) {
            System.out.println(this.jsonClient.toString() + "\n" + this.reception);
            try {
                sleep(500);
                if (this.status.equals("emission")) {
                    int compteur = 0;
                    while(!this.execution(this.reception)){
                        sleep(2000);
                        compteur ++;
                        System.out.println("Try " + compteur);
                    }
                    this.status = "reception";
                    this.out.println(this.jsonClient.toString());
                } else {
                    this.reception = this.in.readLine();
                    this.status = "emission";
                }
            } catch (InterruptedException e) {
                System.out.println("Problème run ThreadClient : " + e.getMessage());
                break;
            } catch (IOException e){
                System.out.println("Problème run ThreadClient : " + e.getMessage());
                break;
            }
        }
    }

    public boolean execution(String reception){
        try {
            this.jsonServer = new JSONObject(reception);
            this.client.setJsonServer(this.jsonServer);
            this.frame.getPanel().actualiser();


            //ICI


            this.jsonClient = formJSON();
            return true;
        }catch (Exception e){
            System.out.println("Client : " + e.getMessage());
            return false;
        }
    }

    public JSONObject formJSON(){
        JSONObject client=new JSONObject();
        client.put("pseudo", this.client.getPseudo());
        client.put("id", this.client.getId());
        client.put("state", this.client.getState());
        client.put("command", this.client.getCommande());
        return client;
    }
}
