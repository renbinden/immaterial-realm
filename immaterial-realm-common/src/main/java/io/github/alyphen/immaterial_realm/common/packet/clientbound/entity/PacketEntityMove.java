package io.github.alyphen.immaterial_realm.common.packet.clientbound.entity;

import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.world.Direction;

public class PacketEntityMove extends Packet {

    private long entityId;
    private String directionFacing;
    private String areaName;
    private int x;
    private int y;
    private int dx;
    private int dy;

    public PacketEntityMove(long entityId, Direction directionFacing, String areaName, int x, int y, int dx, int dy) {
        this.entityId = entityId;
        this.directionFacing = directionFacing.name();
        this.areaName = areaName;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
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

    public int getHorizontalSpeed() {
        return dx;
    }

    public int getVerticalSpeed() {
        return dy;
    }

}
