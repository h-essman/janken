package client.gui;

import client.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Game extends Panel implements ActionListener {

    private JLabel instructionLabel;
    private JButton btnSubmit;
    private ButtonGroup radBtnGroup;
    private boolean submitted;
    private JRadioButton radBtnRock, radBtnLeaf, radBtnCissors;

    Game(Client client, Frame frame) {

        super(client, frame);
        this.setLayout(null);

        this.radBtnGroup = new ButtonGroup();

        this.instructionLabel = new JLabel("Choissisez et validez !");
        this.instructionLabel.setBounds(50, 10, 300, 20);
        this.add(this.instructionLabel);

        this.btnSubmit = new JButton("Valider");
        this.btnSubmit.setBounds(100, 100, 80, 30);
        this.add(this.btnSubmit);
        this.btnSubmit.addActionListener(this);

        this.radBtnRock = new JRadioButton("Pierre");
        this.radBtnRock.setBounds(20, 50, 80, 30);
        this.radBtnRock.setMnemonic(KeyEvent.VK_P);
        this.radBtnRock.setActionCommand("pierre");
        this.add(this.radBtnRock);
        this.radBtnGroup.add(this.radBtnRock);

        this.radBtnLeaf = new JRadioButton("Feuille");
        this.radBtnLeaf.setBounds(120, 50, 80, 30);
        this.radBtnLeaf.setMnemonic(KeyEvent.VK_F);
        this.radBtnLeaf.setActionCommand("feuille");
        this.add(this.radBtnLeaf);
        this.radBtnGroup.add(this.radBtnLeaf);

        this.radBtnCissors = new JRadioButton("Ciseaux");
        this.radBtnCissors.setBounds(220, 50, 80, 30);
        radBtnCissors.setMnemonic(KeyEvent.VK_C);
        radBtnCissors.setActionCommand("ciseaux");
        this.add(this.radBtnCissors);
        this.radBtnGroup.add(this.radBtnCissors);

    }

    @Override
    public void refresh() {
        if(this.submitted && this.getClient().getPlayer().isWaiting()){
            this.instructionLabel.setText("En attente de l'autre joueur...");
            this.btnSubmit.setVisible(false);
        }else if(!this.submitted){
            this.instructionLabel.setText("Choissisez et validez !");
            this.btnSubmit.setVisible(true);
        }else{
            this.btnSubmit.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == this.btnSubmit){
            if(this.radBtnRock.isSelected()){
                this.getClient().getPlayer().setChoice(1);
            }else if(this.radBtnLeaf.isSelected()){
                this.getClient().getPlayer().setChoice(2);
            }else if(this.radBtnCissors.isSelected()){
                this.getClient().getPlayer().setChoice(3);
            }else{
                return;
            }
            this.submitted = true;
            this.getClient().setCommand("choice");
            this.getClient().setArgument(this.getClient().getPlayer().getChoice());
            this.getClient().getPlayer().setChoice(0);
            this.btnSubmit.setVisible(false);
        }

    }
    public void setSubmitted(boolean submitted) { this.submitted = submitted; }
}
