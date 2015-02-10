package io.github.alyphen.immaterial_realm.common.packet.clientbound.sprite;

import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

public class PacketAddFaceSprite extends PacketAddSprite {

    public PacketAddFaceSprite(String name, Sprite sprite, int frameDelay) {
        super(name, sprite, frameDelay);
    }

}
