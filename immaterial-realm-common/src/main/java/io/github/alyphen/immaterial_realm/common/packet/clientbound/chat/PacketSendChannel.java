package io.github.alyphen.immaterial_realm.common.packet.clientbound.chat;

import io.github.alyphen.immaterial_realm.common.chat.ChatChannel;
import io.github.alyphen.immaterial_realm.common.packet.Packet;

import java.awt.*;

public class PacketSendChannel extends Packet {

    private String name;
    private Color colour;
    private int radius;

    public PacketSendChannel(String name, Color colour, int radius) {
        this.name = name;
        this.colour = colour;
        this.radius = radius;
    }

    public PacketSendChannel(ChatChannel channel) {
        this.name = channel.getName();
        this.colour = channel.getColour();
        this.radius = channel.getRadius();
    }

    public String getName() {
        return name;
    }

    public Color getColour() {
        return colour;
    }

    public int getRadius() {
        return radius;
    }

}
