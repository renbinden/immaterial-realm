package io.github.alyphen.immaterial_realm.client.panel;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Math.toRadians;
import static java.util.logging.Level.SEVERE;

public class LoginLoadingSpinnerPanel extends JPanel {

    private ImmaterialRealmClient client;

    private BufferedImage loadingSpinner;
    private int angle;

    public LoginLoadingSpinnerPanel(ImmaterialRealmClient client) {
        this.client = client;
        try {
            loadingSpinner = ImageIO.read(getClass().getResourceAsStream("/loading_spinner.png"));
        } catch (IOException exception) {
            client.getLogger().log(SEVERE, "Failed to load loading spinner image", exception);
        }
        setMaximumSize(new Dimension(128, 128));
        setPreferredSize(new Dimension(128, 128));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (client.getLoginPanel().isLoggingIn()) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.rotate(toRadians(angle), 64, 64);
            graphics2D.drawImage(loadingSpinner, 32, 32, null);
            graphics2D.rotate(toRadians(-angle), 64, 64);
        }
    }

    public void onTick() {
        angle = angle >= 359 ? 0 : angle + 5;
        repaint();
    }

}
