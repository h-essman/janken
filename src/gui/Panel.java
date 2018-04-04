package gui;

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
import java.net.Inet4Address;
import java.util.ArrayList;
import java.net.InetAddress;
import java.net.UnknownHostException;
import static java.lang.Integer.parseInt;

public class Panel extends JPanel implements ActionListener {

    private JButton btnClient, btnServer, btnSolo, btnInfo, btnQuit;
    private JTextField inputHost, inputPseudo, inputPort;
    private JLabel labelHost, labelPseudo, labelPort, labelPic;
    private String hostname, ipadress;

    public Panel() throws IOException {

        try{
            InetAddress inetadr = InetAddress.getLocalHost();
            this.hostname = inetadr.getHostName();
            this.ipadress = inetadr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.setLayout(null);

        BufferedImage myPicture = ImageIO.read(new File("D:\\javaproject\\clientserveur\\src\\image.png"));
        this.labelPic= new JLabel(new ImageIcon(myPicture.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        labelPic.setBounds(400,20,100,100);
        this.add(labelPic);

        this.labelPseudo = new JLabel("Pseudo : ");
        this.labelPseudo.setBounds(20,20,150,30);
        this.add(labelPseudo);

        this.labelHost = new JLabel("Adresse de l'hôte : ");
        this.labelHost .setBounds(20,60,150,30);
        this.add(labelHost);

        this.labelPort= new JLabel("Port de l'hôte : ");
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

        this.btnSolo = new JButton("Partie en solo");
        this.btnSolo.setBounds(180,150,160,20);
        this.add(btnSolo);
        this.btnSolo.addActionListener(this);

        this.btnServer = new JButton("Créer un serveur");
        this.btnServer.setBounds(350,150,160,20);
        this.add(btnServer);
        this.btnServer.addActionListener(this);

        this.btnClient = new JButton("Rejoindre un serveur");
        this.btnClient.setBounds(10,150,160,20);
        this.add(btnClient);
        this.btnClient.addActionListener(this);

        this.btnInfo = new JButton("Information");
        this.btnInfo.setBounds(10,200,130,20);
        this.add(btnInfo);
        this.btnInfo.addActionListener(this);

        this.btnQuit = new JButton("Quitter");
        this.btnQuit.setBounds(410,200,100,20);
        this.add(btnQuit);
        this.btnQuit.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e){

        if (e.getSource() == this.btnQuit) {
            System.exit(0);
        } else if (e.getSource() == this.btnServer) {
            if (inputPseudo.getText().length() == 0 | inputPort.getText().length() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Des champs sont vides..."
                );
            }else {
                new Server(Integer.parseInt(inputPort.getText()));
            }
        }else if (e.getSource() == this.btnClient) {
            if (inputHost.getText().length() == 0 | inputPseudo.getText().length() == 0 | inputPort.getText().length() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Des champs sont vides..."
                );
            }else {
                new Client(inputHost.getText(), Integer.parseInt(inputPort.getText()), inputPseudo.getText());
            }
        }else if (e.getSource() == this.btnSolo) {
            System.out.println("Partie SOLO");
        }else if (e.getSource() == this.btnInfo) {
            JOptionPane.showMessageDialog(this,
                    "Votre adresse IP : "+this.ipadress +
                    "\n"+"Votre nom d'hôte : "+ this.hostname+"" +
                    "\n"+"Votre pseudo :"+inputPseudo.getText()
            );
        }
    }
    public void resetField(){
        this.inputPort.setText("");
        this.inputHost.setText("");
        this.inputPseudo.setText("");
    }
}
