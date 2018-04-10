package client.gui;

import client.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Server extends Panel implements ActionListener {

    private JLabel pseudoServer, nbClientServer;
    private JButton btnActualiser;

    public Server(Client client, Frame frame){
        super(client, frame);
        this.setLayout(null);

        this.pseudoServer = new JLabel();
        this.pseudoServer.setBounds(20,60,300,30);
        this.add(pseudoServer);


        this.nbClientServer = new JLabel();
        this.nbClientServer.setBounds(20,100,300,30);
        this.add(nbClientServer);

        this.btnActualiser = new JButton("Actualiser");
        this.btnActualiser.setBounds(10,150,100,40);
        this.add(btnActualiser);

        this.btnActualiser.addActionListener(this);
    }

    public void actualiser(){
        this.pseudoServer.setText("Connecté au serveur : "+ this.getClient().getJsonServer().get("server"));
        this.nbClientServer.setText("Il y a  "+ this.getClient().getJsonServer().get("nbClient")+" connectés au serveur.");
        System.out.println("J'actualise le server");
    }
    public void actionPerformed(ActionEvent e){

        if (e.getSource() == this.btnActualiser) {
            this.actualiser();
        }
    }

}
