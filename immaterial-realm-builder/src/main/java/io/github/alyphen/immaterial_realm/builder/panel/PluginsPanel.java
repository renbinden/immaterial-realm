package io.github.alyphen.immaterial_realm.builder.panel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;

public class PluginsPanel extends JPanel {

    private WebView browser;
    private Stage stage;
    private WebEngine webEngine;
    private JFXPanel jfxPanel;

    public PluginsPanel() {
        jfxPanel = new JFXPanel();
        createScene();
        setLayout(new BorderLayout());
        add(jfxPanel, CENTER);
    }

    private void createScene() {
        Platform.runLater(() -> {
            BorderPane border = new BorderPane();
            Scene scene = new Scene(border);
            browser = new WebView();
            webEngine = browser.getEngine();
            webEngine.load("https://duckduckgo.com/");
            border.setCenter(browser);
            jfxPanel.setScene(scene);
        });
    }

}
