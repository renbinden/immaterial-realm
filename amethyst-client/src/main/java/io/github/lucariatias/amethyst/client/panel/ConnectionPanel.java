package io.github.lucariatias.amethyst.client.panel;

import io.github.lucariatias.amethyst.client.AmethystClient;

import javax.swing.*;
import java.awt.*;

public class ConnectionPanel extends JPanel {

    private AmethystClient client;

    private JLabel lblAddress;
    private JTextField addressField;
    private JButton btnConnect;

    public ConnectionPanel(AmethystClient client) {
        this.client = client;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(640, 480));
        add(Box.createVerticalGlue());
        lblAddress = new JLabel("Server Address: ");
        lblAddress.setAlignmentX(CENTER_ALIGNMENT);
        add(lblAddress);
        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(256, 24));
        addressField.setMaximumSize(new Dimension(256, 24));
        addressField.setAlignmentX(CENTER_ALIGNMENT);
        addressField.addActionListener(event -> btnConnect.doClick());
        add(addressField);
        lblAddress.setLabelFor(addressField);
        add(Box.createVerticalStrut(16));
        btnConnect = new JButton("Connect");
        btnConnect.setPreferredSize(new Dimension(64, 24));
        btnConnect.setAlignmentX(CENTER_ALIGNMENT);
        btnConnect.addActionListener(event -> {
            String address;
            int port = 39752;
            if (addressField.getText().contains(":")) {
                address = addressField.getText().split(":")[0];
                port = Integer.parseInt(addressField.getText().split(":")[1]);
            } else {
                address = addressField.getText();
            }
            client.getNetworkManager().setServerAddress(address);
            client.getNetworkManager().setServerPort(port);
            new Thread(() -> client.getNetworkManager().connect()).start();
        });
        add(btnConnect);
        add(Box.createVerticalGlue());
    }

}
