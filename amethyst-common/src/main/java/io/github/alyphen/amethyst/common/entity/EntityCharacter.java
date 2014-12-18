package io.github.alyphen.amethyst.common.entity;

import io.github.alyphen.amethyst.common.character.Character;
import io.github.alyphen.amethyst.common.sprite.Sprite;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class EntityCharacter extends Entity {

    private static final long CHAT_MESSAGE_EXPIRY = 5000L;

    private Character character;
    private String lastChatMessage;
    private long lastChatMessageTime;

    public EntityCharacter(long id) {
        super(id);
    }

    @Override
    public void paint(Graphics graphics) {
        Sprite sprite = getSprite();
        if (sprite != null) sprite.paint(graphics);
        if (getLastChatMessage() != null && lastChatMessageTime + CHAT_MESSAGE_EXPIRY > System.currentTimeMillis()) {
            graphics.setColor(new Color(64, 64, 64, 128));
            int lastMessageWidth = graphics.getFontMetrics().stringWidth(getLastChatMessage());
            int width = (int) getBounds().getWidth();
            graphics.fillRoundRect(((width - lastMessageWidth) / 2) - 8, -40, lastMessageWidth + 16, 32, 8, 8);
            GeneralPath triangle = new GeneralPath();
            triangle.moveTo((width / 2), 0);
            triangle.lineTo(width / 4, -8);
            triangle.lineTo(3 * (width / 4), -8);
            triangle.closePath();
            ((Graphics2D) graphics).fill(triangle);
            graphics.setColor(Color.WHITE);
            graphics.drawString(getLastChatMessage(), ((width - lastMessageWidth) / 2), graphics.getFontMetrics().getHeight() - 40);
        }
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
        return new Rectangle(getX(), getY() + getSprite().getHeight() / 2, getSprite().getWidth(), getSprite().getHeight() / 2);
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public String getLastChatMessage() {
        return lastChatMessage;
    }

    public void setLastChatMessage(String lastChatMessage) {
        this.lastChatMessage = lastChatMessage;
        lastChatMessageTime = System.currentTimeMillis();
    }

}
