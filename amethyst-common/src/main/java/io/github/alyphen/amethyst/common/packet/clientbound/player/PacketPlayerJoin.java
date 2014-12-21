package io.github.alyphen.amethyst.common.packet.clientbound.player;

import io.github.alyphen.amethyst.common.packet.Packet;

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
