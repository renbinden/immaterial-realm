package io.github.alyphen.amethyst.common.packet.serverbound.chat;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketServerboundChatMessage extends Packet {

    private String channel;
    private String message;

    public PacketServerboundChatMessage(String channel, String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

}
