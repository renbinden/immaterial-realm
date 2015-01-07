package io.github.alyphen.immaterial_realm.builder.panel;

import io.github.alyphen.immaterial_realm.builder.ImmaterialRealmBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class MenuPanel extends JPanel {

    public MenuPanel(ImmaterialRealmBuilder application) {
        GridLayout layout = new GridLayout(3, 3);
        layout.setHgap(16);
        layout.setVgap(16);
        setLayout(layout);
        try {
            JButton btnChatDesigner = new JButton("Chat Designer", new ImageIcon(read(getClass().getResourceAsStream("/icon_chat_designer.png"))));
            btnChatDesigner.addActionListener(event -> application.showPanel("chat designer"));
            add(btnChatDesigner);
            JButton btnLogViewer = new JButton("Log Viewer", new ImageIcon(read(getClass().getResourceAsStream("/icon_log_viewer.png"))));
            btnLogViewer.addActionListener(event -> application.showPanel("log viewer"));
            add(btnLogViewer);
            JButton btnMapBuilder = new JButton("Map Builder", new ImageIcon(read(getClass().getResourceAsStream("/icon_map_builder.png"))));
            btnMapBuilder.addActionListener(event -> application.showPanel("map builder"));
            add(btnMapBuilder);
            JButton btnObjectScripter = new JButton("Object Scripter", new ImageIcon(read(getClass().getResourceAsStream("/icon_object_scripter.png"))));
            btnObjectScripter.addActionListener(event -> application.showPanel("object scripter"));
            add(btnObjectScripter);
            JButton btnPlugins = new JButton("Plugins", new ImageIcon(read(getClass().getResourceAsStream("/icon_plugins.png"))));
            btnPlugins.addActionListener(event -> application.showPanel("plugins"));
            add(btnPlugins);
            JButton btnSettings = new JButton("Settings", new ImageIcon(read(getClass().getResourceAsStream("/icon_settings.png"))));
            btnSettings.addActionListener(event -> application.showPanel("settings"));
            add(btnSettings);
            JButton btnTilesheets = new JButton("Tilesheets", new ImageIcon(read(getClass().getResourceAsStream("/icon_tilesheets.png"))));
            btnTilesheets.addActionListener(event -> application.showPanel("tilesheets"));
            add(btnTilesheets);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
