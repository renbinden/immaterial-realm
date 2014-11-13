package io.github.alyphen.amethyst.common.character;

import io.github.alyphen.amethyst.common.player.Player;
import io.github.alyphen.amethyst.common.packet.character.PacketSendCharacterSprites;
import io.github.alyphen.amethyst.common.sprite.Sprite;
import io.github.alyphen.amethyst.common.world.WorldArea;

import java.io.Serializable;

public class Character implements Serializable {

    private long playerId;
    private long id;
    private String name;
    private String gender;
    private String race;
    private String description;
    private boolean dead;
    private String areaName;
    private int x;
    private int y;

    private transient Sprite walkUpSprite;
    private transient Sprite walkDownSprite;
    private transient Sprite walkLeftSprite;
    private transient Sprite walkRightSprite;

    public Character(long playerId, long id, String name, String gender, String race, String description, boolean dead, String areaName, int x, int y) {
        this.playerId = playerId;
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.race = race;
        this.description = description;
        this.dead = dead;
        this.areaName = areaName;
        this.x = x;
        this.y = y;
    }

    public Character(Player player, long id, String name, String gender, String race, String description, boolean dead, String areaName, int x, int y) {
        this(player.getId(), id, name, gender, race, description, dead, areaName, x, y);
    }

    public Character(long playerId, long id) {
        this(playerId, id, "Unknown", "Unknown", "Unknown", "", false, "spawn", 0, 0);
    }

    public void setPlayer(Player player) {
        setPlayerId(player.getId());
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setArea(WorldArea area) {
        setAreaName(area.getName());
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

    public Sprite getWalkUpSprite() {
        return walkUpSprite;
    }

    public void setWalkUpSprite(Sprite walkUpSprite) {
        this.walkUpSprite = walkUpSprite;
    }

    public Sprite getWalkDownSprite() {
        return walkDownSprite;
    }

    public void setWalkDownSprite(Sprite walkDownSprite) {
        this.walkDownSprite = walkDownSprite;
    }

    public Sprite getWalkLeftSprite() {
        return walkLeftSprite;
    }

    public void setWalkLeftSprite(Sprite walkLeftSprite) {
        this.walkLeftSprite = walkLeftSprite;
    }

    public Sprite getWalkRightSprite() {
        return walkRightSprite;
    }

    public void setWalkRightSprite(Sprite walkRightSprite) {
        this.walkRightSprite = walkRightSprite;
    }

    public void setupSprites(PacketSendCharacterSprites spriteUpdatePacket) {
        setWalkUpSprite(spriteUpdatePacket.getWalkUpSprite());
        setWalkDownSprite(spriteUpdatePacket.getWalkDownSprite());
        setWalkLeftSprite(spriteUpdatePacket.getWalkLeftSprite());
        setWalkRightSprite(spriteUpdatePacket.getWalkRightSprite());
    }

}
