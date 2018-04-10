package client;
import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ThreadCient implements Runnable{

    private JSONObject json;
    private PrintWriter out;
    private BufferedReader in;
    private String reception;
    private Client client;
    private JSONParser parser;

    public ThreadCient(String host, int port, Client client) throws IOException {
        Socket socket = new Socket(host, port);
        this.client = client;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.parser = new JSONParser();
    }
    public void run() {
        try {
            while(true){
                if(this.in.ready()){
                    this.execution(reception);
                    this.out.println(this.json.toString());
                }else{
                    this.reception = this.in.readLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error ThreadClient : "+e.getMessage());
        }

    }

    private boolean execution(String reception){
        try {
            JSONObject receptionJSON = new JSONObject();
            receptionJSON = (JSONObject) this.parser.parse(reception);


            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject formJSON(String commande){
        JSONObject client=new JSONObject();
        client.put("pseudo", this.client.getPseudo());
        client.put("id", this.client.getId());
        client.put("state", this.client.getState());
        client.put("command", this.client.getCommande());
        return client;
    }
}
