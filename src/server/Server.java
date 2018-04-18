package server;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import server.game.Player;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Server {

    private String name = "hesserver";

    private ArrayList<Player> players;
    private ArrayList<Lobby> lobbies;

    private int nbClient = 0;
    private int lastIdClient = 0;
    private int lastIdlobby = 0;

    private byte[] passphrase;
    private boolean secure;

    public Server(int port) {

        this.lobbies = new ArrayList<>();
        this.players = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Serveur sécurisé ? (Y/N)");
        if (scanner.nextLine().equals("Y")) {
            this.secure = true;
            System.out.println("Entrer votre passphrase :");
            try {
                this.passphrase = sha256digest16(scanner.nextLine());
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

            while (true) {
                Socket socket = server.accept();
                System.out.println("Nouveau client est connecté !");
                new Thread(new ThreadServer(socket, this)).start();
            }

        } catch (Exception e) {
            System.out.println("Déconnexion d'un client...");
        }

    }

    public void createLobby(String name, Player player) {

        Lobby lobby = new Lobby(name, player, this);
        player.setStatus("creator");
        player.setLobby(lobby);
        giveIdLobby(lobby);
        this.lobbies.add(lobby);

        System.out.println("Création du lobby " + lobby.getName() + " ID " + lobby.getId() +
                " par " + player.getPseudo() + " ID " + player.getId());

    }

    public void removeLobby(Lobby lobby) {
        String message = "Suppression du lobby " + lobby.getName() + " ID " + lobby.getId();
        this.lobbies.remove(lobby);
        System.out.println(message);
    }

    public void joinLobby(int id, Player player) {
        for (Lobby lobby : lobbies) {
            if (id == lobby.getId()) {
                player.setStatus("opponent");
                player.setLobby(lobby);
                System.out.println(player.getPseudo() + " ID " + player.getId() + " a rejoint le lobby " +
                        lobby.getName() + " ID " + lobby.getId());
            }
        }
        System.out.println("Erreur joinLobby");
    }

    public JSONArray getLobbies() {
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

    public JSONArray getLobbyPlayers(Player player){
        JSONArray jsonPlayers = new JSONArray();
        Lobby lobby = player.getLobby();
        for (Player p : lobby.getPlayers()) {
            JSONObject jsonPlayer = new JSONObject();
            jsonPlayer.put("pseudo", p.getPseudo());
            jsonPlayer.put("id", p.getId());
            jsonPlayer.put("status", p.getStatus());
            jsonPlayers.put(jsonPlayer);
        }
        return jsonPlayers;
    }

    public int giveIdClient(Player player) {
        this.lastIdClient++;
        player.setId(this.lastIdClient);
        return this.lastIdClient;
    }

    public int giveIdLobby(Lobby lobby) {
        this.lastIdlobby++;
        lobby.setId(this.lastIdlobby);
        return this.lastIdlobby;
    }

    public Lobby getLobbyById(int id){
        for (Lobby lobby:lobbies){
            if (lobby.getId() == id){
                return lobby;
            }
        }
        return null;
    }

    public void addClient(Player player) {
        this.players.add(player);
        this.nbClient++;
        System.out.println("Ajout du joueur ID " + player.getId());
    }

    public void removeClient(Player player) {
        String message = "Suppression de " + player.getPseudo() + " ID " + player.getId();
        this.nbClient--;
        this.players.remove(player);
        System.out.println(message);
    }

    public String encrypt(String str) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(this.passphrase, "AES"));
        return Base64.encodeBase64URLSafeString(cipher.doFinal(str.getBytes(Charsets.UTF_8)));
    }

    public String decrypt(String encryptedInput) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(this.passphrase, "AES"));
        return new String(cipher.doFinal(Base64.decodeBase64(encryptedInput)), Charsets.UTF_8);
    }

    public byte[] sha256digest16(String clearpassphrase) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(clearpassphrase.getBytes(Charsets.UTF_8));
        byte[] sha256 = digest.digest();
        return Arrays.copyOf(sha256, 16);
    }

    public int getClient() { return this.nbClient; }

    public String getName() { return name; }

    public ArrayList<Lobby> getArrayLobbies() { return this.lobbies; }

    public ArrayList<Player> getArrayPlayers() { return this.players; }

    public boolean isSecure() { return secure; }

}
