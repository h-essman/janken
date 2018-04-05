package server;

import server.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadClient implements Runnable {

    private Socket socket;
    private Server server;
    private Player player;

    public ThreadClient(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }

    public void run() {

        try {
            //On initialise le in et out
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            //On attend le pseudo du client
            String pseudo = in.readLine();

            //On crée le joueur sur le serveur
            this.player = new Player(pseudo,in,out);
            out.println("/ready");

            //Choix du client
            while(true){

                //On attend l'ordre
                String ordre = in.readLine();

                if(ordre.contains("/stop")){
                    System.out.println("Instruction STOP reçue");
                    out.println("/stop");
                    close();
                }else if(ordre.contains("/server")){
                    out.println("Il y a "+ this.server.getClient()+" client(s) connecté(s)");
                }else if(ordre.contains("/lobbies")){
                    out.println(this.server.getLobbies());
                }else if(ordre.contains("/join")){
                    out.println("Name ?");
                    String name = in.readLine();
                    if(this.server.joinLobby(name,this.player)) {
                        break;
                    }
                }else if(ordre.contains("/create")){
                    out.println("Name ?");
                    String name = in.readLine();
                    this.server.createLobby(name,this.player);
                    break;
                }else{
                    out.println("Ordre non reconnu : "+ordre);
                }
                System.out.println("Thread reçoit : " + ordre);
            }

        } catch (IOException e) {
            System.out.println("Problème : "+e.getMessage());
        }
    }

    public void close(){
        System.out.println("Le thread s'arrête...");
        this.server.removeClient();
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
