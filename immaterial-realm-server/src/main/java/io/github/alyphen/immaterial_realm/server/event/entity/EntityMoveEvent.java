package io.github.alyphen.immaterial_realm.server.event.entity;

import io.github.alyphen.immaterial_realm.common.entity.Entity;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;
import io.github.alyphen.immaterial_realm.server.event.Cancellable;

public class EntityMoveEvent extends EntityEvent implements Cancellable {

    private boolean cancelled;
    private WorldArea newArea;
    private int newX;
    private int newY;

    public EntityMoveEvent(Entity entity, WorldArea newArea, int newX, int newY) {
        super(entity);
        this.newArea = newArea;
        this.newX = newX;
        this.newY = newY;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public WorldArea getNewArea() {
        return newArea;
    }

    public void setNewArea(WorldArea newArea) {
        this.newArea = newArea;
    }

    public int getNewX() {
        return newX;
    }

    public void setNewX(int newX) {
        this.newX = newX;
    }

    public int getNewY() {
        return newY;
    }

    public void setNewY(int newY) {
        this.newY = newY;
    }

}
