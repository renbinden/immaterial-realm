package io.github.alyphen.immaterial_realm.common.packet.clientbound.chat;

import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.player.Player;

import java.util.UUID;

public class PacketClientboundGlobalChatMessage extends Packet {

    private UUID playerUUID;
    private String channel;
    private String message;

    public PacketClientboundGlobalChatMessage(Player player, String channel, String message) {
        this.playerUUID = player.getUUID();
        this.channel = channel;
        this.message = message;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

}
