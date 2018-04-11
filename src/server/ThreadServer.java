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
    private Server server;
    private Player player;
    private JSONObject jsonServer;
    private JSONObject jsonClient;
    private String reception;
    private PrintWriter out;
    private BufferedReader in;
    private String status = "reception";
    private String command = "";
    private String argument = "";

    public ThreadServer(Socket socket, Server server){
        this.socket = socket;
        this.server = server;

        this.player = new Player(this);
        this.server.giveIdClient(this.player);

        try {
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        this.jsonServer = new JSONObject();
        this.jsonClient = new JSONObject();
    }

    public void run() {

        while (true) {
            System.out.println(this.jsonServer.toString() + "\n" + this.reception);
            try {
                sleep(500);
                if (this.status.equals("emission")) {
                    int compteur = 0;
                    while(!this.execution(this.reception)){
                        sleep(2000);
                        compteur ++;
                        System.out.println("Try " + compteur);
                    }
                    this.out.println(jsonServer.toString());
                    this.status = "reception";
                } else {
                    this.reception = this.in.readLine();
                    this.status = "emission";
                }
            }catch(IOException e){
                System.out.println("Erreur IO run ThreadServer : "+e.getMessage());
                this.server.removeClient();
                if(player.getStatus().equals("creator")) {
                    this.server.removeLobby(this.player.getLobby());
                }
                break;
            }catch (InterruptedException e){
                System.out.println("Erreur SLEEP run ThreadServer : "+e.getMessage());
                break;
            }
        }
    }
    public boolean execution(String reception){
        try {
            this.jsonClient = new JSONObject(reception);
            this.player.setPseudo(this.jsonClient.getString("pseudo"));

            if(this.jsonClient.getString("command").equals("create")){
                this.server.createLobby(this.jsonClient.getString("argumentString"),this.player);
            }
            if(this.jsonClient.getString("command").equals("join")){
                this.server.joinLobby(this.jsonClient.getInt("argumentInt"),this.player);
            }

            //ICI

            this.jsonServer = formJSON();
            this.command = "";
            this.argument = "";
            return true;
        }catch (Exception e){
            System.out.println(this.server.getArrayLobbies().toString());
            return false;
        }
    }
    public JSONObject formJSON(){
        JSONObject client=new JSONObject();
        client.put("server", this.server.getName());
        client.put("clients", this.server.getClient());
        client.put("id", this.player.getId());
        client.put("command", this.getCommand());
        client.put("lobbies", this.server.getLobbies());
        return client;
    }

    public String getCommand() { return command; }

    public void setCommand(String command) { this.command = command; }

    public String getArgument() { return argument; }

    public void setArgument(String argument) { this.argument = argument; }

    public Server getServer() { return server; }
}
