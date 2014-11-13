package io.github.alyphen.amethyst.common.packet.world;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketSendWorld extends Packet {

    private String name;

    public PacketSendWorld(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
