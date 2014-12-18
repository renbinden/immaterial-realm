package io.github.alyphen.amethyst.common.packet.clientbound.entity;

import io.github.alyphen.amethyst.common.packet.Packet;
import io.github.alyphen.amethyst.common.world.Direction;

public class PacketEntityMove extends Packet {

    private long entityId;
    private String directionFacing;
    private String areaName;
    private int x;
    private int y;

    public PacketEntityMove(long entityId, Direction directionFacing, String areaName, int x, int y) {
        this.entityId = entityId;
        this.directionFacing = directionFacing.name();
        this.areaName = areaName;
        this.x = x;
        this.y = y;
    }

    public long getEntityId() {
        return entityId;
    }

    public Direction getDirectionFacing() {
        return Direction.valueOf(directionFacing);
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
