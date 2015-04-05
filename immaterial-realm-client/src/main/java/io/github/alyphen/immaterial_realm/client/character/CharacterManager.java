package io.github.alyphen.immaterial_realm.client.character;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;
import io.github.alyphen.immaterial_realm.common.character.Character;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CharacterManager {

    private ImmaterialRealmClient client;
    private Map<Long, Character> characters = new HashMap<>();

    public CharacterManager(ImmaterialRealmClient client) {
        this.client = client;
    }

    public Character getCharacter(long id) throws SQLException {
        Character character = characters.get(id);
        if (character != null) {
            return character;
        } else {
            character = client.getDatabaseManager().getDatabase().getTable(Character.class).get(id);
            characters.put(id, character);
            return character;
        }
    }

    public void addCharacter(Character character) throws SQLException {
        client.getDatabaseManager().getDatabase().getTable(Character.class).insert(character);
    }

    public void updateCharacter(Character character) throws SQLException {
        client.getDatabaseManager().getDatabase().getTable(Character.class).update(character);
    }
}
