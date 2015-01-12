package io.github.alyphen.immaterial_realm.builder.panel;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
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
            stage = new Stage();
            stage.setTitle("Web View");
            stage.setResizable(true);
            Group root = new Group();
            Scene scene = new Scene(root,80,20);
            stage.setScene(scene);
            browser = new WebView();
            webEngine = browser.getEngine();
            webEngine.load("https://duckduckgo.com/");
            ObservableList<Node> children = root.getChildren();
            children.add(browser);
            jfxPanel.setScene(scene);
        });
    }

}
