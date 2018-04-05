package client.gui;

import javax.swing.*;
import java.io.IOException;

public class Frame extends JFrame {

        public Frame() {

            this.setSize(530, 280);
            this.setTitle("Pierre Feuille Papier Ciseaux");
            try {
                Panel menu = new Panel();
                this.add(menu);
            }catch (IOException e){
                System.out.println(e.getMessage());
            }

        }

}
