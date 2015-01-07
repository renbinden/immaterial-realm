package io.github.alyphen.immaterial_realm.common.packet.clientbound.player;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketPlayerJoin extends Packet {

    private long playerId;
    private String playerName;

    public PacketPlayerJoin(long playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
