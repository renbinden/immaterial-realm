package io.github.alyphen.immaterial_realm.common.packet.clientbound.object;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import java.awt.*;
import java.io.IOException;

import static java.util.logging.Level.SEVERE;

public class PacketSendObjectType extends Packet {

    private String name;
    private byte[][] sprite;
    private int frameDelay;
    private Rectangle bounds;

    public PacketSendObjectType(String name, Sprite sprite, Rectangle bounds) {
        this.name = name;
        if (sprite != null) {
            try {
                this.sprite = sprite.toByteArray();
            } catch (IOException exception) {
                ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to encode object sprite", exception);
            }
            this.frameDelay = sprite.getFrameDelay();
        }
        this.bounds = bounds;
    }

    public String getName() {
        return name;
    }

    public Sprite getSprite() {
        if (sprite != null) {
            try {
                return Sprite.fromByteArray("__object_" + name, sprite, frameDelay);
            } catch (IOException exception) {
                ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to decode object sprite", exception);
            }
        }
        return null;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
