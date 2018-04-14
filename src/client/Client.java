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

    private String pseudo, state, command, argumentString;
    private int id, argumentInt;

    private Frame frame;
    private JSONObject jsonServer;

    private byte[] passphrase;
    private boolean secure;

    public Client(Frame frame) {
        this.frame = frame;
    }

    public boolean connexion(String host, int port, String pseudo, boolean secure) {

        try {

            this.pseudo = pseudo;
            this.command = "";
            this.secure = secure;

            new Thread(new ThreadCient(host, port, this, this.frame)).start();

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public String encrypt(String str) throws Exception {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(this.passphrase, "AES"));

        return Base64.encodeBase64URLSafeString(cipher.doFinal(str.getBytes(Charsets.UTF_8)));

    }

    public String decrypt(String encryptedInput) throws Exception {

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

    public String getPseudo() { return this.pseudo; }

    public void setPseudo(String pseudo) { this.pseudo = pseudo; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getCommand() { return command; }

    public void setCommand(String commande) { this.command = commande; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public void setJsonServer(JSONObject jsonServer) { this.jsonServer = jsonServer; }

    public JSONObject getJsonServer() { return this.jsonServer; }

    public String getArgumentString() { return argumentString; }

    public void setArgumentString(String argument) { this.argumentString = argument; }

    public int getArgumentInt() { return argumentInt; }

    public void setArgumentInt(int argumentInt) { this.argumentInt = argumentInt; }

    public boolean isSecure() { return secure; }
}
