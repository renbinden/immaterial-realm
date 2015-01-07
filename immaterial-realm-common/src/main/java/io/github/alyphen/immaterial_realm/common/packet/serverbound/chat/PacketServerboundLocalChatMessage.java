package io.github.alyphen.immaterial_realm.common.packet.serverbound.chat;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

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
