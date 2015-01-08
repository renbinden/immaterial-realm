package io.github.alyphen.immaterial-realm.common.packet.serverbound.tile;

import io.github.alyphen.immaterial-realm.common.packet.Packet;

public class PacketRequestTileSheet extends Packet {

    private String name;

    public PacketRequestTileSheet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
