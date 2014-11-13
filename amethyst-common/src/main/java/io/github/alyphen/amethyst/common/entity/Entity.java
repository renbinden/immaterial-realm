package io.github.alyphen.amethyst.common.entity;

import io.github.alyphen.amethyst.common.object.WorldObject;
import io.github.alyphen.amethyst.common.sprite.Sprite;
import io.github.alyphen.amethyst.common.world.Direction;

import java.awt.*;

import static io.github.alyphen.amethyst.common.world.Direction.DOWN;

public abstract class Entity extends WorldObject {
    private Direction directionFacing;

    public Entity(long id, String type, Sprite sprite, Rectangle bounds) {
        super(id, type, sprite, bounds);
        directionFacing = DOWN;
    }

    public Direction getDirectionFacing() {
        return directionFacing;
    }

    public void setDirectionFacing(Direction direction) {
        this.directionFacing = direction;
    }

}
