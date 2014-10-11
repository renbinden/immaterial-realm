package io.github.lucariatias.amethyst.common.packet.chat;

import io.github.lucariatias.amethyst.common.packet.Packet;

public class PacketChatMessage extends Packet {

    private String channel;
    private String message;

    public PacketChatMessage(String channel, String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

}
