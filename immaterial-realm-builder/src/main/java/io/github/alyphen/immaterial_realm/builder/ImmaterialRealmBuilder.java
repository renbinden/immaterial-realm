package io.github.alyphen.immaterial_realm.builder;

import io.github.alyphen.immaterial_realm.builder.panel.*;
import io.github.alyphen.immaterial_realm.common.tile.TileSheet;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class ImmaterialRealmBuilder extends JPanel implements Runnable {

    private static final long DELAY = 100L;

    private ImmaterialRealmBuilderFrame frame;

    private MenuPanel menuPanel;
    private ChatDesignerPanel chatDesignerPanel;
    private LogViewerPanel logViewerPanel;
    private MapBuilderPanel mapBuilderPanel;
    private ObjectScripterPanel objectScripterPanel;
    private PluginsPanel pluginsPanel;
    private SettingsPanel settingsPanel;
    private TileSheetsPanel tileSheetsPanel;
    private SpritesPanel spritesPanel;

    public ImmaterialRealmBuilder(ImmaterialRealmBuilderFrame frame) {
        this.frame = frame;
        setLayout(new CardLayout());
        loadData();
        menuPanel = new MenuPanel(this);
        add(menuPanel, "menu");
        chatDesignerPanel = new ChatDesignerPanel(this);
        add(chatDesignerPanel, "chat designer");
        logViewerPanel = new LogViewerPanel(this);
        add(logViewerPanel, "log viewer");
        mapBuilderPanel = new MapBuilderPanel(this);
        add(mapBuilderPanel, "map builder");
        objectScripterPanel = new ObjectScripterPanel(this);
        add(objectScripterPanel, "object scripter");
        pluginsPanel = new PluginsPanel(this);
        add(pluginsPanel, "plugins");
        settingsPanel = new SettingsPanel(this);
        add(settingsPanel, "settings");
        tileSheetsPanel = new TileSheetsPanel(this);
        add(tileSheetsPanel, "tilesheets");
        spritesPanel = new SpritesPanel(this);
        add(spritesPanel, "sprites");
        new Thread(this).start();
    }

    public void showPanel(String panel) {
        CardLayout layout = (CardLayout) getLayout();
        layout.show(this, panel);
        frame.pack();
    }

    public void loadData() {
        loadTileSheets();
    }

    public void loadTileSheets() {
        File tileSheetDirectory = new File("./tilesheets");
        if (tileSheetDirectory.exists() && tileSheetDirectory.isDirectory()) {
            for (File file : tileSheetDirectory.listFiles(File::isDirectory)) {
                try {
                    TileSheet.load(file);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public void doTick() {
        mapBuilderPanel.onTick();
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (true) {
            doTick();
            repaint();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            if (sleep < 0) {
                sleep = 2;
            }
            try {
                sleep(sleep);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            beforeTime = System.currentTimeMillis();
        }
    }

    public ImmaterialRealmBuilderFrame getFrame() {
        return frame;
    }

}
