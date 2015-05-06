package io.github.alyphen.immaterial_realm.common.packet.clientbound.hud;

import io.github.alyphen.immaterial_realm.common.hud.HUDComponent;
import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketSendHUDComponent extends Packet {

    private HUDComponent component;

    public PacketSendHUDComponent(HUDComponent component) {
        this.component = component;
    }

    public HUDComponent getComponent() {
        return component;
    }

}
