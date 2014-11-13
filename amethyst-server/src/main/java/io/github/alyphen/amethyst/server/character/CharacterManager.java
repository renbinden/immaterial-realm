package io.github.alyphen.amethyst.server.character;

import io.github.alyphen.amethyst.common.character.Character;
import io.github.alyphen.amethyst.common.sprite.Sprite;
import io.github.alyphen.amethyst.common.util.FileUtils;
import io.github.alyphen.amethyst.server.AmethystServer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;

public class CharacterManager {

    private AmethystServer server;

    public CharacterManager(AmethystServer server) {
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
        File walkUpSpriteFile = new File("./characters/" + character.getId() + "/walk_up.png");
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
        File walkDownSpriteFile = new File("./characters/" + character.getId() + "/walk_down.png");
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
        File walkLeftSpriteFile = new File("./characters/" + character.getId() + "/walk_left.png");
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
        File walkRightSpriteFile = new File("./characters/" + character.getId() + "/walk_right.png");
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
            if (defaultSpritesDirectory.mkdirs()) {
                File walkUpSpriteFile = new File("./characters/default/walk_up.png");
                if (!walkUpSpriteFile.exists()) {
                    copy(getClass().getResourceAsStream("/characters/default/walk_up.png"), get(walkUpSpriteFile.getPath()));
                }
                File walkDownSpriteFile = new File("./characters/default/walk_down.png");
                if (!walkDownSpriteFile.exists()) {
                    copy(getClass().getResourceAsStream("/characters/default/walk_down.png"), get(walkDownSpriteFile.getPath()));
                }
                File walkLeftSpriteFile = new File("./characters/default/walk_left.png");
                if (!walkLeftSpriteFile.exists()) {
                    copy(getClass().getResourceAsStream("/characters/default/walk_left.png"), get(walkLeftSpriteFile.getPath()));
                }
                File walkRightSpriteFile = new File("./characters/default/walk_right.png");
                if (!walkRightSpriteFile.exists()) {
                    copy(getClass().getResourceAsStream("/characters/default/walk_right.png"), get(walkRightSpriteFile.getPath()));
                }
            }
        }

    }

    public Sprite getDefaultWalkUpSprite() {
        File walkUpSpriteFile = new File("./characters/default/walk_up.png");
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
        File walkDownSpriteFile = new File("./characters/default/walk_down.png");
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
        File walkLeftSpriteFile = new File("./characters/default/walk_left.png");
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
        File walkRightSpriteFile = new File("./characters/default/walk_right.png");
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

    public void addCharacter(Character character) throws SQLException {
        Connection connection = server.getDatabaseManager().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO characters (player_id, name, gender, race, description, dead, area_name, x, y) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setLong(1, character.getPlayerId());
            statement.setString(2, character.getName());
            statement.setString(3, character.getGender());
            statement.setString(4, character.getRace());
            statement.setString(5, character.getDescription());
            statement.setBoolean(6, character.isDead());
            statement.setString(7, character.getAreaName());
            statement.setInt(8, character.getX());
            statement.setInt(9, character.getY());
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


}
