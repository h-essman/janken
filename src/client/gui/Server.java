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
    private JList listLobbies;

    private JLabel pseudoServer, nbClientServer;
    private JButton btnCreate, btnJoin;

    private JSONArray arrayLobbies;

    private ArrayList<Integer> correspondance;
    private int choice;

    Server(Client client, Frame frame) {

        super(client, frame);
        this.setLayout(null);
        this.correspondance = new ArrayList<>();

        this.pseudoServer = new JLabel();
        this.pseudoServer.setBounds(10, 10, 300, 20);
        this.add(this.pseudoServer);

        this.nbClientServer = new JLabel();
        this.nbClientServer.setBounds(10, 30, 300, 20);
        this.add(this.nbClientServer);

        this.btnCreate = new JButton("Créer");
        this.btnCreate.setBounds(300, 100, 100, 40);
        this.add(this.btnCreate);
        this.btnCreate.addActionListener(this);

        this.btnJoin = new JButton("Rejoindre");
        this.btnJoin.setBounds(300, 40, 100, 40);
        this.add(btnJoin);
        this.btnJoin.addActionListener(this);

        this.listLobbiesModel = new DefaultListModel();
        this.listLobbies = new JList(listLobbiesModel);
        this.listLobbies.setBounds(30, 70, 200, 200);
        this.add(this.listLobbies);
        JScrollPane scrollListLobbies = new JScrollPane(listLobbies);
        scrollListLobbies.setBounds(30, 70, 200, 200);
        this.add(scrollListLobbies);
        listLobbies.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    public void refresh() {

        this.pseudoServer.setText("Connecté au serveur : " + this.getClient().getJsonServer().get("server"));
        this.nbClientServer.setText("Il y a  " + this.getClient().getJsonServer().get("clients") + " connecté(s) au serveur.");

        this.arrayLobbies = this.getClient().getJsonServer().getJSONArray("lobbies");
        int i = 0;

        this.choice = this.listLobbies.getSelectedIndex();
        this.listLobbiesModel.removeAllElements();

        for (Object lobby : arrayLobbies) {

            JSONObject jsonLobby = (JSONObject) lobby;
            String message = jsonLobby.getString("name") + " créé par " + jsonLobby.getString("creator");

            if (jsonLobby.getBoolean("full")) {
                message += " PLEIN";
            }

            this.listLobbiesModel.add(i, message);
            this.correspondance.add(i, jsonLobby.getInt("id"));
            i++;

        }

        this.listLobbies.setSelectedIndex(this.choice);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.btnCreate) {

            String inputName = JOptionPane.showInputDialog(this,
                    "Entrer le nom de votre lobby :",
                    "Création Lobby", JOptionPane.QUESTION_MESSAGE);

            if (!inputName.equals("")) {

                this.getClient().setCommand("create");
                this.getClient().setArgument(inputName);

            } else {

                this.getFrame().showError("Vous n'avez pas rentré de nom !");

            }
        } else if (e.getSource() == this.btnJoin) {

            for (Object lobby : arrayLobbies) {
                JSONObject jsonLobby = (JSONObject) lobby;

                if (jsonLobby.getInt("id") == this.correspondance.get(this.choice) && !jsonLobby.getBoolean("full")) {

                    this.getClient().setCommand("join");
                    this.getClient().setArgument(jsonLobby.getInt("id"));

                } else if (jsonLobby.getInt("id") == this.correspondance.get(this.choice) && jsonLobby.getBoolean("full")) {

                    this.getFrame().showError("Ce lobby est plein !");

                }
            }
        }
    }

}
