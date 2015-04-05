package io.github.alyphen.immaterial_realm.common.sprite;

import io.github.alyphen.immaterial_realm.common.database.TableRow;

public class SpriteFrame extends TableRow {

    private long id;
    private long spriteId;
    private long frameId;

    public SpriteFrame(long id, Sprite sprite, IndexedImage image) {
        this(sprite, image);
        this.id = id;
    }

    public SpriteFrame(Sprite sprite, IndexedImage image) {
        this(sprite.getId(), image.getId());
    }

    public SpriteFrame(long id, long spriteId, long imageId) {
        super(id);
        this.spriteId = spriteId;
        this.frameId = imageId;
    }

    public SpriteFrame(long spriteId, long imageId) {
        this(0, spriteId, imageId);
    }

    public long getId() {
        return id;
    }

    public long getSpriteId() {
        return spriteId;
    }

    public long getFrameId() {
        return frameId;
    }

}
