package io.github.lucariatias.amethyst.client.panel;

import io.github.lucariatias.amethyst.common.world.World;

import javax.swing.*;

public class WorldPanel extends JPanel {

    private boolean active;
    private World world;

    public void onTick() {

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
