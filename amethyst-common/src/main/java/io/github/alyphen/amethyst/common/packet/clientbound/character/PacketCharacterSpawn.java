package io.github.alyphen.amethyst.common.packet.clientbound.character;

import io.github.alyphen.amethyst.common.character.Character;
import io.github.alyphen.amethyst.common.packet.Packet;
import io.github.alyphen.amethyst.common.sprite.Sprite;

import java.io.IOException;

public class PacketCharacterSpawn extends Packet {

    private long id;
    private long playerId;
    private String name;
    private String gender;
    private String race;
    private String description;
    private boolean dead;
    private boolean active;
    private String areaName;
    private int x;
    private int y;
    private byte[][] walkUpSprite;
    private int walkUpFrameDelay;
    private byte[][] walkLeftSprite;
    private int walkLeftFrameDelay;
    private byte[][] walkRightSprite;
    private int walkRightFrameDelay;
    private byte[][] walkDownSprite;
    private int walkDownFrameDelay;

    public PacketCharacterSpawn(Character character, Sprite walkUpSprite, Sprite walkDownSprite, Sprite walkLeftSprite, Sprite walkRightSprite) throws IOException {
        this.id = character.getId();
        this.playerId = character.getPlayerId();
        this.name = character.getName();
        this.gender = character.getGender();
        this.race = character.getRace();
        this.description = character.getDescription();
        this.dead = character.isDead();
        this.active = character.isActive();
        this.areaName = character.getAreaName();
        this.x = character.getX();
        this.y = character.getY();
        this.walkUpSprite = walkUpSprite.toByteArray();
        this.walkUpFrameDelay = walkUpSprite.getFrameDelay();
        this.walkDownSprite = walkDownSprite.toByteArray();
        this.walkDownFrameDelay = walkDownSprite.getFrameDelay();
        this.walkLeftSprite = walkLeftSprite.toByteArray();
        this.walkLeftFrameDelay = walkLeftSprite.getFrameDelay();
        this.walkRightSprite = walkRightSprite.toByteArray();
        this.walkRightFrameDelay = walkRightSprite.getFrameDelay();
    }

    public long getId() {
        return id;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getRace() {
        return race;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isActive() {
        return active;
    }

    public String getAreaName() {
        return areaName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Sprite getWalkUpSprite() {
        try {
            return Sprite.fromByteArray(walkUpSprite, walkUpFrameDelay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Sprite getWalkLeftSprite() {
        try {
            return Sprite.fromByteArray(walkLeftSprite, walkLeftFrameDelay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Sprite getWalkRightSprite() {
        try {
            return Sprite.fromByteArray(walkRightSprite, walkRightFrameDelay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Sprite getWalkDownSprite() {
        try {
            return Sprite.fromByteArray(walkDownSprite, walkDownFrameDelay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
