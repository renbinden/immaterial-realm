package io.github.alyphen.amethyst.client;

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
            frame.requestFocus();
        });

    }

    public AmethystClientFrame() {
        setTitle("Amethyst");
        setFocusable(true);
        add(new AmethystClient(this));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
