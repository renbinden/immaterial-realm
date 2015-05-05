package io.github.alyphen.immaterial_realm.server.event.player;

import io.github.alyphen.immaterial_realm.common.player.Player;
import io.github.alyphen.immaterial_realm.server.event.Event;

public abstract class PlayerEvent extends Event {

    private Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
