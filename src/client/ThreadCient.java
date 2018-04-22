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
    private Player player;
    private Frame frame;

    private JSONObject jsonClient, jsonServer;

    private String reception, emission;
    private boolean waiting = true;

    ThreadCient(String host, int port, Client client, Frame frame, Player player) throws Exception {

        this.frame = frame;
        this.client = client;
        this.player = player;
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
            this.player.setId(this.jsonServer.getInt("id"));

            //TODO gestion des réponses du serveur aux commandes

            switch (this.jsonServer.getString("command")){
                case "server":
                    this.frame.goPanel("server");
                    break;
                case "join":
                    if(this.jsonServer.getString("argument").equals("ok")){
                        this.player.setStatus("opponent");
                        this.frame.goPanel("lobby");
                    }else{
                        this.frame.showError("Impossible de rejoindre ce lobby...");
                    }
                    break;
                case "create":
                    this.player.setStatus("creator");
                    this.frame.goPanel("lobby");
                    break;
                case "ready":
                    if(this.jsonServer.getString("argument").equals("ok")){
                        this.player.setReady(true);
                    }else{
                        this.player.setReady(false);
                    }
                    break;
                case "game":
                    this.frame.goPanel("game");
                    break;
                case "wait":
                    this.player.setWaiting(true);
                    break;
                case "end":
                    this.player.setWaiting(false);
                    switch (this.jsonServer.getString("argument")){
                        case "win":
                            for(Object player:jsonServer.getJSONArray("players")){
                                JSONObject jsonPlayer = (JSONObject) player;
                                if(this.player.getId() == jsonPlayer.getInt("id")){
                                    this.player.setScore(jsonPlayer.getInt("score"));
                                    this.player.setGameResult("win");
                                    this.frame.goPanel("result");
                                }
                            }
                            break;
                        case "loose":
                            this.player.setGameResult("loose");
                            this.frame.goPanel("result");
                            break;
                        case "equality":
                            this.player.setGameResult("equality");
                            this.frame.goPanel("result");
                            break;
                    }
                    break;
                case "quit":
                    this.player.setReady(false);
                    if(this.jsonServer.getString("argument").equals("server")){
                        this.frame.goPanel("server");
                    }else if(this.jsonServer.getString("argument").equals("lobby")) {
                        this.player.setScore(0);
                        this.player.setGameResult("none");
                        this.frame.goPanel("lobby");
                    }else if(this.jsonServer.getString("argument").equals("menu")){
                        this.kill("retour au menu");
                    }
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

        client.put("pseudo", this.player.getPseudo());
        client.put("id", this.player.getId());

        client.put("state", this.client.getState());
        client.put("command", this.client.getCommand());
        client.put("argument", this.client.getArgument());

        if(this.client.getState().equals("game")){
            client.put("score", this.player.getScore());
        }

        this.client.setCommand("");
        this.client.setArgument("");
        return client;
    }

    private void kill(String message){
        this.frame.goPanel("menu");
        this.frame.showError("Vous avez été déconnecté du serveur !");
        try {
            this.socket.close();
            System.out.println("Socket fermé : "+message);
        }catch (Exception e){
            System.out.println("Erreur fermeture socket : "+e.getMessage());
        }
    }
}
