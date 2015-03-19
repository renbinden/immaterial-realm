package io.github.alyphen.immaterial_realm.common.packet.serverbound.character;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketSaveCharacter extends Packet {

    private int hairId;
    private int faceId;
    private int torsoId;
    private int legsId;
    private int feetId;
    private String name;
    private String gender;
    private String race;
    private String description;
    private boolean newCharacter;

    public PacketSaveCharacter(int hairId, int faceId, int torsoId, int legsId, int feetId, String name, String gender, String race, String description, boolean newCharacter) {
        this.hairId = hairId;
        this.faceId = faceId;
        this.torsoId = torsoId;
        this.legsId = legsId;
        this.feetId = feetId;
        this.name = name;
        this.gender = gender;
        this.race = race;
        this.description = description;
        this.newCharacter = newCharacter;
    }

    public int getHairId() {
        return hairId;
    }

    public int getFaceId() {
        return faceId;
    }

    public int getTorsoId() {
        return torsoId;
    }

    public int getLegsId() {
        return legsId;
    }

    public int getFeetId() {
        return feetId;
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

    public boolean isNewCharacter() {
        return newCharacter;
    }

}
