package client;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ThreadCient implements Runnable{

    private JSONArray json;
    private PrintWriter out;
    private BufferedReader in;
    private String reception;
    private Client client;

    public ThreadCient(String host, int port, Client client) throws IOException {
        Socket socket = new Socket(host, port);
        this.client = client;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    public void run() {
        try {
            while(true){
                if(this.in.ready()){
                    this.execution(reception);
                    this.out.println(this.json.toString());
                    this.reception = "";
                }else{
                    this.reception += this.in.readLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error ThreadClient : "+e.getMessage());
        }

    }
    private boolean execution(String reception){
        if(true){
            return true;
        }else {
            return false;
        }
    }
    public JSONArray getJson() {
        return json;
    }

}
