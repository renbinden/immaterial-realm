package io.github.alyphen.immaterial_realm.common.packet.clientbound.login;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketVersion extends Packet {

    private String version;

    public PacketVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

}
