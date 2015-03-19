package io.github.alyphen.immaterial_realm.server.character;

import io.github.alyphen.immaterial_realm.common.sprite.Sprite;
import io.github.alyphen.immaterial_realm.common.world.Direction;
import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static io.github.alyphen.immaterial_realm.common.util.MathUtils.max;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.util.logging.Level.SEVERE;

public class CharacterComponentManager {

    private Map<Direction, List<Sprite>> hairSprites;
    private Map<Direction, List<Sprite>> faceSprites;
    private Map<Direction, List<Sprite>> torsoSprites;
    private Map<Direction, List<Sprite>> legsSprites;
    private Map<Direction, List<Sprite>> feetSprites;

    public CharacterComponentManager(ImmaterialRealmServer server) {
        hairSprites = new EnumMap<>(Direction.class);
        faceSprites = new EnumMap<>(Direction.class);
        torsoSprites = new EnumMap<>(Direction.class);
        legsSprites = new EnumMap<>(Direction.class);
        feetSprites = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            hairSprites.put(direction, new ArrayList<>());
            faceSprites.put(direction, new ArrayList<>());
            torsoSprites.put(direction, new ArrayList<>());
            legsSprites.put(direction, new ArrayList<>());
            feetSprites.put(direction, new ArrayList<>());
        }
        try {
            saveDefaultSprites();
        } catch (IOException exception) {
            server.getLogger().log(SEVERE, "Failed to save default sprites", exception);
        }
        try {
            loadSprites();
        } catch (IOException exception) {
            server.getLogger().log(SEVERE, "Failed to load sprites", exception);
        }
    }

    private void saveDefaultSprites() throws IOException {
        File componentSpritesDirectory = new File("./character_components");
        if (componentSpritesDirectory.exists() && !componentSpritesDirectory.isDirectory()) {
            componentSpritesDirectory.delete();
        }
        if (!componentSpritesDirectory.exists()) {
            componentSpritesDirectory.mkdirs();
            saveDefaultHairSprites();
            saveDefaultFaceSprites();
            saveDefaultTorsoSprites();
            saveDefaultLegsSprites();
            saveDefaultFeetSprites();
        }
    }

    private void saveDefaultHairSprites() throws IOException {
        for (Direction direction : Direction.values()) {
            saveDefaultSprites("hair", direction);
        }
    }

    private void saveDefaultFaceSprites() throws IOException {
        for (Direction direction : Direction.values()) {
            saveDefaultSprites("face", direction);
        }
    }

    private void saveDefaultTorsoSprites() throws IOException {
        for (Direction direction : Direction.values()) {
            saveDefaultSprites("torso", direction);
        }
    }

    private void saveDefaultLegsSprites() throws IOException {
        for (Direction direction : Direction.values()) {
            saveDefaultSprites("legs", direction);
        }
    }

    private void saveDefaultFeetSprites() throws IOException {
        for (Direction direction : Direction.values()) {
            saveDefaultSprites("feet", direction);
        }
    }

    private void saveDefaultSprites(String type, Direction direction) throws IOException {
        File componentSpritesDirectory = new File("./character_components");
        File typeDirectory = new File(componentSpritesDirectory, type);
        if (typeDirectory.exists() && !typeDirectory.isDirectory()) {
            typeDirectory.delete();
        }
        if (!typeDirectory.exists()) {
            typeDirectory.mkdirs();
        }
        File directionDirectory = new File(typeDirectory, direction.toString().toLowerCase());
        if (directionDirectory.exists() && !directionDirectory.isDirectory()) {
            directionDirectory.delete();
        }
        if (!directionDirectory.exists()) {
            directionDirectory.mkdirs();
        }
        File defaultSpriteDirectory = new File(directionDirectory, "default");
        defaultSpriteDirectory.mkdirs();
        copy(getClass().getResourceAsStream("/character_components/" + type + "/" + direction.toString().toLowerCase() + "/default/sprite.png"), get(new File(defaultSpriteDirectory, "sprite.png").getPath()));
        copy(getClass().getResourceAsStream("/character_components/" + type + "/" + direction.toString().toLowerCase() + "/default/sprite.json"), get(new File(defaultSpriteDirectory, "sprite.json").getPath()));
    }

    private void loadSprites() throws IOException {
        loadHairSprites();
        loadFaceSprites();
        loadTorsoSprites();
        loadLegsSprites();
        loadFeetSprites();
    }

    private void loadHairSprites() throws IOException {
        File componentSpritesDirectory = new File("./character_components");
        File hairSpritesDirectory = new File(componentSpritesDirectory, "hair");
        for (Direction direction : Direction.values()) {
            File directionDirectory = new File(hairSpritesDirectory, direction.toString().toLowerCase());
            for (File file : directionDirectory.listFiles(File::isDirectory)) {
                addHairSprite(direction, Sprite.load(file));
            }
        }
    }

    private void loadFaceSprites() throws IOException {
        File componentSpritesDirectory = new File("./character_components");
        File faceSpritesDirectory = new File(componentSpritesDirectory, "face");
        for (Direction direction : Direction.values()) {
            File directionDirectory = new File(faceSpritesDirectory, direction.toString().toLowerCase());
            for (File file : directionDirectory.listFiles(File::isDirectory)) {
                addFaceSprite(direction, Sprite.load(file));
            }
        }
    }

    private void loadTorsoSprites() throws IOException {
        File componentSpritesDirectory = new File("./character_components");
        File torsoSpritesDirectory = new File(componentSpritesDirectory, "torso");
        for (Direction direction : Direction.values()) {
            File directionDirectory = new File(torsoSpritesDirectory, direction.toString().toLowerCase());
            for (File file : directionDirectory.listFiles(File::isDirectory)) {
                addTorsoSprite(direction, Sprite.load(file));
            }
        }
    }

    private void loadLegsSprites() throws IOException {
        File componentSpritesDirectory = new File("./character_components");
        File legsSpritesDirectory = new File(componentSpritesDirectory, "legs");
        for (Direction direction : Direction.values()) {
            File directionDirectory = new File(legsSpritesDirectory, direction.toString().toLowerCase());
            for (File file : directionDirectory.listFiles(File::isDirectory)) {
                addLegsSprite(direction, Sprite.load(file));
            }
        }
    }

    private void loadFeetSprites() throws IOException {
        File componentSpritesDirectory = new File("./character_components");
        File feetSpritesDirectory = new File(componentSpritesDirectory, "feet");
        for (Direction direction : Direction.values()) {
            File directionDirectory = new File(feetSpritesDirectory, direction.toString().toLowerCase());
            for (File file : directionDirectory.listFiles(File::isDirectory)) {
                addFeetSprite(direction, Sprite.load(file));
            }
        }
    }

    public Sprite combine(int hairId, int faceId, int torsoId, int legsId, int feetId, Direction direction) {
        Sprite hairSprite = hairId < hairSprites.get(direction).size() ? hairSprites.get(direction).get(hairId) : null;
        Sprite faceSprite = faceId < faceSprites.get(direction).size() ? faceSprites.get(direction).get(faceId) : null;
        Sprite torsoSprite = torsoId < torsoSprites.get(direction).size() ? torsoSprites.get(direction).get(torsoId) : null;
        Sprite legsSprite = legsId < legsSprites.get(direction).size() ? legsSprites.get(direction).get(legsId) : null;
        Sprite feetSprite = feetId < feetSprites.get(direction).size() ? feetSprites.get(direction).get(feetId) : null;
        if (hairSprite == null || faceSprite == null || torsoSprite == null || legsSprite == null || feetSprite == null) return null;
        int maxFrames = max(hairSprite.getFrames().length, faceSprite.getFrames().length, torsoSprite.getFrames().length, legsSprite.getFrames().length, feetSprite.getFrames().length);
        int maxWidth = max(hairSprite.getWidth(), faceSprite.getWidth(), torsoSprite.getWidth(), legsSprite.getWidth(), feetSprite.getWidth());
        int maxHeight = max(hairSprite.getHeight(), faceSprite.getHeight(), torsoSprite.getHeight(), legsSprite.getHeight(), feetSprite.getHeight());
        BufferedImage[] frames = new BufferedImage[maxFrames];
        for (int i = 0; i < maxFrames; i++) {
            BufferedImage frame = new BufferedImage(maxWidth, maxHeight, TYPE_INT_ARGB);
            Graphics graphics = frame.createGraphics();
            graphics.drawImage(hairSprite.getFrames()[i], 0, 0, null);
            graphics.drawImage(legsSprite.getFrames()[i], 0, 0, null);
            graphics.drawImage(torsoSprite.getFrames()[i], 0, 0, null);
            graphics.drawImage(feetSprite.getFrames()[i], 0, 0, null);
            graphics.drawImage(faceSprite.getFrames()[i], 0, 0, null);
            graphics.dispose();
            frames[i] = frame;
        }
        return new Sprite("__character_combination_" + hairId + "_" + faceId + "_" + torsoId + "_" + legsId + "_" + feetId, 25, frames);
    }

    public void addHairSprite(Direction direction, Sprite sprite) {
        hairSprites.get(direction).add(sprite);
    }

    public void addFaceSprite(Direction direction, Sprite sprite) {
        faceSprites.get(direction).add(sprite);
    }

    public void addTorsoSprite(Direction direction, Sprite sprite) {
        torsoSprites.get(direction).add(sprite);
    }

    public void addLegsSprite(Direction direction, Sprite sprite) {
        legsSprites.get(direction).add(sprite);
    }

    public void addFeetSprite(Direction direction, Sprite sprite) {
        feetSprites.get(direction).add(sprite);
    }

    public List<Sprite> getHairSprites(Direction direction) {
        return hairSprites.get(direction);
    }

    public List<Sprite> getFaceSprites(Direction direction) {
        return faceSprites.get(direction);
    }

    public List<Sprite> getTorsoSprites(Direction direction) {
        return torsoSprites.get(direction);
    }

    public List<Sprite> getLegsSprites(Direction direction) {
        return legsSprites.get(direction);
    }

    public List<Sprite> getFeetSprites(Direction direction) {
        return feetSprites.get(direction);
    }

}
