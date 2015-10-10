package io.github.alyphen.immaterial_realm.common.packet.clientbound.player;

import java.util.UUID;

public class PacketPlayerLeave {

    private UUID playerUUID;

    public PacketPlayerLeave(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

}
