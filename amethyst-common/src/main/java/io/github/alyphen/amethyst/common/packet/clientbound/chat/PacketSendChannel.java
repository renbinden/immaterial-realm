package io.github.alyphen.amethyst.common.packet.clientbound.chat;

import io.github.alyphen.amethyst.common.packet.Packet;

import java.awt.*;

public class PacketSendChannel extends Packet {

    private String name;
    private Color colour;
    private String format;

    public PacketSendChannel(String name, Color colour, String format) {
        this.name = name;
        this.colour = colour;
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public Color getColour() {
        return colour;
    }

    public String getFormat() {
        return format;
    }

}
