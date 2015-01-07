package io.github.alyphen.immaterial_realm.builder.panel;

import io.github.alyphen.immaterial_realm.builder.panel.mapbuilder.AreaSelectPanel;
import io.github.alyphen.immaterial_realm.builder.panel.mapbuilder.MapEditorPanel;
import io.github.alyphen.immaterial_realm.builder.panel.mapbuilder.TilePanel;
import io.github.alyphen.immaterial_realm.builder.panel.mapbuilder.TileSheetPanel;
import io.github.alyphen.immaterial_realm.common.world.World;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.BorderLayout.*;

public class MapBuilderPanel extends JPanel {

    private World world;

    private AreaSelectPanel areaSelectPanel;
    private TileSheetPanel tileSheetPanel;
    private TilePanel tilePanel;
    private MapEditorPanel mapEditorPanel;

    public MapBuilderPanel() {
        File worldFile  = new File("./worlds/default");
        if (worldFile.exists()) {
            try {
                world = World.load(worldFile);
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        } else {
            world = World.create("default");
        }
        setLayout(new BorderLayout());
        areaSelectPanel = new AreaSelectPanel(this);
        tileSheetPanel = new TileSheetPanel();
        tilePanel = new TilePanel(this);
        mapEditorPanel = new MapEditorPanel(this);
        add(new AreaSelectPanel(this), WEST);
        JPanel tilesPanel = new JPanel();
        tilesPanel.setLayout(new GridLayout(2, 1));
        tilesPanel.add(tileSheetPanel);
        tilesPanel.add(tilePanel);
        add(tilesPanel, EAST);
        add(mapEditorPanel, CENTER);
        add(areaSelectPanel, WEST);
    }

    public World getWorld() {
        return world;
    }

    public WorldArea getArea() {
        return areaSelectPanel.getSelectedArea();
    }

    public AreaSelectPanel getAreaSelectPanel() {
        return areaSelectPanel;
    }

    public TileSheetPanel getTileSheetPanel() {
        return tileSheetPanel;
    }

    public TilePanel getTilePanel() {
        return tilePanel;
    }

    public MapEditorPanel getMapEditorPanel() {
        return mapEditorPanel;
    }

    public void onTick() {
        getMapEditorPanel().onTick();
        if (isVisible()) {
            repaint();
        }
    }

}
