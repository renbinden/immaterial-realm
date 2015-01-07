package io.github.alyphen.immaterial_realm.common.packet.clientbound.player;

import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PacketSendPlayers extends Packet {

    private Map<Long, String> players;

    public PacketSendPlayers(Set<Player> players) {
        this.players = new HashMap<>();
        for (Player player : players) {
            this.players.put(player.getId(), player.getName());
        }
    }

    public Set<Player> getPlayers() {
        return this.players.entrySet().stream().map(entry -> new Player(entry.getKey(), entry.getValue())).collect(Collectors.toSet());
    }

}
