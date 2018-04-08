package client.gui;

import client.Client;

import javax.swing.*;
import java.io.IOException;

public class Frame extends JFrame {

        public Frame(Client client) {

            this.setSize(530, 280);
            this.setTitle("Pierre Feuille Papier Ciseaux");
            try {
                Panel menu = new Panel(client);
                this.add(menu);
            }catch (IOException e){
                System.out.println(e.getMessage());
            }

        }

}
