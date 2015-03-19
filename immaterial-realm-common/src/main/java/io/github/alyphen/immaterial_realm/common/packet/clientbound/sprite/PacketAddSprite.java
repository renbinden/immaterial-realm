package io.github.alyphen.immaterial_realm.common.packet.clientbound.sprite;

import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import java.io.IOException;

public class PacketAddSprite extends Packet {

    private String name;
    private byte[][] sprite;
    private int frameDelay;

    public PacketAddSprite(Sprite sprite) {
        this.name = sprite.getName();
        try {
            this.sprite = sprite.toByteArray();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        this.frameDelay = sprite.getFrameDelay();
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
