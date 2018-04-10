package client.gui;

import client.Client;
import server.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import static java.lang.Integer.parseInt;

public class Menu extends Panel implements ActionListener {


    private JButton btnConnexion, btnQuit;
    private JTextField inputHost, inputPseudo, inputPort;
    private JLabel labelHost, labelPseudo, labelPort, labelPic;
    private String hostname, ipadress;

    public Menu(Client client, Frame frame){

       super(client,frame);

        try{
            InetAddress inetadr = InetAddress.getLocalHost();
            this.hostname = inetadr.getHostName();
            this.ipadress = inetadr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.setLayout(null);

        /*BufferedImage myPicture = ImageIO.read(new File(".\\image.png"));
        this.labelPic= new JLabel(new ImageIcon(myPicture.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        labelPic.setBounds(400,20,100,100);
        this.add(labelPic);*/

        this.labelPseudo = new JLabel("Pseudo : ");
        this.labelPseudo.setBounds(20,20,150,30);
        this.add(labelPseudo);

        this.labelHost = new JLabel("Adresse du serveur : ");
        this.labelHost .setBounds(20,60,150,30);
        this.add(labelHost);

        this.labelPort= new JLabel("Port du serveur : ");
        this.labelPort.setBounds(20,100,150,30);
        this.add(labelPort);

        this.inputPseudo = new JTextField(5);
        this.inputPseudo.setText("Joueur");
        this.inputPseudo.setBounds(170,20,200,30);
        this.add(inputPseudo);

        this.inputHost = new JTextField(5);
        this.inputHost.setText("localhost");
        this.inputHost.setBounds(170,60,200,30);
        this.add(inputHost);

        this.inputPort = new JTextField(5);
        this.inputPort.setText("1234");
        this.inputPort.setBounds(170,100,200,30);
        this.add(inputPort);

        this.btnConnexion = new JButton("Connexion");
        this.btnConnexion.setBounds(10,150,200,40);
        this.add(btnConnexion);
        this.btnConnexion.addActionListener(this);

        this.btnQuit = new JButton("Quitter");
        this.btnQuit.setBounds(220,150,200,40);
        this.add(btnQuit);
        this.btnQuit.addActionListener(this);
    }

    @Override
    public void actualiser() {
        System.out.println("J'actualise le menu");
    }

    public void actionPerformed(ActionEvent e){

        if (e.getSource() == this.btnQuit) {
            System.exit(0);
        }else if (e.getSource() == this.btnConnexion) {
            if (inputHost.getText().length() == 0 | inputPseudo.getText().length() == 0 | inputPort.getText().length() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Des champs sont vides..."
                );
            } else {
                if (this.getClient().connexion(inputHost.getText(), Integer.parseInt(inputPort.getText()), inputPseudo.getText())) {
                    this.getFrame().goNext("server");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erreur de connexion..."
                    );
                }
            }
        }
    }
    public void resetField(){
        this.inputPort.setText("");
        this.inputHost.setText("");
        this.inputPseudo.setText("");
    }
}
