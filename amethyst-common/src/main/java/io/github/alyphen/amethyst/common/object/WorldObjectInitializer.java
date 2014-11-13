package io.github.alyphen.amethyst.common.object;

import io.github.alyphen.amethyst.common.sprite.Sprite;

import java.awt.*;

public interface WorldObjectInitializer {

    public String getObjectName();

    public Sprite getObjectSprite();

    public Rectangle getObjectBounds();

    public WorldObject initialize(long id);

}
