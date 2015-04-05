package io.github.alyphen.immaterial_realm.common.character;

import io.github.alyphen.immaterial_realm.common.database.TableRow;
import io.github.alyphen.immaterial_realm.common.player.Player;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;

import java.io.Serializable;

public class Character extends TableRow implements Serializable {

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

    private transient Sprite walkUpSprite;
    private transient Sprite walkDownSprite;
    private transient Sprite walkLeftSprite;
    private transient Sprite walkRightSprite;

    public Character(Player player, long id, String name, String gender, String race, String description, boolean dead, boolean active, String areaName, int x, int y, Sprite walkUpSprite, Sprite walkDownSprite, Sprite walkLeftSprite, Sprite walkRightSprite) {
        this(player.getId(), id, name, gender, race, description, dead, active, areaName, x, y, walkUpSprite, walkDownSprite, walkLeftSprite, walkRightSprite);
    }

    public Character(long playerId, long id, Sprite walkUpSprite, Sprite walkDownSprite, Sprite walkLeftSprite, Sprite walkRightSprite) {
        this(playerId, id, "Unknown", "Unknown", "Unknown", "", false, true, "default", 0, 0, walkUpSprite, walkDownSprite, walkLeftSprite, walkRightSprite);
    }

    public Character(long playerId, long id, String name, String gender, String race, String description, boolean dead, boolean active, String areaName, int x, int y, Sprite walkUpSprite, Sprite walkDownSprite, Sprite walkLeftSprite, Sprite walkRightSprite) {
        super(id);
        this.playerId = playerId;
        this.name = name;
        this.gender = gender;
        this.race = race;
        this.description = description;
        this.dead = dead;
        this.active = active;
        this.areaName = areaName;
        this.x = x;
        this.y = y;
        this.walkUpSprite = walkUpSprite;
        this.walkDownSprite = walkDownSprite;
        this.walkLeftSprite = walkLeftSprite;
        this.walkRightSprite = walkRightSprite;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

}
