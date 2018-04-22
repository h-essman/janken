package client;

import client.gui.Frame;
import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class Client {

    private Player player;

    private String state, command;
    private Object argument;

    private Frame frame;
    private JSONObject jsonServer;

    private byte[] passphrase;
    private boolean secure;

    public Client(Frame frame) {
        this.frame = frame;
    }

    public boolean connexion(String host, int port, String pseudo, boolean secure) {

        try {

            this.command = "";
            this.secure = secure;
            this.player = new Player(pseudo);
            this.frame.goPanel("connexion");
            new Thread(new ThreadCient(host, port, this, this.frame, this.player)).start();

            return true;
        } catch (Exception e) {
            System.out.println("Echec de la connexion : "+e.getMessage());
            this.frame.goPanel("menu");
            return false;
        }
    }

    String encrypt(String str) throws Exception {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(this.passphrase, "AES"));

        return Base64.encodeBase64URLSafeString(cipher.doFinal(str.getBytes(Charsets.UTF_8)));

    }

    String decrypt(String encryptedInput) throws Exception {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(this.passphrase, "AES"));

        return new String(cipher.doFinal(Base64.decodeBase64(encryptedInput)), Charsets.UTF_8);

    }

    private byte[] sha256digest16(String clearpassphrase) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(clearpassphrase.getBytes(Charsets.UTF_8));
        byte[] sha256 = digest.digest();

        return Arrays.copyOf(sha256, 16);

    }

    public void setPassphrase(String passphrase){

        try {
            this.passphrase = sha256digest16(passphrase);
        }catch (Exception e){
            System.out.println("Erreur lors du chiffrement de la passphrase : "+e.getMessage());
        }

    }



    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    String getCommand() { return command; }

    public void setCommand(String commande) { this.command = commande; }

    void setJsonServer(JSONObject jsonServer) { this.jsonServer = jsonServer; }

    public JSONObject getJsonServer() { return this.jsonServer; }

    Object getArgument() { return argument; }

    public void setArgument(String argument) { this.argument = argument; }

    public void setArgument(int argument) { this.argument = argument; }

    boolean isSecure() { return secure; }

    public Player getPlayer() { return player; }
}
