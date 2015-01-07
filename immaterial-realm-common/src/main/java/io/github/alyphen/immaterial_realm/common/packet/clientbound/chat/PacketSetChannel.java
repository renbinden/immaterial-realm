package io.github.alyphen.immaterial_realm.common.packet.clientbound.chat;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketSetChannel extends Packet {

    private String channel;

    public PacketSetChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

}
