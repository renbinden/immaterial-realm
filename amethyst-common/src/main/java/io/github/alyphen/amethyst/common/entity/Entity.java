package io.github.alyphen.amethyst.common.entity;

import io.github.alyphen.amethyst.common.sprite.Sprite;
import io.github.alyphen.amethyst.common.world.Direction;

import java.awt.*;

import static io.github.alyphen.amethyst.common.world.Direction.DOWN;

public abstract class Entity {

    private long id;
    private int x;
    private int y;
    private Direction directionFacing;

    public Entity(long id) {
        this.id = id;
        directionFacing = DOWN;
    }

    public long getId() {
        return id;
    }

    public Direction getDirectionFacing() {
        return directionFacing;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirectionFacing(Direction direction) {
        this.directionFacing = direction;
    }

    public abstract void paint(Graphics graphics);

    public abstract Sprite getSprite();

    public abstract Rectangle getBounds();

}
