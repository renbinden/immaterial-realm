package io.github.lucariatias.amethyst.common.entity;

import io.github.lucariatias.amethyst.common.character.Character;
import io.github.lucariatias.amethyst.common.player.Player;
import io.github.lucariatias.amethyst.common.sprite.Sprite;

import java.awt.*;

public class EntityCharacter extends Entity {

    private Player player;
    private Character character;

    public EntityCharacter(long id, Sprite sprite, Rectangle bounds) {
        super(id, sprite, bounds);
    }

    @Override
    public void paint(Graphics graphics) {
        Sprite sprite = getSprite();
        if (sprite != null) sprite.paint(graphics);
    }

    public Sprite getSprite() {
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    public Object[] getData() {
        return new Object[] {getId()};
    }
}
