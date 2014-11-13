package io.github.lucariatias.amethyst.common.packet.character;

import io.github.lucariatias.amethyst.common.packet.Packet;

public class PacketRequestCharacterSprites extends Packet {

    private long characterId;

    public PacketRequestCharacterSprites(long characterId) {
        this.characterId = characterId;
    }

    public long getCharacterId() {
        return characterId;
    }

}
