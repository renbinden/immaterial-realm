package io.github.alyphen.immaterial_realm.common.packet.clientbound.chat;

import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.packet.Packet;

import java.util.UUID;

public class PacketClientboundLocalChatMessage extends Packet {

    private UUID characterUUID;
    private String channel;
    private String message;

    public PacketClientboundLocalChatMessage(Character character, String channel, String message) {
        this.characterUUID = character.getUUID();
        this.channel = channel;
        this.message = message;
    }

    public UUID getCharacterUUID() {
        return characterUUID;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

}
