package io.github.alyphen.immaterial_realm.common.sprite;

import io.github.alyphen.immaterial_realm.common.database.TableRow;

import java.util.UUID;

public class SpriteFrame extends TableRow {

    private UUID spriteUUID;
    private UUID frameUUID;

    public SpriteFrame(UUID uuid, Sprite sprite, IndexedImage image) {
        this(uuid, sprite.getUUID(), image.getUUID());
    }

    public SpriteFrame(Sprite sprite, IndexedImage image) {
        this(UUID.randomUUID(), sprite, image);
    }

    public SpriteFrame(UUID uuid, UUID spriteUUID, UUID imageUUID) {
        super(uuid);
        this.spriteUUID = spriteUUID;
        this.frameUUID = imageUUID;
    }

    public SpriteFrame(UUID spriteUUID, UUID frameUUID) {
        this(UUID.randomUUID(), spriteUUID, frameUUID);
    }

    public UUID getSpriteUUID() {
        return spriteUUID;
    }

    public UUID getFrameUUID() {
        return frameUUID;
    }

}
