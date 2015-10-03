package io.github.alyphen.immaterial_realm.common.database.table;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.database.Database;
import io.github.alyphen.immaterial_realm.common.database.Table;
import io.github.alyphen.immaterial_realm.common.player.Player;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.logging.Level.SEVERE;

public class CharacterTable extends Table<Character> {

    public CharacterTable(Database database) throws SQLException {
        super(database, Character.class);
    }

    @Override
    public void create() {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS character (" +
                        "player_id INTEGER," +
                        "id INTEGER PRIMARY KEY," +
                        "name TEXT," +
                        "gender TEXT," +
                        "race TEXT," +
                        "description TEXT," +
                        "dead BOOLEAN," +
                        "active BOOLEAN," +
                        "area_name TEXT," +
                        "x INTEGER," +
                        "y INTEGER," +
                        "walk_up_sprite_id INTEGER," +
                        "walk_down_sprite_id INTEGER," +
                        "walk_left_sprite_id INTEGER," +
                        "walk_right_sprite_id INTEGER," +
                        "FOREIGN KEY(walk_up_sprite_id) REFERENCES sprite(id)," +
                        "FOREIGN KEY(walk_down_sprite_id) REFERENCES sprite(id)," +
                        "FOREIGN KEY(walk_left_sprite_id) REFERENCES sprite(id)," +
                        "FOREIGN KEY(walk_right_sprite_id) REFERENCES sprite(id)" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to create character table", exception);
        }
    }

    @Override
    public long insert(Character character) throws SQLException {
        Table<Sprite> spriteTable = getDatabase().getTable(Sprite.class);
        if (spriteTable.get(character.getWalkUpSprite().getId()) == null)
            spriteTable.insert(character.getWalkUpSprite());
        else
            spriteTable.update(character.getWalkUpSprite());
        if (spriteTable.get(character.getWalkDownSprite().getId()) == null)
            spriteTable.insert(character.getWalkDownSprite());
        else
            spriteTable.update(character.getWalkDownSprite());
        if (spriteTable.get(character.getWalkLeftSprite().getId()) == null)
            spriteTable.insert(character.getWalkLeftSprite());
        else
            spriteTable.update(character.getWalkLeftSprite());
        if (spriteTable.get(character.getWalkRightSprite().getId()) == null)
            spriteTable.insert(character.getWalkRightSprite());
        else
            spriteTable.update(character.getWalkRightSprite());
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO character (player_id, name, gender, race, description, dead, active, area_name, x, y, walk_up_sprite_id, walk_down_sprite_id, walk_left_sprite_id, walk_right_sprite_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                RETURN_GENERATED_KEYS
        )) {
            statement.setLong(1, character.getPlayerId());
            statement.setString(2, character.getName());
            statement.setString(3, character.getGender());
            statement.setString(4, character.getRace());
            statement.setString(5, character.getDescription());
            statement.setBoolean(6, character.isDead());
            statement.setBoolean(7, character.isActive());
            statement.setString(8, character.getAreaName());
            statement.setInt(9, character.getX());
            statement.setInt(10, character.getY());
            statement.setLong(11, character.getWalkUpSprite().getId());
            statement.setLong(12, character.getWalkDownSprite().getId());
            statement.setLong(13, character.getWalkLeftSprite().getId());
            statement.setLong(14, character.getWalkRightSprite().getId());
            if (statement.executeUpdate() == 0) throw new SQLException("Failed to insert character");
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                character.setId(id);
                return id;
            }
        }
        throw new SQLException("Failed to insert character");
    }

    @Override
    public long update(Character character) throws SQLException {
        Table<Sprite> spriteTable = getDatabase().getTable(Sprite.class);
        if (spriteTable.get(character.getWalkUpSprite().getId()) == null)
            spriteTable.insert(character.getWalkUpSprite());
        else
            spriteTable.update(character.getWalkUpSprite());
        if (spriteTable.get(character.getWalkDownSprite().getId()) == null)
            spriteTable.insert(character.getWalkDownSprite());
        else
            spriteTable.update(character.getWalkDownSprite());
        if (spriteTable.get(character.getWalkLeftSprite().getId()) == null)
            spriteTable.insert(character.getWalkLeftSprite());
        else
            spriteTable.update(character.getWalkLeftSprite());
        if (spriteTable.get(character.getWalkRightSprite().getId()) == null)
            spriteTable.insert(character.getWalkRightSprite());
        else
            spriteTable.update(character.getWalkRightSprite());
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE character SET player_id = ?, name = ?, gender = ?, race = ?, description = ?, dead = ?, active = ?, area_name = ?, x = ?, y = ?, walk_up_sprite_id = ?, walk_down_sprite_id = ?, walk_left_sprite_id = ?, walk_right_sprite_id = ? WHERE id = ?")) {
            statement.setLong(1, character.getPlayerId());
            statement.setString(2, character.getName());
            statement.setString(3, character.getGender());
            statement.setString(4, character.getRace());
            statement.setString(5, character.getDescription());
            statement.setBoolean(6, character.isDead());
            statement.setBoolean(7, character.isActive());
            statement.setString(8, character.getAreaName());
            statement.setInt(9, character.getX());
            statement.setInt(10, character.getY());
            statement.setLong(11, character.getWalkUpSprite().getId());
            statement.setLong(12, character.getWalkDownSprite().getId());
            statement.setLong(13, character.getWalkLeftSprite().getId());
            statement.setLong(14, character.getWalkRightSprite().getId());
            statement.setLong(15, character.getId());
            statement.executeUpdate();
            return character.getId();
        }
    }

    @Override
    public Character get(long id) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM character WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Character(
                        resultSet.getLong("player_id"),
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("gender"),
                        resultSet.getString("race"),
                        resultSet.getString("description"),
                        resultSet.getBoolean("dead"),
                        resultSet.getBoolean("active"),
                        resultSet.getString("area_name"),
                        resultSet.getInt("x"),
                        resultSet.getInt("y"),
                        getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_up_sprite_id")),
                        getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_down_sprite_id")),
                        getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_left_sprite_id")),
                        getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_right_sprite_id"))
                );
            }
        }
        return null;
    }

    public Set<Character> getCharacters(Player player) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM character WHERE player_id = ?")) {
            statement.setLong(1, player.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<Character> characters = new HashSet<>();
            while (resultSet.next()) {
                characters.add(
                        new Character(
                                resultSet.getLong("player_id"),
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("gender"),
                                resultSet.getString("race"),
                                resultSet.getString("description"),
                                resultSet.getBoolean("dead"),
                                resultSet.getBoolean("active"),
                                resultSet.getString("area_name"),
                                resultSet.getInt("x"),
                                resultSet.getInt("y"),
                                getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_up_sprite_id")),
                                getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_down_sprite_id")),
                                getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_left_sprite_id")),
                                getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_right_sprite_id"))
                        )
                );
            }
            return characters;
        }
    }

    public Character getActiveCharacter(Player player) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM character WHERE player_id = ?")) {
            statement.setLong(1, player.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getBoolean("active")) {
                    return new Character(
                            resultSet.getLong("player_id"),
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("gender"),
                            resultSet.getString("race"),
                            resultSet.getString("description"),
                            resultSet.getBoolean("dead"),
                            resultSet.getBoolean("active"),
                            resultSet.getString("area_name"),
                            resultSet.getInt("x"),
                            resultSet.getInt("y"),
                            getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_up_sprite_id")),
                            getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_down_sprite_id")),
                            getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_left_sprite_id")),
                            getDatabase().getTable(Sprite.class).get(resultSet.getLong("walk_right_sprite_id"))
                    );
                }
            }
        }
        return null;
    }

}
