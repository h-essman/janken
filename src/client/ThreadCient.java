package client;

import client.gui.Frame;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;

/*Class ThreadClient qui gère la communication avec le Thread du serveur
On y trouve notamment la gestion des réponses du serveur,
la formation du JSON,
la gestion de la synchronisation pour le rafraichissement des infos affichées sur le panel en cours.
 */

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

    /*Fonction RUN car c'est un Thread
    Elle gère la communication avec le thread du serveur
    Le client parle en envoyant un JSON, attend la reponse du serveur, execute les ordres du serveur, repond au serveur...
     */
    public void run() {
        while (true) {
            try {
                sleep(200);
                if (!this.waiting) {
                    int compteur = 0;
                    while (!this.execution(this.reception)) {//Si l'éxecution échoue cela signifie que le client ne comprend le JSON du serveur et donc qu'il y un mauvais mot de passe ou une incoherence du JSON
                        sleep(500);
                        if(compteur == 2){//Si l'éxecution échoue plus de 2 fois alors on se déconnecte
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
                this.kill(e.getMessage());//Si on a une erreur une kill proprement
                break;
            }
            //System.out.println(this.reception + "\n" + this.jsonClient.toString());
        }
    }

    //Fonction d'execution du JSON du serveur
    private boolean execution(String reception) {
        try {
            if (this.client.isSecure()) {//On déchiffre si la connexion est sécurisée
                reception = this.client.decrypt(reception);
            }
            this.jsonServer = new JSONObject(reception);
            this.client.setJsonServer(this.jsonServer);
            this.player.setId(this.jsonServer.getInt("id"));

            //Suivant la commande du serveur on execute des actions
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
                    this.player.setReady(false);
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
                    switch (this.jsonServer.getString("argument")) {

                        case "server":
                            this.frame.goPanel("server");
                            break;

                        case "lobby":
                            this.player.setScore(0);
                            this.player.setGameResult("none");
                            this.frame.goPanel("lobby");
                            break;

                        case "menu":
                            this.kill("retour au menu");
                            break;
                    }
                    break;
            }

            this.jsonClient = formJSON();// On forme le JSON du client
            this.emission = this.client.isSecure() ? this.client.encrypt(this.jsonClient.toString()) : this.jsonClient.toString();//On l'envoie chiffré si la connexion est sécurisée

            /* Gestion des incohérences entre le panel et le JSON
            Dans une logique de synchronisation il est possible que le panel attende des données pas encore reçu
            il faut alors attendre le prochain JSON
             */
            try {
                this.frame.getPanel().refresh();
            }catch (Exception e){
                System.out.println("Attente de la synchronisation : "+e.getMessage());
            }

            return true;//On retourne true si l'execution s'est bien passée
        } catch (Exception e) {
            System.out.println("Problème execution : "+e.getMessage());
            return false;
        }
    }

    //Fonction de formation du JSON client
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

    //Fonction permettant de quitter proprement
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
