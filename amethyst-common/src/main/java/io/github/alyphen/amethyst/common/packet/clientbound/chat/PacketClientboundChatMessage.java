package io.github.alyphen.amethyst.common.packet.clientbound.chat;

import io.github.alyphen.amethyst.common.character.Character;
import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketClientboundChatMessage extends Packet {

    private long characterId;
    private String channel;
    private String message;

    public PacketClientboundChatMessage(Character character, String channel, String message) {
        this.characterId = character.getId();
        this.channel = channel;
        this.message = message;
    }

    public long getCharacterId() {
        return characterId;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

}
