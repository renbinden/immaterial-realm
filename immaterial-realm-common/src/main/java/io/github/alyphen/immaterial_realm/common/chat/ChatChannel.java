package io.github.alyphen.immaterial_realm.common.chat;

import java.awt.*;

public class ChatChannel implements Comparable<ChatChannel> {

    private String name;
    private Color colour;
    private int radius;
    private boolean defaultChannel;

    public ChatChannel(String name, Color colour, int radius, boolean defaultChannel) {
        this.name = name;
        this.colour = colour;
        this.radius = radius;
        this.defaultChannel = defaultChannel;
    }

    public ChatChannel(String name, Color colour, int radius) {
        this(name, colour, radius, false);
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

    public boolean isDefaultChannel() {
        return defaultChannel;
    }

    public void setDefaultChannel(boolean defaultChannel) {
        this.defaultChannel = defaultChannel;
    }

    @Override
    public int compareTo(ChatChannel channel) {
        return getName().compareTo(channel.getName());
    }

}
