package io.github.alyphen.amethyst.common.packet.login;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketVersion extends Packet {

    private String version;

    public PacketVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

}
