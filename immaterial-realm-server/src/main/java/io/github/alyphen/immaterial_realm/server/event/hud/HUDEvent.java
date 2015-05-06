package io.github.alyphen.immaterial_realm.server.event.hud;

import io.github.alyphen.immaterial_realm.common.hud.HUD;
import io.github.alyphen.immaterial_realm.server.event.Event;

public class HUDEvent extends Event {

    private HUD hud;

    public HUDEvent(HUD hud) {
        this.hud = hud;
    }

    public HUD getHUD() {
        return hud;
    }

}
