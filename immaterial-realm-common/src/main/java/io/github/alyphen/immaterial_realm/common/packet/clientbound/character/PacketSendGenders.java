package io.github.alyphen.immaterial_realm.common.packet.clientbound.character;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketSendGenders extends Packet {

    private String[] genders;

    public PacketSendGenders(String... genders) {
        this.genders = genders;
    }

    public String[] getGenders() {
        return genders;
    }

}
