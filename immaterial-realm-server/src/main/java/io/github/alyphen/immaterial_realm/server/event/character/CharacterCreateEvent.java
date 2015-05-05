package io.github.alyphen.immaterial_realm.server.event.character;

import io.github.alyphen.immaterial_realm.server.event.Event;

public class CharacterCreateEvent extends Event {

    private int hairId;
    private int faceId;
    private int torsoId;
    private int legsId;
    private int feetId;
    private String name;
    private String gender;
    private String race;
    private String description;

    public CharacterCreateEvent(int hairId, int faceId, int torsoId, int legsId, int feetId, String name, String gender, String race, String description) {
        this.hairId = hairId;
        this.faceId = faceId;
        this.torsoId = torsoId;
        this.legsId = legsId;
        this.feetId = feetId;
        this.name = name;
        this.gender = gender;
        this.race = race;
        this.description = description;
    }

    public int getHairId() {
        return hairId;
    }

    public void setHairId(int hairId) {
        this.hairId = hairId;
    }

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public int getTorsoId() {
        return torsoId;
    }

    public void setTorsoId(int torsoId) {
        this.torsoId = torsoId;
    }

    public int getLegsId() {
        return legsId;
    }

    public void setLegsId(int legsId) {
        this.legsId = legsId;
    }

    public int getFeetId() {
        return feetId;
    }

    public void setFeetId(int feetId) {
        this.feetId = feetId;
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

}
