package io.github.alyphen.immaterial_realm.server.event.player;

import io.github.alyphen.immaterial_realm.common.player.Player;

public class PlayerJoinEvent extends PlayerEvent {

    public PlayerJoinEvent(Player player) {
        super(player);
    }

}
