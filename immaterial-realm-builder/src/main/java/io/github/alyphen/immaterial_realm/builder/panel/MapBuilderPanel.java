package io.github.alyphen.immaterial_realm.builder.panel;

import io.github.alyphen.immaterial_realm.builder.ImmaterialRealmBuilder;
import io.github.alyphen.immaterial_realm.builder.panel.mapbuilder.AreaSelectPanel;
import io.github.alyphen.immaterial_realm.builder.panel.mapbuilder.MapEditorPanel;
import io.github.alyphen.immaterial_realm.builder.panel.mapbuilder.ObjectsPanel;
import io.github.alyphen.immaterial_realm.builder.panel.mapbuilder.TilePanel;
import io.github.alyphen.immaterial_realm.common.world.World;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.BorderLayout.*;
import static java.util.logging.Level.SEVERE;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class MapBuilderPanel extends JPanel {

    private World world;

    private AreaSelectPanel areaSelectPanel;
    private JTabbedPane objectsAndTilesPanel;
    private TilePanel tilePanel;
    private ObjectsPanel objectsPanel;
    private MapEditorPanel mapEditorPanel;

    public MapBuilderPanel(ImmaterialRealmBuilder application) {
        File worldFile  = new File("./worlds/default");
        if (worldFile.exists()) {
            try {
                world = World.load(worldFile);
            } catch (IOException | ClassNotFoundException exception) {
                showMessageDialog(null, "Failed to load world:\n" + exception.getMessage(), "Failed to load world", ERROR_MESSAGE);
                application.getLogger().log(SEVERE, "Failed to load world", exception);
            }
        } else {
            world = World.create("default");
        }
        setLayout(new BorderLayout());
        areaSelectPanel = new AreaSelectPanel(this);
        tilePanel = new TilePanel();
        objectsPanel = new ObjectsPanel();
        objectsAndTilesPanel = new JTabbedPane();
        objectsAndTilesPanel.addTab("Tiles", tilePanel);
        objectsAndTilesPanel.addTab("Objects", objectsPanel);
        mapEditorPanel = new MapEditorPanel(this);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(event -> {
            try {
                world.save(new File("./worlds/default"));
            } catch (IOException exception) {
                showMessageDialog(null, "Failed to save world:\n" + exception.getMessage(), "Failed to save world", ERROR_MESSAGE);
                application.getLogger().log(SEVERE, "Failed to save world", exception);
            }
        });
        buttonsPanel.add(btnSave);
        JButton btnAddRow = new JButton("Add row");
        btnAddRow.addActionListener(event -> getArea().addRows(1));
        buttonsPanel.add(btnAddRow);
        JButton btnAddColumn = new JButton("Add column");
        btnAddColumn.addActionListener(event -> getArea().addColumns(1));
        buttonsPanel.add(btnAddColumn);
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(event -> application.showPanel("menu"));
        buttonsPanel.add(btnBack);
        add(buttonsPanel, SOUTH);
        add(objectsAndTilesPanel, EAST);
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

    public TilePanel getTilePanel() {
        return tilePanel;
    }

    public ObjectsPanel getObjectsPanel() {
        return objectsPanel;
    }

    public boolean isTilesSelected() {
        return objectsAndTilesPanel.getSelectedComponent() == getTilePanel();
    }

    public boolean isObjectsSelected() {
        return objectsAndTilesPanel.getSelectedComponent() == getObjectsPanel();
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
