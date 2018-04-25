package server;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//Class server avec les méthodes et variables principales

public class Server {

    private ArrayList<Player> players;
    private ArrayList<Lobby> lobbies;

    private int nbClient = 0;
    private int lastIdClient = 0;
    private int lastIdLobby = 0;

    private byte[] passphrase;
    private boolean secure;

    public Server(int port) {

        this.lobbies = new ArrayList<>();
        this.players = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Serveur sécurisé ? (Y/N)");//On demande à l'admin si il veut lancer un serveur sécurisé ou non
        if (scanner.nextLine().equals("Y")) {
            this.secure = true;
            System.out.println("Entrer votre passphrase :");
            try {
                this.passphrase = sha256digest16(scanner.nextLine());//On hash le mot de passe avec la fonction de condensat
            } catch (Exception e) {
                System.out.println("Erreur chiffrement passphrase...");
                return;
            }
        } else {
            this.secure = false;
        }

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Serveur à l'écoute...");
            while (true) {//On écoute les connexions entrantes
                Socket socket = server.accept();
                System.out.println("Nouveau client est connecté !");
                new Thread(new ThreadServer(socket, this)).start();//On lance un thread pour chaque client connecté
            }
        } catch (Exception e) {
            System.out.println("Déconnexion d'un client...");
        }
    }

    /*Fonction de condensat du mot de passe entrer par l'utilisateur.
     Double interet : - crypter le mot de passe entré
                      - obtenir une passphrase de 16Bytes pour le chiffrement en AES 16Bytes quelque soit la taille du mot de passe entré.
    */
    private byte[] sha256digest16(String clearpassphrase) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(clearpassphrase.getBytes(Charsets.UTF_8));
        byte[] sha256 = digest.digest();
        return Arrays.copyOf(sha256, 16);
    }

    //Fonction pour créer un lobby
    private void giveIdLobby(Lobby lobby) {
        this.lastIdLobby++;
        lobby.setId(this.lastIdLobby);
    }

    //Fonction pour créer un lobby
    void createLobby(String name, Player player) {
        Lobby lobby = new Lobby(name, player, this);
        player.setStatus("creator");
        player.setLobby(lobby);
        giveIdLobby(lobby);
        this.lobbies.add(lobby);
        System.out.println("Création du lobby " + lobby.getName() + " ID " + lobby.getId() +
                " par " + player.getPseudo() + " ID " + player.getId());

    }

    //Fonction pour donner un id à un lobby
    void removeLobby(Lobby lobby) {
        String message = "Suppression du lobby " + lobby.getName() + " ID " + lobby.getId();
        this.lobbies.remove(lobby);
        System.out.println(message);
    }

    //Fonction pour rejoindre un lobby
    boolean joinLobby(int id, Player player) {
        for (Lobby lobby : lobbies) {
            if (id == lobby.getId() && !lobby.isFull()) {
                player.setStatus("opponent");
                player.setLobby(lobby);
                System.out.println(player.getPseudo() + " ID " + player.getId() + " a rejoint le lobby " +
                        lobby.getName() + " ID " + lobby.getId());
                return true;
            }else if(id == lobby.getId() && lobby.isFull()){
                System.out.println(player.getPseudo()+" ID "+player.getId()+" n'a pas pu rejoindre car le lobby est PLEIN");
                return false;
            }
        }
        System.out.println(player.getPseudo()+" ID "+player.getId()+" n'a pas pu rejoindre car le lobby est introuvable");
        return false;
    }

    //Fonction qui retourne la liste des lobbies au format JSONArray afin des les envoyer au client
    JSONArray getLobbies() {
        JSONArray jsonLobbies = new JSONArray();
        for (Lobby lobby : this.lobbies) {
            JSONObject jsonLobby = new JSONObject();
            jsonLobby.put("name", lobby.getName());
            jsonLobby.put("id", lobby.getId());
            jsonLobby.put("full", lobby.isFull());
            jsonLobby.put("creator", lobby.getCreator().toString());
            jsonLobbies.put(jsonLobby);
        }
        return jsonLobbies;
    }

    //Fonction pour donner un id à un client
    void giveIdClient(Player player) {
        this.lastIdClient++;
        player.setId(this.lastIdClient);
    }

    //Fonction pour ajouter un joueur dans l'ArrayList
    void addClient(Player player) {
        this.players.add(player);
        this.nbClient++;
        System.out.println("Ajout du joueur ID " + player.getId());
    }

    //Fonction pour supprimer un joueur de l'ArrayList
    void removeClient(Player player) {
        String message = "Suppression de " + player.getPseudo() + " ID " + player.getId();
        this.nbClient--;
        this.players.remove(player);
        System.out.println(message);
    }

    //Fonction de chiffrement en AES 16Bytes
    String encrypt(String str) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(this.passphrase, "AES"));
        return Base64.encodeBase64URLSafeString(cipher.doFinal(str.getBytes(Charsets.UTF_8)));
    }

    //Fonction de déchiffrement AES 16Bytes
    String decrypt(String encryptedInput) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(this.passphrase, "AES"));
        return new String(cipher.doFinal(Base64.decodeBase64(encryptedInput)), Charsets.UTF_8);
    }

    //On définit ici le nom du serveur
    String getName() { return "hesserver"; }

    boolean isSecure() { return secure; }

    int getClient() { return this.nbClient; }

}
