package io.github.alyphen.immaterial_realm.common.packet.clientbound.sprite;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import java.io.IOException;

import static java.util.logging.Level.SEVERE;

public class PacketAddSprite extends Packet {

    private String name;
    private byte[][] sprite;
    private int frameDelay;

    public PacketAddSprite(ImmaterialRealm immaterialRealm, Sprite sprite) {
        this.name = sprite.getName();
        try {
            this.sprite = sprite.toByteArray();
        } catch (IOException exception) {
            immaterialRealm.getLogger().log(SEVERE, "Failed to encode sprite", exception);
        }
        this.frameDelay = sprite.getFrameDelay();
    }

    public String getName() {
        return name;
    }

    public Sprite getSprite(ImmaterialRealm immaterialRealm) {
        try {
            return immaterialRealm.getSpriteManager().createSprite(name, sprite, frameDelay);
        } catch (IOException exception) {
            immaterialRealm.getLogger().log(SEVERE, "Failed to decode sprite", exception);
        }
        return null;
    }

}
