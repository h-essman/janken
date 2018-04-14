package server;

import org.json.JSONObject;
import server.game.Player;

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

    private String command = "";
    private String argument = "";

    public ThreadServer(Socket socket, Server server) {

        this.socket = socket;
        this.server = server;

        this.jsonServer = new JSONObject();
        this.jsonClient = new JSONObject();

        this.player = new Player(this);
        this.server.giveIdClient(this.player);

        System.out.println(this.server.addClient(player));

        try {
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void run() {

        while (true) {

            //System.out.println(this.jsonServer.toString() + "\n" + this.reception);

            try {
                sleep(300);

                if (!this.waiting) {
                    int compteur = 0;

                    while (!this.execution(this.reception)) {
                        sleep(2000);
                        if(compteur == 3){
                            System.out.println("Kick !!!");
                            this.socket.close();
                            break;
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

            if (this.jsonClient.getString("command").equals("create")) {
                System.out.println(this.server.createLobby(this.jsonClient.getString("argumentString"), this.player));
            }
            if (this.jsonClient.getString("command").equals("join")) {
                System.out.println(this.server.joinLobby(this.jsonClient.getInt("argumentInt"), this.player));
            }

            //ICI

            this.jsonServer = formJSON();
            this.command = "";
            this.argument = "";

            if (this.server.isSecure()) {
                this.emission = this.server.encrypt(this.jsonServer.toString());
            } else {
                this.emission = this.jsonServer.toString();
            }

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
        client.put("command", this.getCommand());
        client.put("lobbies", this.server.getLobbies());

        return client;

    }

    public void kill(String error){

        System.out.println("Arrêt d'un thread : "+error);
        System.out.println(this.server.removeClient(player));

        if (player.getStatus().equals("creator")) {
            System.out.println(this.server.removeLobby(this.player.getLobby()));
        }
        try {
            this.socket.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public String getCommand() { return command; }

    public void setCommand(String command) { this.command = command; }

    public String getArgument() { return argument; }

    public void setArgument(String argument) { this.argument = argument; }

    public Server getServer() { return server; }

}
