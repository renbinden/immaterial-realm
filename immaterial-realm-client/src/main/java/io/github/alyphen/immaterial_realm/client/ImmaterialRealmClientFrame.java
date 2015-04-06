package io.github.alyphen.immaterial_realm.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        setExtendedState(MAXIMIZED_BOTH);
        setUndecorated(true);
        setTitle("ImmaterialRealm");
        setFocusable(true);
        ImmaterialRealmClient client = new ImmaterialRealmClient(this);
        add(client);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                client.getNetworkManager().closeConnections();
                System.exit(0);
            }
        });
    }

}
