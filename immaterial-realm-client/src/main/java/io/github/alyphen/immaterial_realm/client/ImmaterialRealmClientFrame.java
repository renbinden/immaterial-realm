package io.github.alyphen.immaterial_realm.client;

import javax.swing.*;
import java.awt.*;

public class ImmaterialRealmClientFrame extends JFrame {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }
        EventQueue.invokeLater(() -> {
            ImmaterialRealmClientFrame frame = new ImmaterialRealmClientFrame();
            frame.setVisible(true);
            frame.requestFocus();
        });

    }

    public ImmaterialRealmClientFrame() {
        setTitle("ImmaterialRealm");
        setFocusable(true);
        add(new ImmaterialRealmClient(this));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
