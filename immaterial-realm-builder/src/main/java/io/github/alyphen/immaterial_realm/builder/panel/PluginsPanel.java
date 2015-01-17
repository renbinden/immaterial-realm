package io.github.alyphen.immaterial_realm.builder.panel;

import io.github.alyphen.immaterial_realm.builder.ImmaterialRealmBuilder;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;

public class PluginsPanel extends JPanel {

    private WebView browser;
    private WebEngine webEngine;
    private JFXPanel jfxPanel;

    public PluginsPanel(ImmaterialRealmBuilder application) {
        jfxPanel = new JFXPanel();
        Platform.runLater(() -> {
            BorderPane border = new BorderPane();
            Scene scene = new Scene(border);
            browser = new WebView();
            webEngine = browser.getEngine();
            webEngine.loadContent(
                    "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<title>Plugin browser unimplemented</title>" +
                    "</head>" +
                    "<body>" +
                    "<p>The plugin browser is currently not yet implemented. We're working on it!</p>" +
                    "</body>" +
                    "</html>"
            );
            border.setCenter(browser);
            jfxPanel.setScene(scene);
        });
        setLayout(new BorderLayout());
        add(jfxPanel, CENTER);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> application.showPanel("menu"));
        buttonsPanel.add(backButton);
        add(buttonsPanel, SOUTH);
    }

}
