package io.github.alyphen.amethyst.common.tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

    private TileSheet sheet;
    private int row;
    private int col;
    private BufferedImage image;

    public Tile(TileSheet sheet, int row, int col) {
        this.sheet = sheet;
        this.row = row;
        this.col = col;
        image = sheet.getSheet().getSubimage(col * sheet.getTileWidth(), row * sheet.getTileHeight(), sheet.getTileWidth(), sheet.getTileHeight());
    }

    public TileSheet getSheet() {
        return sheet;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    public int getWidth() {
        return getSheet().getTileWidth();
    }

    public int getHeight() {
        return getSheet().getTileHeight();
    }

    public void paint(Graphics graphics, int x, int y) {
        graphics.drawImage(image, x, y, null);
    }

}
