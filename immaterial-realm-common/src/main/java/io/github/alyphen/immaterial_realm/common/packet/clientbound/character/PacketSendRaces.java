package io.github.alyphen.immaterial_realm.common.packet.clientbound.character;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketSendRaces extends Packet {

    private String[] races;

    public PacketSendRaces(String... races) {
        this.races = races;
    }

    public String[] getRaces() {
        return races;
    }

}
