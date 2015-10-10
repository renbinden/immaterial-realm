package io.github.alyphen.immaterial_realm.common.packet.clientbound.character;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import java.io.IOException;
import java.util.UUID;

import static java.util.logging.Level.SEVERE;

public class PacketCharacterUpdate extends Packet {

    private UUID characterUUID;
    private UUID playerUUID;
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

    public PacketCharacterUpdate(Character character) throws IOException {
        this.characterUUID = character.getUUID();
        this.playerUUID = character.getPlayerUUID();
        this.name = character.getName();
        this.gender = character.getGender();
        this.race = character.getRace();
        this.description = character.getDescription();
        this.dead = character.isDead();
        this.active = character.isActive();
        this.areaName = character.getAreaName();
        this.x = character.getX();
        this.y = character.getY();
        this.walkUpSprite = character.getWalkUpSprite().toByteArray();
        this.walkUpFrameDelay = character.getWalkUpSprite().getFrameDelay();
        this.walkDownSprite = character.getWalkDownSprite().toByteArray();
        this.walkDownFrameDelay = character.getWalkDownSprite().getFrameDelay();
        this.walkLeftSprite = character.getWalkLeftSprite().toByteArray();
        this.walkLeftFrameDelay = character.getWalkLeftSprite().getFrameDelay();
        this.walkRightSprite = character.getWalkRightSprite().toByteArray();
        this.walkRightFrameDelay = character.getWalkRightSprite().getFrameDelay();
    }

    public UUID getCharacterUUID() {
        return characterUUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
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

    public Sprite getWalkUpSprite(ImmaterialRealm immaterialRealm) {
        try {
            return immaterialRealm.getSpriteManager().createSprite("__character_walk_up_" + name, walkUpSprite, walkUpFrameDelay);
        } catch (IOException exception) {
            immaterialRealm.getLogger().log(SEVERE, "Failed to decode character walk up sprite", exception);
        }
        return null;
    }

    public Sprite getWalkLeftSprite(ImmaterialRealm immaterialRealm) {
        try {
            return immaterialRealm.getSpriteManager().createSprite("__character_walk_left_" + name, walkLeftSprite, walkLeftFrameDelay);
        } catch (IOException exception) {
            immaterialRealm.getLogger().log(SEVERE, "Failed to decode character walk left sprite", exception);
        }
        return null;
    }

    public Sprite getWalkRightSprite(ImmaterialRealm immaterialRealm) {
        try {
            return immaterialRealm.getSpriteManager().createSprite("__character_walk_right_" + name, walkRightSprite, walkRightFrameDelay);
        } catch (IOException exception) {
            immaterialRealm.getLogger().log(SEVERE, "Failed to decode character walk right sprite", exception);
        }
        return null;
    }

    public Sprite getWalkDownSprite(ImmaterialRealm immaterialRealm) {
        try {
            return immaterialRealm.getSpriteManager().createSprite("__character_walk_down_" + name, walkDownSprite, walkDownFrameDelay);
        } catch (IOException exception) {
            immaterialRealm.getLogger().log(SEVERE, "Failed to decode character walk down sprite", exception);
        }
        return null;
    }

}
