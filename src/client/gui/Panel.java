package client.gui;

import client.Client;

import javax.swing.*;

//Class abstraite pour définir les panels

public abstract class Panel extends JPanel {

    private Client client;
    private Frame frame;

    Panel(Client client, Frame frame) {
        this.client = client;
        this.frame = frame;
    }

    //Fonction refresh qui sera appelé par le ThreadClient afin d'actualiser les informations affichées
    public abstract void refresh();

    public Client getClient() {
        return client;
    }

    public Frame getFrame() {
        return frame;
    }
}
