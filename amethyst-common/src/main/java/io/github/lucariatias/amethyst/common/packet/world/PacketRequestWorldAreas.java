package io.github.lucariatias.amethyst.common.packet.world;

import io.github.lucariatias.amethyst.common.packet.Packet;

public class PacketRequestWorldAreas extends Packet {

    private String world;

    public PacketRequestWorldAreas(String world) {
        this.world = world;
    }

    public String getWorld() {
        return world;
    }

}
