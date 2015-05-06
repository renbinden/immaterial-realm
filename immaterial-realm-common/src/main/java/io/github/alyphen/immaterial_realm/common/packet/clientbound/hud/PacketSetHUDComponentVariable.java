package io.github.alyphen.immaterial_realm.common.packet.clientbound.hud;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketSetHUDComponentVariable extends Packet {

    private String hudComponent;
    private String variable;
    private Object value;

    public PacketSetHUDComponentVariable(String hudComponent, String variable, Object value) {
        this.hudComponent = hudComponent;
        this.variable = variable;
        this.value = value;
    }

    public String getComponent() {
        return hudComponent;
    }

    public String getVariable() {
        return variable;
    }

    public Object getValue() {
        return value;
    }

}
