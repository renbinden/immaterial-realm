package io.github.alyphen.amethyst.common.packet.object;

import io.github.alyphen.amethyst.common.packet.Packet;
import io.github.alyphen.amethyst.common.sprite.Sprite;

import java.awt.*;
import java.io.IOException;

public class PacketSendObjectType extends Packet {

    private String name;
    private byte[][] sprite;
    private int frameDelay;
    private Rectangle bounds;

    public PacketSendObjectType(String name, Sprite sprite, Rectangle bounds) {
        this.name = name;
        try {
            this.sprite = sprite.toByteArray();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        this.frameDelay = sprite.getFrameDelay();
        this.bounds = bounds;
    }

    public String getName() {
        return name;
    }

    public Sprite getSprite() {
        try {
            return Sprite.fromByteArray(sprite, frameDelay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
