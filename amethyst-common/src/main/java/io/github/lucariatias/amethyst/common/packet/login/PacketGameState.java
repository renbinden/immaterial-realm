package io.github.lucariatias.amethyst.common.packet.login;

import io.github.lucariatias.amethyst.common.packet.Packet;

public class PacketGameState extends Packet {

    private String message;

    public PacketGameState(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
