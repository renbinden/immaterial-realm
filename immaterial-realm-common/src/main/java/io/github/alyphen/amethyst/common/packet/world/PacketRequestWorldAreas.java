package io.github.alyphen.immaterial-realm.common.packet.world;

import io.github.alyphen.immaterial-realm.common.packet.Packet;

public class PacketRequestWorldAreas extends Packet {

    private String world;

    public PacketRequestWorldAreas(String world) {
        this.world = world;
    }

    public String getWorld() {
        return world;
    }

}
