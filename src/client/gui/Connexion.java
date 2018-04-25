package client.gui;
import client.Client;

import javax.swing.*;

//Panel d'attente de connexion

public class Connexion extends Panel{

    Connexion (Client client, Frame frame) {
        super(client, frame);
        JLabel connexion = new JLabel("Connexion en cours...");
        connexion.setBounds(20, 40, 150, 40);
        this.add(connexion);
    }
    public void refresh() { }
}
