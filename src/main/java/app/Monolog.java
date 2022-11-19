package app;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Monolog extends JDialog {
    private JPanel contentPane;
    private JButton buttonGetSolution;
    private DrawPanel drawPanel;
    private JButton drawButton;


    public Monolog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonGetSolution);
        buttonGetSolution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.setMode(true);
            }
        });
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.setMode(false);
            }
        });
    }

    public static void main(String[] args) {
        Monolog dialog = new Monolog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
