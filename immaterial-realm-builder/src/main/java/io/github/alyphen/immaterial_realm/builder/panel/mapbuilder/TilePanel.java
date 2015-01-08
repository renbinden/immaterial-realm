package io.github.alyphen.immaterial_realm.builder.panel.mapbuilder;

import io.github.alyphen.immaterial_realm.builder.panel.MapBuilderPanel;
import io.github.alyphen.immaterial_realm.common.tile.Tile;
import io.github.alyphen.immaterial_realm.common.tile.TileSheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.awt.Color.WHITE;

public class TilePanel extends JPanel implements MouseListener {

    private MapBuilderPanel mapBuilderPanel;

    private Tile selectedTile;

    public TilePanel(MapBuilderPanel mapBuilderPanel) {
        this.mapBuilderPanel = mapBuilderPanel;
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        graphics.setColor(WHITE);
        graphics.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        int x = 4, y = 4;
        for (TileSheet sheet :  mapBuilderPanel.getTileSheetPanel().getSelectedTileSheets()) {
            for (Tile tile : sheet.getTiles()) {
                if (selectedTile == tile) {
                    graphics.setColor(Color.DARK_GRAY);
                    graphics.fillRect(x - 2, y - 2, sheet.getTileWidth() + 4, sheet.getTileHeight() + 4);
                }
                tile.paint(graphics, x, y);
                x += tile.getWidth() + 8;
                if (x + sheet.getTileWidth() + 4 > getWidth()) {
                    x = 4;
                    y += sheet.getTileHeight() + 8;
                }
            }
            y += sheet.getTileHeight() + 8;
        }
    }

    public Tile getTileAt(int tileX, int tileY) {
        int x = 4, y = 4;
        for (TileSheet sheet : mapBuilderPanel.getTileSheetPanel().getSelectedTileSheets()) {
            for (Tile tile : sheet.getTiles()) {
                if (tileX >= x && tileX <= x + sheet.getTileWidth() && tileY >= y && tileY <= y + sheet.getTileHeight()) {
                    return tile;
                }
                x += tile.getWidth() + 8;
                if (x + sheet.getTileWidth() + 4 > getWidth()) {
                    x = 4;
                    y += sheet.getTileHeight() + 8;
                }
            }
            y += sheet.getTileHeight() + 8;
        }
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed(MouseEvent event) {
        Tile selectTile = getTileAt(event.getX(), event.getY());
        if (selectTile != null) {
            selectedTile = selectTile;
            repaint();
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

    public Tile getSelectedTile() {
        return selectedTile;
    }
}
