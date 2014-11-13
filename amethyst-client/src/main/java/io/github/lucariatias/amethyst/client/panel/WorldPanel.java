package io.github.lucariatias.amethyst.client.panel;

import io.github.lucariatias.amethyst.client.AmethystClient;
import io.github.lucariatias.amethyst.common.entity.EntityCharacter;
import io.github.lucariatias.amethyst.common.object.WorldObject;
import io.github.lucariatias.amethyst.common.tile.Tile;
import io.github.lucariatias.amethyst.common.world.World;
import io.github.lucariatias.amethyst.common.world.WorldArea;

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
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (getArea() != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            for (int row = 0; row < area.getRows(); row++) {
                for (int col = 0; col < area.getColumns(); col++) {
                    Tile tile = getArea().getTileAt(row, col);
                    tile.paint(graphics, col * tile.getSheet().getTileWidth(), row * tile.getSheet().getTileHeight());
                }
            }
            for (WorldObject object : getArea().getObjects()) {
                graphics2D.translate(-object.getX(), -object.getY());
                object.paint(graphics);
                graphics2D.translate(object.getX(), object.getY());
            }
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

}
