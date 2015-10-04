package io.github.alyphen.immaterial_realm.client.panel;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class ConnectionPanel extends JPanel {

    private JLabel lblAddress;
    private JTextField addressField;
    private JButton btnConnect;
    private JLabel lblStatus;
    private ConnectionLoadingSpinnerPanel loadingSpinnerPanel;

    private boolean connecting;

    public ConnectionPanel(ImmaterialRealmClient client) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(640, 480));
        setBackground(BLACK);
        setForeground(WHITE);
        add(Box.createVerticalGlue());
        add(new LogoPanel(client));
        add(Box.createVerticalStrut(16));
        lblAddress = new JLabel("Server Address: ");
        lblAddress.setAlignmentX(CENTER_ALIGNMENT);
        lblAddress.setForeground(WHITE);
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
            btnConnect.setEnabled(false);
            connecting = true;
            lblStatus.setText("");
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
        add(Box.createVerticalStrut(16));
        lblStatus = new JLabel();
        lblStatus.setAlignmentX(CENTER_ALIGNMENT);
        lblStatus.setForeground(WHITE);
        add(lblStatus);
        add(Box.createVerticalStrut(16));
        loadingSpinnerPanel = new ConnectionLoadingSpinnerPanel(client);
        loadingSpinnerPanel.setAlignmentX(CENTER_ALIGNMENT);
        add(loadingSpinnerPanel);
        add(Box.createVerticalGlue());
    }

    public void onTick() {
        loadingSpinnerPanel.onTick();
    }

    public boolean isConnecting() {
        return connecting;
    }

    public void failConnection(String message) {
        btnConnect.setEnabled(true);
        connecting = false;
        lblStatus.setText("Failed to connect to server: " + message);
    }
}
