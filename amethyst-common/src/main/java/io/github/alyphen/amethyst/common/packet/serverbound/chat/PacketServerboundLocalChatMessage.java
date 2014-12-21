package io.github.alyphen.amethyst.common.packet.serverbound.chat;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketServerboundLocalChatMessage extends Packet {

    private String channel;
    private String message;

    public PacketServerboundLocalChatMessage(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

}
