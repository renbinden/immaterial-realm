package io.github.alyphen.amethyst.common.packet.tile;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketRequestTileSheet extends Packet {

    private String name;

    public PacketRequestTileSheet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
