package io.github.alyphen.immaterial_realm.server.character;

import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.database.table.CharacterTable;
import io.github.alyphen.immaterial_realm.common.player.Player;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;
import io.github.alyphen.immaterial_realm.common.util.FileUtils;
import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.util.logging.Level.SEVERE;

public class CharacterManager {

    private ImmaterialRealmServer server;
    private Sprite defaultWalkUpSprite;
    private Sprite defaultWalkDownSprite;
    private Sprite defaultWalkLeftSprite;
    private Sprite defaultWalkRightSprite;

    public CharacterManager(ImmaterialRealmServer server) {
        this.server = server;
        try {
            extractDefaultSprites();
        } catch (IOException exception) {
            server.getLogger().log(SEVERE, "Failed to extract default sprites", exception);
        }
        try {
            loadDefaultSprites();
        } catch (IOException exception) {
            server.getLogger().log(SEVERE, "Failed to load default sprites");
        }
    }

    private void extractDefaultSprites() throws IOException {
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

    private void loadDefaultSprites() throws IOException {
        File walkUpSpriteFile = new File("./characters/default/walk_up");
        if (walkUpSpriteFile.exists()) {
            try {
                defaultWalkUpSprite = server.getImmaterialRealm().getSpriteManager().loadSprite(walkUpSpriteFile);
            } catch (IOException exception) {
                server.getLogger().log(SEVERE, "Failed to load default walk up sprite", exception);
            }
        }
        File walkDownSpriteFile = new File("./characters/default/walk_down");
        if (walkDownSpriteFile.exists()) {
            try {
                defaultWalkDownSprite = server.getImmaterialRealm().getSpriteManager().loadSprite(walkDownSpriteFile);
            } catch (IOException exception) {
                server.getLogger().log(SEVERE, "Failed to load default walk down sprite", exception);
            }
        }
        File walkLeftSpriteFile = new File("./characters/default/walk_left");
        if (walkLeftSpriteFile.exists()) {
            try {
                defaultWalkLeftSprite = server.getImmaterialRealm().getSpriteManager().loadSprite(walkLeftSpriteFile);
            } catch (IOException exception) {
                server.getLogger().log(SEVERE, "Failed to load default walk left sprite", exception);
            }
        }
        File walkRightSpriteFile = new File("./characters/default/walk_right");
        if (walkRightSpriteFile.exists()) {
            try {
                defaultWalkRightSprite = server.getImmaterialRealm().getSpriteManager().loadSprite(walkRightSpriteFile);
            } catch (IOException exception) {
                server.getLogger().log(SEVERE, "Failed to load default walk right sprite", exception);
            }
        }
    }

    public Character getCharacter(UUID uuid) throws SQLException {
        return server.getDatabaseManager().getDatabase().getTable(Character.class).get(uuid);
    }

    public Character getCharacter(Player player) throws SQLException {
        return ((CharacterTable) server.getDatabaseManager().getDatabase().getTable(Character.class)).getActiveCharacter(player);
    }

    public Set<Character> getCharacters(Player player) throws SQLException {
        return ((CharacterTable) server.getDatabaseManager().getDatabase().getTable(Character.class)).getCharacters(player);
    }

    public void addCharacter(Character character) throws SQLException {
        server.getDatabaseManager().getDatabase().getTable(Character.class).insert(character);
    }

    public void updateCharacter(Character character) throws SQLException {
        server.getDatabaseManager().getDatabase().getTable(Character.class).update(character);
    }


    public Sprite getDefaultWalkUpSprite() {
        return defaultWalkUpSprite;
    }


    public Sprite getDefaultWalkDownSprite() {
        return defaultWalkDownSprite;
    }

    public Sprite getDefaultWalkLeftSprite() {
        return defaultWalkLeftSprite;
    }

    public Sprite getDefaultWalkRightSprite() {
        return defaultWalkRightSprite;
    }
}
