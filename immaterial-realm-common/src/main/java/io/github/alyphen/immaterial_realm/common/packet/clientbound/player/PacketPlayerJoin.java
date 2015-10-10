package io.github.alyphen.immaterial_realm.common.packet.clientbound.player;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

import java.util.UUID;

public class PacketPlayerJoin extends Packet {

    private UUID playerUUID;
    private String playerName;

    public PacketPlayerJoin(UUID playerUUID, String playerName) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }
}
