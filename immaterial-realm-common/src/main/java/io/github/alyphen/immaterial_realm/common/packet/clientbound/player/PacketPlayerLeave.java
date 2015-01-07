package io.github.alyphen.immaterial_realm.common.packet.clientbound.player;

public class PacketPlayerLeave {

    private long playerId;

    public PacketPlayerLeave(long playerId) {
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

}
