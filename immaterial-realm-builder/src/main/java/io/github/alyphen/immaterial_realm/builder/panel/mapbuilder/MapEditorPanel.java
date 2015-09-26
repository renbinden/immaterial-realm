package io.github.alyphen.immaterial_realm.builder.panel.mapbuilder;

import io.github.alyphen.immaterial_realm.builder.panel.MapBuilderPanel;
import io.github.alyphen.immaterial_realm.common.tile.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.awt.event.KeyEvent.*;
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
            graphics2D.translate(xOffset, yOffset);
        }
    }


    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (mapBuilderPanel.getArea() != null) {
            Tile tile = mapBuilderPanel.getTilePanel().getSelectedTile();
            if (tile != null) {
                mapBuilderPanel.getArea().setTileAt((event.getX() + xOffset) / tile.getWidth(), (event.getY() + yOffset) / tile.getHeight(), tile);
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
        if (mapBuilderPanel.getArea() != null) {
            Tile tile = mapBuilderPanel.getTilePanel().getSelectedTile();
            if (tile != null) {
                mapBuilderPanel.getArea().setTileAt((event.getX() + xOffset) / tile.getWidth(), (event.getY() + yOffset) / tile.getHeight(), tile);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {

    }
}
