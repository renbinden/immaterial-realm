package io.github.alyphen.amethyst.common.packet.world;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketShowArea extends Packet {

    private String area;

    public PacketShowArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

}
