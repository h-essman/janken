package client.gui;

import client.Client;

import javax.swing.*;

public abstract class Panel extends JPanel {

    private Client client;
    private Frame frame;

    public Panel(Client client, Frame frame) {
        this.client = client;
        this.frame = frame;
    }

    public abstract void refresh();

    public Client getClient() {
        return client;
    }

    public Frame getFrame() {
        return frame;
    }
}
