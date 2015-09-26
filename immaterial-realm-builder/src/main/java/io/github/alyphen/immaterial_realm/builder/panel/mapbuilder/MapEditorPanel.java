package io.github.alyphen.immaterial_realm.builder.panel.mapbuilder;

import io.github.alyphen.immaterial_realm.builder.panel.MapBuilderPanel;
import io.github.alyphen.immaterial_realm.common.object.WorldObject;
import io.github.alyphen.immaterial_realm.common.object.WorldObjectFactory;
import io.github.alyphen.immaterial_realm.common.tile.Tile;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Set;

import static java.awt.Color.RED;
import static java.awt.event.KeyEvent.*;
import static java.util.stream.Collectors.toSet;
import static javax.swing.KeyStroke.getKeyStroke;

public class MapEditorPanel extends JPanel implements MouseListener, MouseMotionListener {

    private static final int SCROLL_SPEED = 32;

    private MapBuilderPanel mapBuilderPanel;
    private int xOffset;
    private int yOffset;
    private int xScroll;
    private int yScroll;

    public MapEditorPanel(MapBuilderPanel mapBuilderPanel) {
        this.mapBuilderPanel = mapBuilderPanel;
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(getKeyStroke(VK_W, 0, false), "scrollUp");
        inputMap.put(getKeyStroke(VK_S, 0, false), "scrollDown");
        inputMap.put(getKeyStroke(VK_D, 0, false), "scrollRight");
        inputMap.put(getKeyStroke(VK_A, 0, false), "scrollLeft");
        inputMap.put(getKeyStroke(VK_W, 0, true), "stopScrollUp");
        inputMap.put(getKeyStroke(VK_S, 0, true), "stopScrollDown");
        inputMap.put(getKeyStroke(VK_D, 0, true), "stopScrollRight");
        inputMap.put(getKeyStroke(VK_A, 0, true), "stopScrollLeft");
        getActionMap().put("scrollUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                yScroll = SCROLL_SPEED;
            }
        });
        getActionMap().put("scrollDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                yScroll = -SCROLL_SPEED;
            }
        });
        getActionMap().put("scrollRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                xScroll = -SCROLL_SPEED;
            }
        });
        getActionMap().put("scrollLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                xScroll = SCROLL_SPEED;
            }
        });
        getActionMap().put("stopScrollUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                yScroll = 0;
            }
        });
        getActionMap().put("stopScrollDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                yScroll = 0;
            }
        });
        getActionMap().put("stopScrollRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                xScroll = 0;
            }
        });
        getActionMap().put("stopScrollLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                xScroll = 0;
            }
        });
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (mapBuilderPanel.getArea() != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.translate(-xOffset, -yOffset);
            for (int row = 0; row < mapBuilderPanel.getArea().getTiles().length; row++) {
                for (int col = 0; col < mapBuilderPanel.getArea().getTiles()[row].length; col++) {
                    Tile tile = mapBuilderPanel.getArea().getTileAt(row, col);
                    if (tile != null)
                        tile.paint(graphics, col * tile.getWidth(), row * tile.getHeight());
                }
            }
            for (WorldObject object : mapBuilderPanel.getArea().getObjects()) {
                object.paint(graphics);
                graphics.setColor(RED);
                graphics.drawRect(object.getX(), object.getY(), (int) object.getBounds().getWidth() - 1, (int) object.getBounds().getHeight() - 1);
            }
            graphics2D.translate(xOffset, yOffset);
        }
    }


    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            if (mapBuilderPanel.isTilesSelected()) {
                WorldArea area = mapBuilderPanel.getArea();
                if (area != null) {
                    Tile tile = mapBuilderPanel.getTilePanel().getSelectedTile();
                    if (tile != null) {
                        area.setTileAt((event.getY() + yOffset) / tile.getHeight(), (event.getX() + xOffset) / tile.getWidth(), tile);
                    }
                }
            } else if (mapBuilderPanel.isObjectsSelected()) {
                WorldArea area = mapBuilderPanel.getArea();
                if (area != null) {
                    WorldObject object = WorldObjectFactory.createObject(mapBuilderPanel.getObjectsPanel().getSelectedObjectType());
                    if (object != null) {
                        object.setX(((event.getX() + xOffset) / (int) object.getBounds().getWidth()) * (int) object.getBounds().getWidth());
                        object.setY(((event.getY() + yOffset) / (int) object.getBounds().getHeight()) * (int) object.getBounds().getHeight());
                        if (area.getObjects().stream().filter(areaObject -> areaObject.getBounds().contains(event.getX(), event.getY()) && areaObject.getClass() == object.getClass()).collect(toSet()).size() == 0) {
                            area.addObject(object);
                        }
                    }
                }
            }
        } else if (SwingUtilities.isRightMouseButton(event)) {
            WorldArea area = mapBuilderPanel.getArea();
            if (area != null) {
                Set<WorldObject> objectsToDelete = area.getObjects().stream().filter(object -> object.getBounds().contains(event.getX(), event.getY())).collect(toSet());
                objectsToDelete.forEach(area::removeObject);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseExited(MouseEvent event) {

    }

    public void onTick() {
        xOffset -= xScroll;
        yOffset -= yScroll;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            if (mapBuilderPanel.isTilesSelected()) {
                WorldArea area = mapBuilderPanel.getArea();
                if (area != null) {
                    Tile tile = mapBuilderPanel.getTilePanel().getSelectedTile();
                    if (tile != null) {
                        area.setTileAt((event.getY() + yOffset) / tile.getHeight(), (event.getX() + xOffset) / tile.getWidth(), tile);
                    }
                }
            } else if (mapBuilderPanel.isObjectsSelected()) {
                WorldArea area = mapBuilderPanel.getArea();
                if (area != null) {
                    WorldObject object = WorldObjectFactory.createObject(mapBuilderPanel.getObjectsPanel().getSelectedObjectType());
                    if (object != null) {
                        object.setX(((event.getX() + xOffset) / (int) object.getBounds().getWidth()) * (int) object.getBounds().getWidth());
                        object.setY(((event.getY() + yOffset) / (int) object.getBounds().getHeight()) * (int) object.getBounds().getHeight());
                        if (area.getObjects().stream().filter(areaObject -> areaObject.getBounds().contains(event.getX(), event.getY()) && areaObject.getClass() == object.getClass()).collect(toSet()).size() == 0) {
                            area.addObject(object);
                        }
                    }
                }
            }
        } else if (SwingUtilities.isRightMouseButton(event)) {
            WorldArea area = mapBuilderPanel.getArea();
            if (area != null) {
                Set<WorldObject> objectsToDelete = area.getObjects().stream().filter(object -> object.getBounds().contains(event.getX(), event.getY())).collect(toSet());
                objectsToDelete.forEach(area::removeObject);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {

    }
}
