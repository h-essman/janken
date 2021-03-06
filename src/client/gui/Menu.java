package client.gui;

import client.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Panel du menu

public class Menu extends Panel implements ActionListener {

    private JButton btnConnexion, btnQuit;
    private JTextField inputHost, inputPseudo, inputPort;
    private JLabel labelHost, labelPseudo, labelPort;
    private JCheckBox checkBoxSecure;

    Menu(Client client, Frame frame) {
        super(client, frame);

        this.setLayout(null);

        this.labelPseudo = new JLabel("Pseudo : ");
        this.labelPseudo.setBounds(20, 20, 150, 30);
        this.add(labelPseudo);

        this.labelHost = new JLabel("Adresse du serveur : ");
        this.labelHost.setBounds(20, 60, 150, 30);
        this.add(labelHost);

        this.labelPort = new JLabel("Port du serveur : ");
        this.labelPort.setBounds(20, 120, 150, 30);
        this.add(labelPort);

        this.inputPseudo = new JTextField(5);
        this.inputPseudo.setText("Joueur");
        this.inputPseudo.setBounds(170, 20, 200, 30);
        this.add(inputPseudo);

        this.inputHost = new JTextField(5);
        this.inputHost.setText("localhost");
        this.inputHost.setBounds(170, 60, 200, 30);
        this.add(inputHost);

        this.inputPort = new JTextField(5);
        this.inputPort.setText("1234");
        this.inputPort.setBounds(170, 120, 200, 30);
        this.add(inputPort);

        this.btnConnexion = new JButton("Connexion");
        this.btnConnexion.setBounds(10, 170, 200, 40);
        this.add(btnConnexion);
        this.btnConnexion.addActionListener(this);

        this.btnQuit = new JButton("Quitter");
        this.btnQuit.setBounds(220, 170, 200, 40);
        this.add(btnQuit);
        this.btnQuit.addActionListener(this);

        this.checkBoxSecure = new JCheckBox("Sécurisé");
        this.checkBoxSecure.setBounds(80, 90, 100, 20);
        this.add(this.checkBoxSecure);
        this.checkBoxSecure.addActionListener(this);
    }

    @Override
    public void refresh() {
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.btnQuit) {
            System.exit(0);

        } else if (e.getSource() == this.btnConnexion) {
            if (inputHost.getText().length() == 0 | inputPseudo.getText().length() == 0 | inputPort.getText().length() == 0) {
                this.getFrame().showError("Des champs sont vides...");

            } else {
                if (this.checkBoxSecure.isSelected()) {
                    String message = JOptionPane.showInputDialog(this,
                            "Entrer la passphrase :",
                            "Connexion sécurisée", JOptionPane.QUESTION_MESSAGE);
                    if (message != null) {
                        this.getClient().setPassphrase(message);
                    } else {
                        return;
                    }
                }

                if (!this.getClient().connexion(inputHost.getText(), Integer.parseInt(inputPort.getText()), inputPseudo.getText(), this.checkBoxSecure.isSelected())){
                   this.getFrame().showError("Erreur de connexion...");
                   this.resetField();
                }
            }
        }
    }

    public void resetField() {

        this.inputPort.setText("");
        this.inputHost.setText("");
        this.inputPseudo.setText("");

    }
}
