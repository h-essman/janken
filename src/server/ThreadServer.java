package server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ThreadServer implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private Server server;
    private Player player;

    private JSONObject jsonServer, jsonClient;

    private String reception, emission;
    private boolean waiting = true;

    private String command = "server";
    private String argument = "";

    ThreadServer(Socket socket, Server server) {

        this.socket = socket;
        this.server = server;

        this.jsonServer = new JSONObject();
        this.jsonClient = new JSONObject();

        this.player = new Player(this);
        this.server.giveIdClient(this.player);
        this.server.addClient(player);

        try {
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void run() {

        while (true) {
            System.out.println(this.jsonServer.toString() + "\n" + this.reception);
            try {
                sleep(300);
                if (!this.waiting) {
                    int compteur = 0;
                    while (!this.execution(this.reception)) {
                        sleep(500);
                        if(compteur == 2){
                            System.out.println("Kick !!!");
                            throw new java.lang.Exception("kicked");
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
            if (this.server.isSecure()) {
                reception = this.server.decrypt(reception);
            }
            this.jsonClient = new JSONObject(reception);
            this.player.setPseudo(this.jsonClient.getString("pseudo"));

            //TODO Execution command + réponse bool

            switch (this.jsonClient.getString("state")){

                case "server":
                    switch (this.jsonClient.getString("command")) {
                        case "create":
                            this.server.createLobby(this.jsonClient.getString("argument"), this.player);
                            this.command = "create";
                            break;

                        case "join":
                            this.command = "join";
                            this.argument = this.server.joinLobby(this.jsonClient.getInt("argument"), this.player) ? "ok" : "ko";
                            break;
                    }
                    break;

                case "lobby":
                    //ready or not
                    try {
                        switch (this.jsonClient.getString("command")) {
                            case "launch":
                                break;
                            case "ready":
                                if(this.jsonClient.getString("argument").equals("ok")){
                                    this.player.setReady(true);
                                    this.command = "ready";
                                    this.argument = "ok";
                                }else {
                                    this.player.setReady(false);
                                    this.command = "ready";
                                    this.argument = "ko";
                                }
                                break;
                            case "quit":
                                break;
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case "game":
                    break;
            }
            this.jsonServer = this.formJSON();
            this.emission = this.server.isSecure() ? this.server.encrypt(this.jsonServer.toString()) : this.jsonServer.toString();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private JSONObject formJSON() {
        JSONObject client = new JSONObject();
        client.put("server", this.server.getName());
        client.put("clients", this.server.getClient());
        client.put("id", this.player.getId());


        //TODO JSON à envoyer suivant les states
        switch (this.jsonClient.getString("state")){
            case "server":
                client.put("lobbies", this.server.getLobbies());
                break;

            case "lobby":
                client.put("lobby", this.player.getLobby().getName());
                client.put("launchable", this.player.getLobby().isLaunchable());
                client.put("players", this.server.getLobbyPlayers(this.player));
                break;

            case "game":
                break;
        }

        client.put("command", this.command);
        client.put("argument", this.argument);

        this.command = "";
        this.argument = "";


        return client;
    }

    private void kill(String error){
        System.out.println("Arrêt d'un thread : "+error);
        this.server.removeClient(player);
        this.player.quitLobby();
        try {
            this.socket.close();
            System.out.println("Socket fermé");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void setCommand(String command) { this.command = command; }
}
