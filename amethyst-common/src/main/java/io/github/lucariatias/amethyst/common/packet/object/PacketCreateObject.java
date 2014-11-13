package io.github.lucariatias.amethyst.common.packet.object;

import io.github.lucariatias.amethyst.common.packet.Packet;

public class PacketCreateObject extends Packet {

    private String type;
    private String world;
    private String area;
    private int x;
    private int y;

    public PacketCreateObject(String type, String world, String area, int x, int y) {
        this.type = type;
        this.world = world;
        this.area = area;
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public String getWorld() {
        return world;
    }

    public String getArea() {
        return area;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
