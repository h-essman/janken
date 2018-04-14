package client;

import client.gui.Frame;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ThreadCient implements Runnable {

    private PrintWriter out;
    private BufferedReader in;

    private Client client;
    private Frame frame;

    private JSONObject jsonClient, jsonServer;

    private String reception, emission;
    private boolean waiting = true;

    public ThreadCient(String host, int port, Client client, Frame frame) {

        this.frame = frame;
        this.client = client;
        this.jsonClient = new JSONObject();
        this.jsonServer = new JSONObject();

        try {

            Socket socket = new Socket(host, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        this.jsonClient = formJSON();

        try {
            if (this.client.isSecure()) {
                this.out.println(this.client.encrypt(jsonClient.toString()));
            } else {
                this.out.println(jsonClient.toString());
            }
        }catch (Exception e){
            System.out.println("Erreur first out :"+e.getMessage());
        }
    }

    public void run() {
        while (true) {

            //System.out.println(this.jsonClient.toString() + "\n" + this.reception);

            try {
                sleep(200);

                if (!this.waiting) {
                    int compteur = 0;

                    while (!this.execution(this.reception)) {
                        sleep(2000);
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
                System.out.println("Problème run ThreadClient : " + e.getMessage());
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
            this.frame.getPanel().refresh();
            this.client.setId(this.jsonServer.getInt("id"));


            //ICI


            this.jsonClient = formJSON();

            if (this.client.isSecure()) {
                this.emission = this.client.encrypt(this.jsonClient.toString());
            } else {
                this.emission = this.jsonClient.toString();
            }

            this.client.setCommand("");
            this.client.setArgumentString("");
            this.client.setArgumentInt(0);

            return true;
        } catch (Exception e) {
            System.out.println("Problème execution : " + e.getMessage());

            return false;
        }

    }

    private JSONObject formJSON() {

        JSONObject client = new JSONObject();
        client.put("pseudo", this.client.getPseudo());
        client.put("id", this.client.getId());
        client.put("state", this.client.getState());
        client.put("command", this.client.getCommand());
        client.put("argumentString", this.client.getArgumentString());
        client.put("argumentInt", this.client.getArgumentInt());
        return client;

    }
}
