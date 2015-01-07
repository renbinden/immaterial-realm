package io.github.alyphen.immaterial_realm.builder;

import javax.swing.*;
import java.awt.*;

public class ImmaterialRealmBuilderFrame extends JFrame {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }
        EventQueue.invokeLater(() -> {
            ImmaterialRealmBuilderFrame frame = new ImmaterialRealmBuilderFrame();
            frame.setVisible(true);
            frame.requestFocus();
        });

    }

    public ImmaterialRealmBuilderFrame() {
        setTitle("ImmaterialRealm Builder");
        setFocusable(true);
        add(new ImmaterialRealmBuilder(this));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
