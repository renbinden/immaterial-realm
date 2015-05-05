package io.github.alyphen.immaterial_realm.server.event.character;

import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.server.event.Event;

public class CharacterEvent extends Event {

    private Character character;

    public CharacterEvent(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

}
