package io.github.alyphen.immaterial_realm.common.packet.world;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketShowArea extends Packet {

    private String area;

    public PacketShowArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

}
