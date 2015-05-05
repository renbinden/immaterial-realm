package io.github.alyphen.immaterial_realm.server.event.entity;

import io.github.alyphen.immaterial_realm.common.entity.Entity;
import io.github.alyphen.immaterial_realm.common.world.World;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;
import io.github.alyphen.immaterial_realm.server.event.Cancellable;

public class EntitySpawnEvent extends EntityEvent implements Cancellable {

    private boolean cancelled;
    private World world;
    private WorldArea area;
    private int x;
    private int y;

    public EntitySpawnEvent(Entity entity, World world, WorldArea area, int x, int y) {
        super(entity);
        this.world = world;
        this.area = area;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
        this.area = area;
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

}
