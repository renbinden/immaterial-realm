package io.github.alyphen.immaterial_realm.common.object;

import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import java.awt.*;

public abstract class WorldObjectInitializer {

    private String objectName;
    private Sprite objectSprite;
    private Rectangle objectBounds;

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Sprite getObjectSprite() {
        return objectSprite;
    }

    public void setObjectSprite(Sprite objectSprite) {
        this.objectSprite = objectSprite;
    }

    public Rectangle getObjectBounds() {
        return objectBounds;
    }

    public void setObjectBounds(Rectangle objectBounds) {
        this.objectBounds = objectBounds;
    }

    public abstract WorldObject initialize(long id);

}
