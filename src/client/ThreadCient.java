package client;

import client.gui.Frame;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ThreadCient implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private Client client;
    private Frame frame;

    private JSONObject jsonClient, jsonServer;

    private String reception, emission;
    private boolean waiting = true;

    ThreadCient(String host, int port, Client client, Frame frame) throws Exception {

        this.frame = frame;
        this.client = client;
        this.jsonClient = new JSONObject();
        this.jsonServer = new JSONObject();

        this.socket = new Socket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.jsonClient = formJSON();
        this.out.println(this.client.isSecure() ? this.client.encrypt(jsonClient.toString()) : jsonClient.toString());

    }

    public void run() {
        while (true) {
            System.out.println(this.jsonClient.toString() + "\n" + this.reception);
            try {
                sleep(200);
                if (!this.waiting) {
                    int compteur = 0;
                    while (!this.execution(this.reception)) {
                        sleep(500);
                        if(compteur == 2){
                            System.out.println("Deconnexion !");
                            throw new java.lang.Exception("disconnected");
                        }
                        compteur++;
                        System.out.println("Try " + compteur);
                    }
                    this.out.println(this.emission);
                    this.waiting = true;
                } else {
                    this.reception = this.in.readLine();
                    this.waiting = false;
                }
            } catch (Exception e) {
                this.kill(e.getMessage());
                break;
            }
        }
    }

    private boolean execution(String reception) {
        try {
            if (this.client.isSecure()) {
                reception = this.client.decrypt(reception);
            }
            this.jsonServer = new JSONObject(reception);
            this.client.setJsonServer(this.jsonServer);
            this.client.setId(this.jsonServer.getInt("id"));

            //TODO gestion des réponses du serveur aux commandes

            switch (this.jsonServer.getString("command")){
                case "join":
                    if(this.jsonServer.getString("response").equals("ok")){
                        this.client.goNext("lobby");
                    }else{
                        this.frame.showError("Impossible de rejoindre ce lobby...");
                    }
                    break;
                case "create":
                    this.client.goNext("lobby");
                    break;
                case "server":
                    this.frame.goNext("server");
                    break;
            }

            this.jsonClient = formJSON();
            this.emission = this.client.isSecure() ? this.client.encrypt(this.jsonClient.toString()) : this.jsonClient.toString();
            try {
                this.frame.getPanel().refresh();
            }catch (Exception e){
                System.out.println("Attente de la synchronisation : "+e.getMessage());
            }
            return true;
        } catch (Exception e) {
            System.out.println("Problème execution : "+e.getMessage());
            return false;
        }
    }

    private JSONObject formJSON() {
        JSONObject client = new JSONObject();

        //TODO JSON à envoyer suivant les states

        client.put("pseudo", this.client.getPseudo());
        client.put("id", this.client.getId());
        client.put("state", this.client.getState());
        client.put("command", this.client.getCommand());
        client.put("argument", this.client.getArgument());

        this.client.setCommand("");
        this.client.setArgument("");
        return client;
    }

    private void kill(String error){
        this.client.goNext("menu");
        this.frame.showError("Vous avez été déconnecté du serveur !");
        try {
            this.socket.close();
            System.out.println("Socket fermé : "+error);
        }catch (Exception e){
            System.out.println("Erreur fermeture socket : "+e.getMessage());
        }
    }
}
