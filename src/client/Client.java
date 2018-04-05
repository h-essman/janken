package client;
import server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Client {

    private String pseudo;
    private boolean waiting = false;
    private String message;
    private PrintWriter out;

    public Client(String host, int port, String pseudo){

        try {
            Socket socket = new Socket(host, port);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = out;

            Scanner clavier = new Scanner(System.in);
            out.println(pseudo);
            in.readLine();
            while (true) {

                if(!waiting) {
                    System.out.print("commande : ");
                    this.message = clavier.nextLine();
                    out.println(message);
                }
                String reponse = in.readLine();
                if (reponse.equals("/stop")) {
                    System.out.println("Thread a reçu un stop donc arrêt du thread...");
                    break;
                }else if (reponse.equals("/stopwaiting")) {
                    this.waiting = false;
                    System.out.println("Opponent found !");
                    System.out.println("Game launched");
                }else if (reponse.equals("/waiting")) {
                    this.waiting = true;
                    System.out.println("Waiting...");
                }else{
                    System.out.println("réponse : " + reponse);
                }
            }
            System.out.println("Fin du client. Salut !");
            in.close();
        } catch (Exception e) {
            System.err.println("Problème client : " + e.getMessage());
        }
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo=pseudo;
    }

    public void sendOut(String message) {
        this.out.println(message);
    }
}
