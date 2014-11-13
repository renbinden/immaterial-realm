package io.github.lucariatias.amethyst.common.packet.entity;

import io.github.lucariatias.amethyst.common.entity.Entity;
import io.github.lucariatias.amethyst.common.packet.Packet;

public class PacketEntitySpawn extends Packet {

    private long id;
    private Class<? extends Entity> entityClass;
    private String areaName;
    private int x;
    private int y;

    public PacketEntitySpawn(long id, Class<? extends Entity> entityClass, String areaName, int x, int y) {
        this.id = id;
        this.entityClass = entityClass;
        this.areaName = areaName;
        this.x = x;
        this.y = y;
    }

    public long getId() {
        return id;
    }

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    public String getAreaName() {
        return areaName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
