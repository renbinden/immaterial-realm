package io.github.alyphen.immaterial_realm.common.entity;

import io.github.alyphen.immaterial_realm.common.sprite.Sprite;
import io.github.alyphen.immaterial_realm.common.world.Direction;

import java.awt.*;

import static io.github.alyphen.immaterial_realm.common.world.Direction.DOWN;

public abstract class Entity {

    private long id;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int oldDx;
    private int oldDy;
    private boolean speedChanged;
    private boolean movementCancelled;
    private boolean forceUpdate;
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

    public int distance(Entity entity) {
        return (int) Math.round(Math.sqrt(distanceSquared(entity)));
    }

    public int distanceSquared(Entity entity) {
        int dx = entity.getX() - getX();
        int dy = entity.getY() - getY();
        return (dx * dx) + (dy * dy);
    }

    public void setVerticalSpeed(int vSpeed) {
        dy = vSpeed;
    }

    public int getVerticalSpeed() {
        return dy;
    }

    public void setHorizontalSpeed(int hSpeed) {
        dx = hSpeed;
    }

    public int getHorizontalSpeed() {
        return dx;
    }

    public void onTick() {
        if (!movementCancelled) {
            speedChanged = oldDx != dx || oldDy != dy;
            if (speedChanged) {
                oldDx = dx;
                oldDy = dy;
            } else {
                x += dx;
                y += dy;
            }
        }
        movementCancelled = false;
    }

    public boolean isSpeedChanged() {
        return speedChanged;
    }

    public boolean isMovementCancelled() {
        return movementCancelled;
    }

    public void setMovementCancelled(boolean movementCancelled) {
        this.movementCancelled = movementCancelled;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public void setDirectionFacing(Direction direction) {
        this.directionFacing = direction;
    }

    public abstract void paint(Graphics graphics);

    public abstract Sprite getSprite();

    public abstract Rectangle getBounds();

}
