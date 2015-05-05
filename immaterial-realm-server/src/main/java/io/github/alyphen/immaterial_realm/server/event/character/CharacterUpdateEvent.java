package io.github.alyphen.immaterial_realm.server.event.character;

import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.server.event.Cancellable;

public class CharacterUpdateEvent extends CharacterEvent implements Cancellable {

    private boolean cancelled;
    private int newHairId;
    private int newFaceId;
    private int newTorsoId;
    private int newLegsId;
    private int newFeetId;
    private String newName;
    private String newGender;
    private String newRace;
    private String newDescription;

    public CharacterUpdateEvent(Character character, int newHairId, int newFaceId, int newTorsoId, int newLegsId, int newFeetId, String newName, String newGender, String newRace, String newDescription) {
        super(character);
        this.newHairId = newHairId;
        this.newFaceId = newFaceId;
        this.newTorsoId = newTorsoId;
        this.newLegsId = newLegsId;
        this.newFeetId = newFeetId;
        this.newName = newName;
        this.newGender = newGender;
        this.newRace = newRace;
        this.newDescription = newDescription;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public int getNewHairId() {
        return newHairId;
    }

    public void setNewHairId(int newHairId) {
        this.newHairId = newHairId;
    }

    public int getNewFaceId() {
        return newFaceId;
    }

    public void setNewFaceId(int newFaceId) {
        this.newFaceId = newFaceId;
    }

    public int getNewTorsoId() {
        return newTorsoId;
    }

    public void setNewTorsoId(int newTorsoId) {
        this.newTorsoId = newTorsoId;
    }

    public int getNewLegsId() {
        return newLegsId;
    }

    public void setNewLegsId(int newLegsId) {
        this.newLegsId = newLegsId;
    }

    public int getNewFeetId() {
        return newFeetId;
    }

    public void setNewFeetId(int newFeetId) {
        this.newFeetId = newFeetId;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewGender() {
        return newGender;
    }

    public void setNewGender(String newGender) {
        this.newGender = newGender;
    }

    public String getNewRace() {
        return newRace;
    }

    public void setNewRace(String newRace) {
        this.newRace = newRace;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

}
