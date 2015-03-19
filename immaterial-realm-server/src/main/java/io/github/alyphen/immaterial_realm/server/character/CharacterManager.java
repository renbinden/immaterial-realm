package io.github.alyphen.immaterial_realm.server.character;

import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.player.Player;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;
import io.github.alyphen.immaterial_realm.common.util.FileUtils;
import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;

public class CharacterManager {

    private ImmaterialRealmServer server;

    public CharacterManager(ImmaterialRealmServer server) {
        this.server = server;
        try {
            createCharactersTable();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        try {
            createDefaultSprites();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void createCharactersTable() throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS characters (" +
                        "player_id INTEGER," +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "gender TEXT," +
                        "race TEXT," +
                        "description TEXT," +
                        "dead BOOLEAN," +
                        "active BOOLEAN," +
                        "area_name TEXT," +
                        "x INTEGER," +
                        "y INTEGER" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Sprite getWalkUpSprite(Character character) {
        File walkUpSpriteFile = new File("./characters/" + character.getId() + "/walk_up");
        if (walkUpSpriteFile.exists()) {
            try {
                return Sprite.load(walkUpSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            return getDefaultWalkUpSprite();
        }
        return null;
    }

    public Sprite getWalkDownSprite(Character character) {
        File walkDownSpriteFile = new File("./characters/" + character.getId() + "/walk_down");
        if (walkDownSpriteFile.exists()) {
            try {
                return Sprite.load(walkDownSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            return getDefaultWalkDownSprite();
        }
        return null;
    }

    public Sprite getWalkLeftSprite(Character character) {
        File walkLeftSpriteFile = new File("./characters/" + character.getId() + "/walk_left");
        if (walkLeftSpriteFile.exists()) {
            try {
                return Sprite.load(walkLeftSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            return getDefaultWalkLeftSprite();
        }
        return null;
    }

    public Sprite getWalkRightSprite(Character character) {
        File walkRightSpriteFile = new File("./characters/" + character.getId() + "/walk_right");
        if (walkRightSpriteFile.exists()) {
            try {
                return Sprite.load(walkRightSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            return getDefaultWalkRightSprite();
        }
        return null;
    }

    public void createDefaultSprites() throws IOException {
        File defaultSpritesDirectory = new File("./characters/default");
        if (!defaultSpritesDirectory.isDirectory()) FileUtils.deleteDirectory(defaultSpritesDirectory);
        if (!defaultSpritesDirectory.exists()) {
            defaultSpritesDirectory.mkdirs();
            File walkUpSpriteFile = new File("./characters/default/walk_up");
            if (!walkUpSpriteFile.exists()) {
                walkUpSpriteFile.mkdirs();
                copy(getClass().getResourceAsStream("/characters/default/walk_up/sprite.json"), get(new File(walkUpSpriteFile, "sprite.json").getPath()));
                copy(getClass().getResourceAsStream("/characters/default/walk_up/sprite.png"), get(new File(walkUpSpriteFile, "sprite.png").getPath()));
            }
            File walkDownSpriteFile = new File("./characters/default/walk_down");
            if (!walkDownSpriteFile.exists()) {
                walkDownSpriteFile.mkdirs();
                copy(getClass().getResourceAsStream("/characters/default/walk_down/sprite.json"), get(new File(walkDownSpriteFile, "sprite.json").getPath()));
                copy(getClass().getResourceAsStream("/characters/default/walk_down/sprite.png"), get(new File(walkDownSpriteFile, "sprite.png").getPath()));
            }
            File walkLeftSpriteFile = new File("./characters/default/walk_left");
            if (!walkLeftSpriteFile.exists()) {
                walkLeftSpriteFile.mkdirs();
                copy(getClass().getResourceAsStream("/characters/default/walk_left/sprite.json"), get(new File(walkLeftSpriteFile, "sprite.json").getPath()));
                copy(getClass().getResourceAsStream("/characters/default/walk_left/sprite.png"), get(new File(walkLeftSpriteFile, "sprite.png").getPath()));
            }
            File walkRightSpriteFile = new File("./characters/default/walk_right");
            if (!walkRightSpriteFile.exists()) {
                walkRightSpriteFile.mkdirs();
                copy(getClass().getResourceAsStream("/characters/default/walk_right/sprite.json"), get(new File(walkRightSpriteFile, "sprite.json").getPath()));
                copy(getClass().getResourceAsStream("/characters/default/walk_right/sprite.png"), get(new File(walkRightSpriteFile, "sprite.png").getPath()));
            }
        }

    }

    public Sprite getDefaultWalkUpSprite() {
        File walkUpSpriteFile = new File("./characters/default/walk_up");
        if (walkUpSpriteFile.exists()) {
            try {
                return Sprite.load(walkUpSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public Sprite getDefaultWalkDownSprite() {
        File walkDownSpriteFile = new File("./characters/default/walk_down");
        if (walkDownSpriteFile.exists()) {
            try {
                return Sprite.load(walkDownSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public Sprite getDefaultWalkLeftSprite() {
        File walkLeftSpriteFile = new File("./characters/default/walk_left");
        if (walkLeftSpriteFile.exists()) {
            try {
                return Sprite.load(walkLeftSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public Sprite getDefaultWalkRightSprite() {
        File walkRightSpriteFile = new File("./characters/default/walk_right");
        if (walkRightSpriteFile.exists()) {
            try {
                return Sprite.load(walkRightSpriteFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public Character getCharacter(long id) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM characters WHERE id = ?")) {
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
                        resultSet.getInt("y")
                );
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Character getCharacter(Player player) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM characters WHERE player_id = ?")) {
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
                            resultSet.getInt("y")
                    );
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Set<Character> getCharacters(Player player) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM characters WHERE player_id = ?")) {
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
                        resultSet.getInt("y")
                    )
                );
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void addCharacter(Character character) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO characters (player_id, name, gender, race, description, dead, active, area_name, x, y) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
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
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void updateCharacter(Character character) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE characters SET player_id = ?, name = ?, gender = ?, race = ?, description = ?, dead = ?, active = ?, area_name = ?, x = ?, y = ? WHERE id = ?")) {
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
            statement.setLong(11, character.getId());
            statement.executeUpdate();
        }
    }


}
