package io.github.alyphen.amethyst.builder;

import javax.swing.*;
import java.awt.*;

public class AmethystBuilderFrame extends JFrame {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }
        EventQueue.invokeLater(() -> {
            AmethystBuilderFrame frame = new AmethystBuilderFrame();
            frame.setVisible(true);
            frame.requestFocus();
        });

    }

    public AmethystBuilderFrame() {
        setTitle("Amethyst Builder");
        setFocusable(true);
        add(new AmethystBuilder(this));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
