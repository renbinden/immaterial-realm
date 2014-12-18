package io.github.alyphen.amethyst.common.packet.serverbound.control;

import io.github.alyphen.amethyst.common.control.Control;
import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketControlPressed extends Packet {

    private String control;

    public PacketControlPressed(Control control) {
        this.control = control.name();
    }

    public Control getControl() {
        return Control.valueOf(control);
    }

}
