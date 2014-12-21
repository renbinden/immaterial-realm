package io.github.alyphen.amethyst.common.packet.serverbound.chat;

import io.github.alyphen.amethyst.common.packet.Packet;
import io.github.alyphen.amethyst.common.player.Player;

public class PacketServerboundGlobalChatMessage extends Packet {

    private String channel;
    private String message;

    public PacketServerboundGlobalChatMessage(String channel, String message) {
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
