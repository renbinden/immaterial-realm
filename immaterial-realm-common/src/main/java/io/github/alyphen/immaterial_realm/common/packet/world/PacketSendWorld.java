package io.github.alyphen.immaterial_realm.common.packet.world;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketSendWorld extends Packet {

    private String name;

    public PacketSendWorld(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
