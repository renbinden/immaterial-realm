package io.github.lucariatias.amethyst.client;

import javax.swing.*;
import java.awt.*;

public class AmethystClientFrame extends JFrame {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }
        EventQueue.invokeLater(() -> {
            AmethystClientFrame frame = new AmethystClientFrame();
            frame.setVisible(true);
        });

    }

    public AmethystClientFrame() {
        setTitle("Amethyst");
        add(new AmethystClient());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //AmethystClient client = new AmethystClient();
        //client.connect("localhost", 39752);
        //add(client);
    }

}
