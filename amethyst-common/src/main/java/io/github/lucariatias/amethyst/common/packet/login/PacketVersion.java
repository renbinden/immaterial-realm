package io.github.lucariatias.amethyst.common.packet.login;

import io.github.lucariatias.amethyst.common.packet.Packet;

public class PacketVersion extends Packet {

    private String version;

    public PacketVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

}
