package io.github.alyphen.amethyst.client.chat;

import java.awt.*;

public class ChatChannel {

    private String name;
    private Color colour;
    private String format;

    public ChatChannel(String name, Color colour, String format) {
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
