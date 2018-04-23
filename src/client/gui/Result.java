package client.gui;

import client.Client;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Result extends Panel implements ActionListener {

    private DefaultListModel listPlayersModel;
    private JList listPlayers;

    private JLabel gameResult;
    private JButton btnContinue, btnQuit, btnLaunch;

    private JSONArray arrayPlayers;

    Result(Client client, Frame frame) {

        super(client, frame);
        this.setLayout(null);

        this.gameResult = new JLabel();
        this.gameResult.setBounds(30, 10, 150, 20);
        this.add(this.gameResult);

        this.btnContinue = new JButton("Continuer");
        this.btnContinue.setBackground(Color.red);
        this.btnContinue.setBounds(20, 40, 100, 30);
        this.add(this.btnContinue);
        this.btnContinue.addActionListener(this);

        this.btnQuit = new JButton("Quitter");
        this.btnQuit.setBounds(140, 40, 80, 30);
        this.add(btnQuit);
        this.btnQuit.addActionListener(this);

        this.btnLaunch = new JButton("GO!");
        this.btnLaunch.setBounds(240, 40, 60, 30);
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

    @Override
    public void refresh() {
        this.gameResult.setText(this.getClient().getPlayer().getGameResult().toUpperCase());

        if(this.getClient().getPlayer().isReady()){
            this.btnContinue.setBackground(Color.green);
        }else {
            this.btnContinue.setBackground(Color.red);
        }

        if(this.getClient().getPlayer().getStatus().equals("creator") && this.getClient().getJsonServer().getBoolean("launchable")){
            this.btnLaunch.setVisible(true);
        }else{
            this.btnLaunch.setVisible(false);
        }

        this.arrayPlayers = this.getClient().getJsonServer().getJSONArray("players");
        int i = 0;

        this.listPlayersModel.removeAllElements();

        for (Object player : arrayPlayers) {

            JSONObject jsonPlayer = (JSONObject) player;
            String message = jsonPlayer.getString("pseudo") + " - Score " + jsonPlayer.getInt("score");

            if (jsonPlayer.getBoolean("ready")) {
                message += " - PRET";
            }else{
                message += " - PAS PRET";
            }

            this.listPlayersModel.add(i, message);
            i++;

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == this.btnContinue) {
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
            this.getClient().setCommand("continue");
        }

    }
}