package io.github.alyphen.amethyst.common.packet.clientbound.chat;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketSetChannel extends Packet {

    private String channel;

    public PacketSetChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

}
