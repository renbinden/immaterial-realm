package io.github.alyphen.immaterial_realm.common.packet.clientbound.entity;

import io.github.alyphen.immaterial_realm.common.entity.Entity;
import io.github.alyphen.immaterial_realm.common.packet.Packet;

import java.util.UUID;

public class PacketEntitySpawn extends Packet {

    private UUID entityUUID;
    private Class<? extends Entity> entityClass;
    private String areaName;
    private int x;
    private int y;

    public PacketEntitySpawn(UUID entityUUID, Class<? extends Entity> entityClass, String areaName, int x, int y) {
        this.entityUUID = entityUUID;
        this.entityClass = entityClass;
        this.areaName = areaName;
        this.x = x;
        this.y = y;
    }

    public UUID getEntityUUID() {
        return entityUUID;
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
