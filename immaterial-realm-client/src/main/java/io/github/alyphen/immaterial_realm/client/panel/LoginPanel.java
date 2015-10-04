package io.github.alyphen.immaterial_realm.client.panel;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.login.PacketLoginDetails;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.util.logging.Level.SEVERE;

public class LoginPanel extends JPanel {

    private JLabel lblUserName;
    private JTextField userNameField;
    private JLabel lblPassword;
    private JPasswordField passwordField;
    private JButton btnLogin;
    private JButton btnSignUp;
    private JLabel lblStatus;
    private LoginLoadingSpinnerPanel loadingSpinnerPanel;

    private boolean loggingIn;

    public LoginPanel(ImmaterialRealmClient client) {
        loggingIn = false;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(640, 480));
        setBackground(BLACK);
        setForeground(WHITE);
        add(Box.createVerticalGlue());
        lblUserName = new JLabel("Username: ");
        lblUserName.setAlignmentX(CENTER_ALIGNMENT);
        lblUserName.setForeground(WHITE);
        add(lblUserName);
        userNameField = new JTextField();
        userNameField.setPreferredSize(new Dimension(256, 24));
        userNameField.setMaximumSize(new Dimension(256, 24));
        userNameField.setAlignmentX(CENTER_ALIGNMENT);
        userNameField.addActionListener(event -> btnLogin.doClick());
        add(userNameField);
        lblUserName.setLabelFor(userNameField);
        add(Box.createVerticalStrut(16));
        lblPassword = new JLabel("Password: ");
        lblPassword.setAlignmentX(CENTER_ALIGNMENT);
        lblPassword.setForeground(WHITE);
        add(lblPassword);
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(256, 24));
        passwordField.setMaximumSize(new Dimension(256, 24));
        passwordField.setAlignmentX(CENTER_ALIGNMENT);
        passwordField.addActionListener(event -> btnLogin.doClick());
        add(passwordField);
        lblPassword.setLabelFor(passwordField);
        add(Box.createVerticalStrut(16));
        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(64, 24));
        btnLogin.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin.addActionListener(event -> {
            btnLogin.setEnabled(false);
            btnSignUp.setEnabled(false);
            loggingIn = true;
            lblStatus.setText("");
            try {
                client.setPlayerName(userNameField.getText());
                client.getNetworkManager().sendPacket(
                        new PacketLoginDetails(
                                userNameField.getText(),
                                client.getEncryptionManager().encrypt(new String(passwordField.getPassword()), client.getNetworkManager().getServerPublicKey()),
                                false
                        )
                );
            } catch (GeneralSecurityException | UnsupportedEncodingException exception) {
                client.getLogger().log(SEVERE, "Failed to encrypt password", exception);
            }
        });
        add(btnLogin);
        add(Box.createVerticalStrut(16));
        btnSignUp = new JButton("Sign up");
        btnSignUp.setPreferredSize(new Dimension(64, 24));
        btnSignUp.setAlignmentX(CENTER_ALIGNMENT);
        btnSignUp.addActionListener(event -> {
            btnLogin.setEnabled(false);
            btnSignUp.setEnabled(false);
            loggingIn = true;
            lblStatus.setText("");
            try {
                client.setPlayerName(userNameField.getText());
                client.getNetworkManager().sendPacket(
                        new PacketLoginDetails(
                                userNameField.getText(),
                                client.getEncryptionManager().encrypt(new String(passwordField.getPassword()), client.getNetworkManager().getServerPublicKey()),
                                true
                        )
                );
            } catch (GeneralSecurityException | UnsupportedEncodingException exception) {
                client.getLogger().log(SEVERE, "Failed to encrypt password", exception);
            }
        });
        add(btnSignUp);
        add(Box.createVerticalStrut(16));
        lblStatus = new JLabel("");
        lblStatus.setAlignmentX(CENTER_ALIGNMENT);
        lblStatus.setForeground(WHITE);
        add(lblStatus);
        add(Box.createVerticalStrut(16));
        loadingSpinnerPanel = new LoginLoadingSpinnerPanel(client);
        loadingSpinnerPanel.setAlignmentX(CENTER_ALIGNMENT);
        add(loadingSpinnerPanel);
        add(Box.createVerticalGlue());
    }

    public void setStatusMessage(String message) {
        lblStatus.setText(message);
    }

    public void reEnableLoginButtons() {
        btnSignUp.setEnabled(true);
        btnLogin.setEnabled(true);
        loggingIn = false;
    }

    public boolean isLoggingIn() {
        return loggingIn;
    }

    public void onTick() {
        loadingSpinnerPanel.onTick();
    }

}
