package io.github.alyphen.amethyst.common.chat;

import java.awt.*;

public class ChatChannel implements Comparable<ChatChannel> {

    private String name;
    private Color colour;
    private int radius;

    public ChatChannel(String name, Color colour, int radius) {
        this.name = name;
        this.colour = colour;
        this.radius = radius;
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

    @Override
    public int compareTo(ChatChannel channel) {
        return getName().compareTo(channel.getName());
    }

}
