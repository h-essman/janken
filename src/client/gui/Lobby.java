package client.gui;

import client.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Lobby extends Panel implements ActionListener {

    private DefaultListModel listPlayersModel;
    private JList listPlayers;

    private JLabel nameLobby, privateLobby;
    private JButton btnReady, btnQuit, btnLaunch;

    private JSONArray arrayPlayers;

    private ArrayList<Integer> correspondance;
    private int choice;

    Lobby(Client client, Frame frame) {

        super(client, frame);
        this.setLayout(null);
        this.correspondance = new ArrayList<>();

        this.nameLobby = new JLabel();
        this.nameLobby.setBounds(30, 10, 150, 20);
        this.add(this.nameLobby);

        /* TODO quand les lobby auront des mots de passe
        this.privateLobby = new JLabel("PRIVE");
        this.privateLobby.setBounds(200, 10, 50, 20);
        this.add(this.privateLobby);
        */

        this.btnReady = new JButton("Prêt");
        this.btnReady.setBackground(Color.red);
        this.btnReady.setBounds(20, 40, 80, 30);
        this.add(this.btnReady);
        this.btnReady.addActionListener(this);

        this.btnQuit = new JButton("Quitter");
        this.btnQuit.setBounds(120, 40, 80, 30);
        this.add(btnQuit);
        this.btnQuit.addActionListener(this);

        this.btnLaunch = new JButton("GO!");
        this.btnLaunch.setBounds(220, 40, 60, 30);
        this.add(btnLaunch);
        this.btnLaunch.addActionListener(this);
        this.btnLaunch.setVisible(false);

        this.listPlayersModel = new DefaultListModel();
        this.listPlayers = new JList(listPlayersModel);
        this.listPlayers.setBounds(10, 80, 300, 50);
        this.add(this.listPlayers);
        JScrollPane scrollListLobbies = new JScrollPane(listPlayers);
        scrollListLobbies.setBounds(10, 80, 300, 50);
        this.add(scrollListLobbies);
        listPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void refresh() {
        this.nameLobby.setText(this.getClient().getJsonServer().getString("lobby"));

        if(this.getClient().getPlayer().isReady()){
            this.btnReady.setBackground(Color.green);
        }else {
            this.btnReady.setBackground(Color.red);
        }

        if(this.getClient().getPlayer().getStatus().equals("creator") && this.getClient().getJsonServer().getBoolean("launchable")){
            this.btnLaunch.setVisible(true);
        }else{
            this.btnLaunch.setVisible(false);
        }

        this.arrayPlayers = this.getClient().getJsonServer().getJSONArray("players");
        int i = 0;

        this.choice = this.listPlayers.getSelectedIndex();
        this.listPlayersModel.removeAllElements();

        for (Object player : arrayPlayers) {

            JSONObject jsonPlayer = (JSONObject) player;
            String message = jsonPlayer.getString("pseudo") + " - " + jsonPlayer.getString("status");

            if (jsonPlayer.getBoolean("ready")) {
                message += " - PRET";
            }else{
                message += " - PAS PRET";
            }

            this.listPlayersModel.add(i, message);
            this.correspondance.add(i, jsonPlayer.getInt("id"));
            i++;

        }

        this.listPlayers.setSelectedIndex(this.choice);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == this.btnReady) {
            if(this.getClient().getPlayer().isReady()){
                this.getClient().setCommand("ready");
                this.getClient().setArgument("ko");
            }else{
                this.getClient().setCommand("ready");
                this.getClient().setArgument("ok");
            }
        }

        if(e.getSource() == this.btnQuit) {
            this.getClient().setCommand("quit");
        }

        if(e.getSource() == this.btnLaunch) {
            this.getClient().setCommand("game");
        }

    }
}
