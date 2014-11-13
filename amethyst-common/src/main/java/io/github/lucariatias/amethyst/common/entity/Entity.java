package io.github.lucariatias.amethyst.common.entity;

import io.github.lucariatias.amethyst.common.object.WorldObject;
import io.github.lucariatias.amethyst.common.sprite.Sprite;
import io.github.lucariatias.amethyst.common.world.Direction;

import java.awt.*;

import static io.github.lucariatias.amethyst.common.world.Direction.DOWN;

public abstract class Entity extends WorldObject {
    private Direction directionFacing;

    public Entity(long id, Sprite sprite, Rectangle bounds) {
        super(id, sprite, bounds);
        directionFacing = DOWN;
    }

    public Direction getDirectionFacing() {
        return directionFacing;
    }

    public void setDirectionFacing(Direction direction) {
        this.directionFacing = direction;
    }

}
