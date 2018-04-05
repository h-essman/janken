import client.Client;
import client.gui.Frame;

import javax.swing.*;


public class MainClient {
    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //new Client("localhost",1234, "Hessclient1");
    }
}
