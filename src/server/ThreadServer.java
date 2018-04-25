package server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;

/*Class ThreadServer qui gère la communication avec le Thread du client
On y trouve notamment la gestion des commandes du client,
la formation du JSON
 */

class ThreadServer implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private Server server;
    private Player player;

    private JSONObject jsonServer, jsonClient;

    private String reception, emission;
    private boolean waiting = true;

    private String command = "server";
    private Object argument = "";

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

    /*Fonction RUN car c'est un Thread
    Elle gère la communication avec le thread du client
     */
    public void run() {
        while (true) {
            try {
                sleep(300);
                if (!this.waiting) {
                    int compteur = 0;
                    while (!this.execution(this.reception)) {//Si l'éxecution échoue cela signifie que le serveur ne comprend le JSON du client et donc qu'il y un mauvais mot de passe ou une incoherence du JSON
                        sleep(500);
                        if(compteur == 2){//Si l'éxecution échoue plus de 2 fois alors on vire le client
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
                this.kill(e.getMessage());//Si on a une erreur une kill proprement
                break;
            }
            //System.out.println(this.reception + "\n" + this.jsonServer.toString());
        }
    }

    //Fonction d'execution du JSON du client
    private boolean execution(String reception) {
        try {
            if (this.server.isSecure()) {//On déchiffre si la connexion est sécurisée
                reception = this.server.decrypt(reception);
            }
            this.jsonClient = new JSONObject(reception);
            this.player.setPseudo(this.jsonClient.getString("pseudo"));

            //Suivant le panel (l'état) du client on execute les commandes
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
                    switch (this.jsonClient.getString("command")) {

                        case "game":
                            this.player.getLobby().newGame();
                            break;

                        case "ready":
                            if (this.jsonClient.getString("argument").equals("ok")) {
                                this.player.setReady(true);
                                this.command = "ready";
                                this.argument = "ok";
                            } else {
                                this.player.setReady(false);
                                this.command = "ready";
                                this.argument = "ko";
                            }
                            break;

                        case "quit":
                            this.player.setReady(false);
                            this.player.quit();
                            this.command = "quit";
                            this.argument = "server";
                            break;
                        }
                    break;

                case "game":
                    switch (this.jsonClient.getString("command")) {

                        case "choice":
                            this.player.setChoice(this.jsonClient.getInt("argument"));
                            if(this.player.getLobby().getGame().done()){
                                this.player.getLobby().getGame().endGame();
                            }else{
                                this.command = "wait";
                            }
                            break;

                        case "quit":
                            this.player.setReady(false);
                            this.player.getLobby().getGame().quitGame();
                            break;
                    }
                    break;
                case "result":
                    switch (this.jsonClient.getString("command")) {

                        case "continue":
                            this.player.getLobby().getGame().continueGame();
                            break;

                        case "ready":
                            if (this.jsonClient.getString("argument").equals("ok")) {
                                this.player.setReady(true);
                                this.command = "ready";
                                this.argument = "ok";
                            } else {
                                this.player.setReady(false);
                                this.command = "ready";
                                this.argument = "ko";
                            }
                            break;

                        case "quit":
                            this.player.setReady(false);
                            this.player.getLobby().getGame().quitGame();
                            break;
                    }
                    break;
            }
            this.jsonServer = this.formJSON();// On forme le JSON du serveur
            this.emission = this.server.isSecure() ? this.server.encrypt(this.jsonServer.toString()) : this.jsonServer.toString();//On l'envoie chiffré si la connexion est sécurisée
            return true;//On retourne true si l'execution s'est bien passée
        } catch (Exception e) {
            return false;
        }
    }

    //Fonction de formation du JSON serveur
    private JSONObject formJSON() {
        JSONObject client = new JSONObject();
        client.put("server", this.server.getName());
        client.put("clients", this.server.getClient());
        client.put("id", this.player.getId());

        //On envoie un les infos nécessaires au client suivant son état
        switch (this.jsonClient.getString("state")){

            case "server":
                client.put("lobbies", this.server.getLobbies());
                break;

            case "lobby":
                client.put("lobby", this.player.getLobby().getName());
                client.put("launchable", this.player.getLobby().isLaunchable());
                client.put("players", this.player.getLobby().getJSONArrayPlayers());
                break;

            case "game":
                client.put("players", this.player.getLobby().getJSONArrayPlayers());
                break;

            case "result":
                client.put("players", this.player.getLobby().getJSONArrayPlayers());
                client.put("launchable", this.player.getLobby().getGame().ready());
                break;
        }

        client.put("command", this.command);
        client.put("argument", this.argument);

        this.command = "";
        this.argument = "";

        return client;
    }

    //Fonction permettant de quitter proprement
    private void kill(String error){
        System.out.println("Arrêt d'un thread : "+error);
        this.server.removeClient(player);
        this.player.quit();
        try {
            this.socket.close();
            System.out.println("Socket fermé");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    void setCommand(String command) { this.command = command; }

    void setArgument(Object argument) { this.argument = argument; }
}
