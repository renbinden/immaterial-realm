package io.github.alyphen.immaterial-realm.common.packet.clientbound.login;

import io.github.alyphen.immaterial-realm.common.packet.Packet;

public class PacketVersion extends Packet {

    private String version;

    public PacketVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

}
