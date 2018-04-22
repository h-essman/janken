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

    private byte[] sha256digest16(String clearpassphrase) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(clearpassphrase.getBytes(Charsets.UTF_8));
        byte[] sha256 = digest.digest();
        return Arrays.copyOf(sha256, 16);
    }

    private void giveIdLobby(Lobby lobby) {
        this.lastIdLobby++;
        lobby.setId(this.lastIdLobby);
    }

    void createLobby(String name, Player player) {

        Lobby lobby = new Lobby(name, player, this);
        player.setStatus("creator");
        player.setLobby(lobby);
        giveIdLobby(lobby);
        this.lobbies.add(lobby);

        System.out.println("Création du lobby " + lobby.getName() + " ID " + lobby.getId() +
                " par " + player.getPseudo() + " ID " + player.getId());

    }

    void removeLobby(Lobby lobby) {
        String message = "Suppression du lobby " + lobby.getName() + " ID " + lobby.getId();
        this.lobbies.remove(lobby);
        System.out.println(message);
    }

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

    void giveIdClient(Player player) {
        this.lastIdClient++;
        player.setId(this.lastIdClient);
    }

    void addClient(Player player) {
        this.players.add(player);
        this.nbClient++;
        System.out.println("Ajout du joueur ID " + player.getId());
    }

    void removeClient(Player player) {
        String message = "Suppression de " + player.getPseudo() + " ID " + player.getId();
        this.nbClient--;
        this.players.remove(player);
        System.out.println(message);
    }

    String encrypt(String str) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(this.passphrase, "AES"));
        return Base64.encodeBase64URLSafeString(cipher.doFinal(str.getBytes(Charsets.UTF_8)));
    }

    String decrypt(String encryptedInput) throws Exception{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(this.passphrase, "AES"));
        return new String(cipher.doFinal(Base64.decodeBase64(encryptedInput)), Charsets.UTF_8);
    }

    String getName() { return "hesserver"; }

    boolean isSecure() { return secure; }

    int getClient() { return this.nbClient; }

}
