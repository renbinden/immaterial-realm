package io.github.alyphen.amethyst.common.object;

import io.github.alyphen.amethyst.common.sprite.Sprite;

import java.awt.*;

public class WorldObject {

    private long id;
    private String type;
    private int x;
    private int y;
    private Sprite sprite;
    private Rectangle bounds;

    public WorldObject(long id, String type, Sprite sprite, Rectangle bounds) {
        this.id = id;
        this.type = type;
        this.sprite = sprite;
        this.bounds = bounds;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void onTick() {
        getSprite().onTick();
    }

    public void onInteract() {}

    public void paint(Graphics graphics) {
        getSprite().paint(graphics);
    }

    public Rectangle getBounds() {
        Rectangle relativeBounds = new Rectangle(bounds);
        relativeBounds.translate(getX(), getY());
        return relativeBounds;
    }

}
