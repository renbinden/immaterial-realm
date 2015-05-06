package io.github.alyphen.immaterial_realm.server.event.hud;

import io.github.alyphen.immaterial_realm.common.hud.HUD;
import io.github.alyphen.immaterial_realm.common.player.Player;

public class HUDCreateEvent extends HUDEvent {

    private Player player;

    public HUDCreateEvent(HUD hud, Player player) {
        super(hud);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
