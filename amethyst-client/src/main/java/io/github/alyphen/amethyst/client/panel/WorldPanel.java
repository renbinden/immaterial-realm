package io.github.alyphen.amethyst.client.panel;

import io.github.alyphen.amethyst.client.AmethystClient;
import io.github.alyphen.amethyst.common.entity.EntityCharacter;
import io.github.alyphen.amethyst.common.tile.Tile;
import io.github.alyphen.amethyst.common.world.World;
import io.github.alyphen.amethyst.common.world.WorldArea;

import javax.swing.*;
import java.awt.*;

public class WorldPanel extends JPanel {

    private AmethystClient client;

    private boolean active;
    private World world;
    private WorldArea area;
    private EntityCharacter playerCharacter;

    public WorldPanel(AmethystClient client) {
        this.client = client;
    }

    public void onTick() {
        world.onTick();
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (getArea() != null && getPlayerCharacter() != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.translate(getCameraX(), getCameraY());
            for (int row = 0; row < area.getRows(); row++) {
                for (int col = 0; col < area.getColumns(); col++) {
                    Tile tile = getArea().getTileAt(row, col);
                    int x = col * tile.getSheet().getTileWidth();
                    int y = row * tile.getSheet().getTileHeight();
                    if (x + tile.getSheet().getTileWidth() >= getCameraX() - (getWidth() / 2)
                            && x <= getCameraX() + (getWidth() / 2)
                            && y + tile.getSheet().getTileHeight() >= getCameraY() - (getHeight() / 2)
                            && y <= getCameraY() + (getHeight() / 2))
                        tile.paint(graphics, x, y);
                }
            }
            getArea().getObjects().stream().filter(object -> object.getX() + object.getBounds().getWidth() >= getCameraX() - (getWidth() / 2)
                    && object.getX() <= getCameraX() + (getWidth() / 2)
                    && object.getY() + object.getBounds().getHeight() >= getCameraY() - (getHeight() / 2)
                    && object.getY() <= getCameraY() + (getHeight() / 2)).forEach(object -> {
                graphics2D.translate(object.getX(), object.getY());
                object.paint(graphics);
                graphics2D.translate(-object.getX(), -object.getY());
            });
            getArea().getEntities().stream().filter(entity -> entity.getX() + entity.getBounds().getWidth() >= getCameraX() - (getWidth() / 2)
                    && entity.getX() <= getCameraX() + (getWidth() / 2)
                    && entity.getY() + entity.getBounds().getHeight() >= getCameraY() - (getHeight() / 2)
                    && entity.getY() <= getCameraY() + (getHeight() /2)).forEach(entity -> {
                graphics2D.translate(entity.getX(), entity.getY());
                entity.paint(graphics);
                graphics2D.translate(-entity.getX(), -entity.getY());
            });
            graphics2D.translate(-getCameraX(), -getCameraY());
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public WorldArea getArea() {
        return area;
    }

    public void setArea(WorldArea area) {
        world.addArea(area);
        if (this.area != null) world.removeArea(this.area);
        this.area = area;
    }

    public EntityCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public void setPlayerCharacter(EntityCharacter player) {
        this.playerCharacter = player;
    }

    private int getCameraX() {
        return (getWidth() / 2) - getPlayerCharacter().getX();
    }

    private int getCameraY() {
        return (getHeight() / 2) - getPlayerCharacter().getY();
    }

}
