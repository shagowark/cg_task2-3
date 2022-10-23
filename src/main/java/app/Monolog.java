package app;

import figures.Rect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

public class Monolog extends JDialog {
    private JPanel contentPane;
    private JButton buttonGetSolution;


    public Monolog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonGetSolution);
        buttonGetSolution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Rect> allRects = DrawPanel.getAllRects();
                //todo sortRect, findResult
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
