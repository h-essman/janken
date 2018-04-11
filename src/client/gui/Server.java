package client.gui;

import client.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Server extends Panel implements ActionListener {

    private DefaultListModel listLobbiesModel;
    private JLabel pseudoServer, nbClientServer;
    private JButton btnCreate, btnJoin;
    private JSONArray arrayLobbies;
    private JList<String> listLobbies;
    private ArrayList<Integer> correspondance;
    private int choice;

    public Server(Client client, Frame frame){
        super(client, frame);
        this.setLayout(null);
        this.correspondance = new ArrayList<>();

        this.pseudoServer = new JLabel();
        this.pseudoServer.setBounds(10,10,300,20);
        this.add(this.pseudoServer);

        this.nbClientServer = new JLabel();
        this.nbClientServer.setBounds(10,30,300,20);
        this.add(this.nbClientServer);

        this.btnCreate = new JButton("Créer");
        this.btnCreate.setBounds(300,100,100,40);
        this.add(this.btnCreate);
        this.btnCreate.addActionListener(this);

        this.btnJoin = new JButton("Rejoindre");
        this.btnJoin.setBounds(300,40,100,40);
        this.add(btnJoin);
        this.btnJoin.addActionListener(this);

        this.listLobbiesModel = new DefaultListModel();
        this.listLobbies = new JList(listLobbiesModel);
        this.listLobbies.setBounds(30, 70, 200, 200);
        this.add(this.listLobbies);

    }

    public void actualiser(){
        this.pseudoServer.setText("Connecté au serveur : "+ this.getClient().getJsonServer().get("server"));
        this.nbClientServer.setText("Il y a  "+ this.getClient().getJsonServer().get("clients")+" connecté(s) au serveur.");

        this.arrayLobbies = this.getClient().getJsonServer().getJSONArray("lobbies");
        int i = 0;
        this.listLobbiesModel.removeAllElements();
        for(Object lobby:arrayLobbies){
            JSONObject jsonLobby = (JSONObject) lobby;
            String message = jsonLobby.getString("name")+" créé par "+jsonLobby.getString("creator");
            if(jsonLobby.getBoolean("full")){
                message += " PLEIN";
            }
            this.listLobbiesModel.add(i, message);
            this.correspondance.add(i, jsonLobby.getInt("id"));
            i++;
        }
    }
    public void actionPerformed(ActionEvent e){

        if (e.getSource() == this.btnCreate) {
            String inputName = JOptionPane.showInputDialog(this,
                    "Entrer le nom de votre lobby :",
                    "Création Lobby",3);
            if(!inputName.equals("")){
                this.getClient().setCommand("create");
                this.getClient().setArgumentString(inputName);
                this.getFrame().goNext("lobby");
            }else{
                JOptionPane.showMessageDialog(this,
                        "Vous n'avez pas rentré de nom !",
                        "Error",2
                );
            }
        }else if (e.getSource() == this.btnJoin){
            for(Object lobby:arrayLobbies){
                JSONObject jsonLobby = (JSONObject) lobby;
                if(jsonLobby.getInt("id")==this.correspondance.get(this.choice) && !jsonLobby.getBoolean("full")){
                    this.getClient().setCommand("join");
                    this.getClient().setArgumentInt(jsonLobby.getInt("id"));
                    this.getFrame().goNext("lobby");
                }else{
                    JOptionPane.showMessageDialog(this,
                            "Ce lobby est plein !",
                            "Error",2
                    );
                }
            }
        }else if(e.getSource() == this.listLobbies){
            this.choice = this.listLobbies.getSelectedIndex();
        }
    }

}
