package io.github.alyphen.amethyst.common.entity;

import io.github.alyphen.amethyst.common.character.Character;
import io.github.alyphen.amethyst.common.sprite.Sprite;

import java.awt.*;

public class EntityCharacter extends Entity {
    private io.github.alyphen.amethyst.common.character.Character character;

    public EntityCharacter(long id) {
        super(id, "entity_character");
    }

    @Override
    public void paint(Graphics graphics) {
        Sprite sprite = getSprite();
        if (sprite != null) sprite.paint(graphics);
    }

    @Override
    public Sprite getSprite() {
        if (getCharacter() == null) return null;
        Sprite sprite;
        switch (getDirectionFacing()) {
            case UP:
                sprite = getCharacter().getWalkUpSprite();
                break;
            default: case DOWN:
                sprite = getCharacter().getWalkDownSprite();
                break;
            case LEFT:
                sprite = getCharacter().getWalkLeftSprite();
                break;
            case RIGHT:
                sprite = getCharacter().getWalkRightSprite();
                break;
        }
        return sprite;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(0, 32, 32, 32);
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

}
