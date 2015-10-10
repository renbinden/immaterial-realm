package io.github.alyphen.immaterial_realm.common.database.table;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.database.Table;
import io.github.alyphen.immaterial_realm.common.player.Player;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.logging.Level.SEVERE;

public class CharacterTable extends Table<Character> {

    public CharacterTable(ImmaterialRealm immaterialRealm) throws SQLException {
        super(immaterialRealm, Character.class);
    }

    @Override
    public void create() {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS character (" +
                        "uuid VARCHAR(36) PRIMARY KEY," +
                        "player_uuid VARCHAR(36)," +
                        "name TEXT," +
                        "gender TEXT," +
                        "race TEXT," +
                        "description TEXT," +
                        "dead BOOLEAN," +
                        "active BOOLEAN," +
                        "area_name TEXT," +
                        "x INTEGER," +
                        "y INTEGER," +
                        "walk_up_sprite_uuid INTEGER," +
                        "walk_down_sprite_uuid INTEGER," +
                        "walk_left_sprite_uuid INTEGER," +
                        "walk_right_sprite_uuid INTEGER," +
                        "FOREIGN KEY(walk_up_sprite_uuid) REFERENCES sprite(uuid)," +
                        "FOREIGN KEY(walk_down_sprite_uuid) REFERENCES sprite(uuid)," +
                        "FOREIGN KEY(walk_left_sprite_uuid) REFERENCES sprite(uuid)," +
                        "FOREIGN KEY(walk_right_sprite_uuid) REFERENCES sprite(uuid)" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to create character table", exception);
        }
    }

    @Override
    public void insert(Character character) throws SQLException {
        Table<Sprite> spriteTable = getImmaterialRealm().getDatabase().getTable(Sprite.class);
        if (spriteTable.get(character.getWalkUpSprite().getUUID()) == null)
            spriteTable.insert(character.getWalkUpSprite());
        else
            spriteTable.update(character.getWalkUpSprite());
        if (spriteTable.get(character.getWalkDownSprite().getUUID()) == null)
            spriteTable.insert(character.getWalkDownSprite());
        else
            spriteTable.update(character.getWalkDownSprite());
        if (spriteTable.get(character.getWalkLeftSprite().getUUID()) == null)
            spriteTable.insert(character.getWalkLeftSprite());
        else
            spriteTable.update(character.getWalkLeftSprite());
        if (spriteTable.get(character.getWalkRightSprite().getUUID()) == null)
            spriteTable.insert(character.getWalkRightSprite());
        else
            spriteTable.update(character.getWalkRightSprite());
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO character (uuid, player_uuid, name, gender, race, description, dead, active, area_name, x, y, walk_up_sprite_uuid, walk_down_sprite_uuid, walk_left_sprite_uuid, walk_right_sprite_uuid) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, character.getUUID().toString());
            statement.setString(2, character.getPlayerUUID().toString());
            statement.setString(3, character.getName());
            statement.setString(4, character.getGender());
            statement.setString(5, character.getRace());
            statement.setString(6, character.getDescription());
            statement.setBoolean(7, character.isDead());
            statement.setBoolean(8, character.isActive());
            statement.setString(9, character.getAreaName());
            statement.setInt(10, character.getX());
            statement.setInt(11, character.getY());
            statement.setString(12, character.getWalkUpSprite().getUUID().toString());
            statement.setString(13, character.getWalkDownSprite().getUUID().toString());
            statement.setString(14, character.getWalkLeftSprite().getUUID().toString());
            statement.setString(15, character.getWalkRightSprite().getUUID().toString());
            if (statement.executeUpdate() == 0)
                throw new SQLException("Failed to insert character");
        }
    }

    @Override
    public void update(Character character) throws SQLException {
        Table<Sprite> spriteTable = getImmaterialRealm().getDatabase().getTable(Sprite.class);
        if (spriteTable.get(character.getWalkUpSprite().getUUID()) == null)
            spriteTable.insert(character.getWalkUpSprite());
        else
            spriteTable.update(character.getWalkUpSprite());
        if (spriteTable.get(character.getWalkDownSprite().getUUID()) == null)
            spriteTable.insert(character.getWalkDownSprite());
        else
            spriteTable.update(character.getWalkDownSprite());
        if (spriteTable.get(character.getWalkLeftSprite().getUUID()) == null)
            spriteTable.insert(character.getWalkLeftSprite());
        else
            spriteTable.update(character.getWalkLeftSprite());
        if (spriteTable.get(character.getWalkRightSprite().getUUID()) == null)
            spriteTable.insert(character.getWalkRightSprite());
        else
            spriteTable.update(character.getWalkRightSprite());
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE character SET player_uuid = ?, name = ?, gender = ?, race = ?, description = ?, dead = ?, active = ?, area_name = ?, x = ?, y = ?, walk_up_sprite_uuid = ?, walk_down_sprite_uuid = ?, walk_left_sprite_uuid = ?, walk_right_sprite_uuid = ? WHERE uuid = ?")) {
            statement.setString(1, character.getPlayerUUID().toString());
            statement.setString(2, character.getName());
            statement.setString(3, character.getGender());
            statement.setString(4, character.getRace());
            statement.setString(5, character.getDescription());
            statement.setBoolean(6, character.isDead());
            statement.setBoolean(7, character.isActive());
            statement.setString(8, character.getAreaName());
            statement.setInt(9, character.getX());
            statement.setInt(10, character.getY());
            statement.setString(11, character.getWalkUpSprite().getUUID().toString());
            statement.setString(12, character.getWalkDownSprite().getUUID().toString());
            statement.setString(13, character.getWalkLeftSprite().getUUID().toString());
            statement.setString(14, character.getWalkRightSprite().getUUID().toString());
            statement.setString(15, character.getUUID().toString());
            statement.executeUpdate();
        }
    }

    @Override
    public Character get(UUID uuid) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM character WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Character(
                        UUID.fromString(resultSet.getString("uuid")), UUID.fromString(resultSet.getString("player_uuid")),
                        resultSet.getString("name"),
                        resultSet.getString("gender"),
                        resultSet.getString("race"),
                        resultSet.getString("description"),
                        resultSet.getBoolean("dead"),
                        resultSet.getBoolean("active"),
                        resultSet.getString("area_name"),
                        resultSet.getInt("x"),
                        resultSet.getInt("y"),
                        getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_up_sprite_uuid"))),
                        getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_down_sprite_uuid"))),
                        getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_left_sprite_uuid"))),
                        getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_right_sprite_uuid")))
                );
            }
        }
        return null;
    }

    public Set<Character> getCharacters(Player player) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM character WHERE player_uuid = ?")) {
            statement.setString(1, player.getUUID().toString());
            ResultSet resultSet = statement.executeQuery();
            Set<Character> characters = new HashSet<>();
            while (resultSet.next()) {
                characters.add(
                        new Character(
                                UUID.fromString(resultSet.getString("uuid")), UUID.fromString(resultSet.getString("player_uuid")),
                                resultSet.getString("name"),
                                resultSet.getString("gender"),
                                resultSet.getString("race"),
                                resultSet.getString("description"),
                                resultSet.getBoolean("dead"),
                                resultSet.getBoolean("active"),
                                resultSet.getString("area_name"),
                                resultSet.getInt("x"),
                                resultSet.getInt("y"),
                                getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_up_sprite_uuid"))),
                                getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_down_sprite_uuid"))),
                                getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_left_sprite_uuid"))),
                                getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_right_sprite_uuid")))
                        )
                );
            }
            return characters;
        }
    }

    public Character getActiveCharacter(Player player) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM character WHERE player_uuid = ?")) {
            statement.setString(1, player.getUUID().toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getBoolean("active")) {
                    return new Character(
                            UUID.fromString(resultSet.getString("uuid")), UUID.fromString(resultSet.getString("player_uuid")),
                            resultSet.getString("name"),
                            resultSet.getString("gender"),
                            resultSet.getString("race"),
                            resultSet.getString("description"),
                            resultSet.getBoolean("dead"),
                            resultSet.getBoolean("active"),
                            resultSet.getString("area_name"),
                            resultSet.getInt("x"),
                            resultSet.getInt("y"),
                            getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_up_sprite_uuid"))),
                            getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_down_sprite_uuid"))),
                            getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_left_sprite_uuid"))),
                            getImmaterialRealm().getDatabase().getTable(Sprite.class).get(UUID.fromString(resultSet.getString("walk_right_sprite_uuid")))
                    );
                }
            }
        }
        return null;
    }

}
