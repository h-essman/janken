package client.gui;

import client.Client;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel panel = new JPanel();
    private Client client;
    private Menu menu;
    private Server server;
    private Lobby lobby;
    private Game game;

    public Frame() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.client = new Client(this);

        this.menu = new Menu(client, this);
        this.server = new Server(client, this);
        this.lobby = new Lobby(client, this);
        this.game = new Game(client, this);

        panel.setLayout(cardLayout);
        panel.add(this.menu, "menu");
        panel.add(this.server, "server");
        panel.add(this.lobby, "lobby");
        panel.add(this.game, "game");
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.setVisible(true);

        this.goNext("menu");

    }

    public void goNext(String stage) {

        switch (stage) {

            case "menu":
                this.setTitle("Janken - MENU");
                this.setSize(440, 250);
                this.cardLayout.show(this.panel, "menu");
                this.client.setState("menu");
                break;

            case "server":
                this.setTitle("Janken - SERVER");
                this.setSize(440, 350);
                this.cardLayout.show(this.panel, "server");
                this.client.setState("server");

                break;

            case "lobby":
                this.setTitle("Janken - LOBBY");
                this.setSize(440, 400);
                this.cardLayout.show(this.panel, "lobby");
                this.client.setState("lobby");
                break;

            case "game":
                this.setTitle("Janken - GAME");
                this.setSize(600, 600);
                this.cardLayout.show(this.panel, "game");
                this.client.setState("game");
                break;

        }
    }

    public Panel getPanel() {

        switch (this.client.getState()) {
            case "menu":

                return this.menu;
            case "server":

                return this.server;
            case "lobby":

                return this.lobby;
            case "game":

                return this.game;
        }

        return null;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Erreur", 2);
    }
}