package io.github.alyphen.immaterial_realm.client.panel;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.util.logging.Level.SEVERE;

public class LogoPanel extends JPanel {

    private BufferedImage logoImage;

    public LogoPanel(ImmaterialRealmClient client) {
        try {
            logoImage = ImageIO.read(getClass().getResourceAsStream("/logo.png"));
        } catch (IOException exception) {
            client.getLogger().log(SEVERE, "Failed to load logo image", exception);
        }
        setPreferredSize(new Dimension(logoImage.getWidth(), logoImage.getHeight()));
        setMaximumSize(new Dimension(logoImage.getWidth(), logoImage.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        graphics.drawImage(logoImage, 0, 0, null);
    }
}
