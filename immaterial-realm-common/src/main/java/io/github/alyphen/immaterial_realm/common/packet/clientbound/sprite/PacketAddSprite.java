package io.github.alyphen.immaterial_realm.common.packet.clientbound.sprite;

import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import java.io.IOException;

public class PacketAddSprite extends Packet {

    private String name;
    private byte[][] sprite;
    private int frameDelay;

    public PacketAddSprite(String name, Sprite sprite, int frameDelay) {
        this.name = name;
        try {
            this.sprite = sprite.toByteArray();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        this.frameDelay = frameDelay;
    }

    public String getName() {
        return name;
    }

    public Sprite getSprite() {
        try {
            return Sprite.fromByteArray(name, sprite, frameDelay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
