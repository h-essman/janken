package client.gui;

import client.Client;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel panel = new JPanel();
    private Client client;
    private Menu menu;
    private Connexion connexion;
    private Server server;
    private Lobby lobby;
    private Game game;
    private Result result;

    public Frame() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.client = new Client(this);

        this.menu = new Menu(client, this);
        this.connexion = new Connexion(client, this);
        this.server = new Server(client, this);
        this.lobby = new Lobby(client, this);
        this.game = new Game(client, this);
        this.result = new Result(client, this);

        panel.setLayout(cardLayout);
        panel.add(this.menu, "menu");
        panel.add(this.connexion, "connexion");
        panel.add(this.server, "server");
        panel.add(this.lobby, "lobby");
        panel.add(this.game, "game");
        panel.add(this.result, "result");
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.setVisible(true);

        this.goPanel("menu");

    }

    public void goPanel(String stage) {

        switch (stage) {

            case "menu":
                this.setTitle("Janken - MENU");
                this.setSize(440, 250);
                this.cardLayout.show(this.panel, "menu");
                this.client.setState("menu");
                break;

            case "connexion":
                this.setTitle("Janken - CONNEXION");
                this.setSize(100, 50);
                this.cardLayout.show(this.panel, "connexion");
                break;

            case "server":
                this.setTitle("Janken - SERVER");
                this.setSize(440, 350);
                this.cardLayout.show(this.panel, "server");
                this.client.setState("server");
                break;

            case "lobby":
                this.setTitle("Janken - LOBBY");
                this.setSize(330, 170);
                this.cardLayout.show(this.panel, "lobby");
                this.client.setState("lobby");
                break;

            case "game":
                this.setTitle("Janken - GAME");
                this.setSize(330, 170);
                this.cardLayout.show(this.panel, "game");
                this.client.setState("game");
                this.game.setSubmitted(false);
                break;

            case "result":
                this.setTitle("Janken - RESULT");
                this.setSize(330, 170);
                this.cardLayout.show(this.panel, "result");
                this.client.setState("result");
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
            case "result":
                return this.result;
        }
        return null;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Erreur", JOptionPane.WARNING_MESSAGE);
    }
}