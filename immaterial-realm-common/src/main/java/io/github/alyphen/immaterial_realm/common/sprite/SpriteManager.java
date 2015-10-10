package io.github.alyphen.immaterial_realm.common.sprite;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.util.FileUtils;
import io.github.alyphen.immaterial_realm.common.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SpriteManager {

    private ImmaterialRealm immaterialRealm;

    private Map<String, Sprite> sprites;

    public SpriteManager(ImmaterialRealm immaterialRealm) {
        this.immaterialRealm = immaterialRealm;
        sprites = new HashMap<>();
    }

    public void loadSprites() throws IOException {
        File spritesDirectory = new File("./sprites");
        File[] fileList = spritesDirectory.listFiles();
        if (fileList != null) {
            for (File directory : fileList) {
                sprites.put(directory.getName(), loadSprite(directory));
            }
        }
    }

    public Sprite getSprite(String name) {
        return sprites.get(name);
    }

    public Collection<Sprite> getSprites() {
        return sprites.values();
    }

    public void addSprite(Sprite sprite) {
        sprites.put(sprite.getName(), sprite);
    }

    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite.getName());
    }

    public void updateSprite(Sprite sprite) {
        Iterator<Map.Entry<String, Sprite>> iterator = sprites.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue() == sprite) {
                iterator.remove();
                break;
            }
        }
        sprites.put(sprite.getName(), sprite);
    }

    public Sprite createSprite(String name, byte[][] byteArray, int frameDelay) throws IOException {
        BufferedImage[] frames = new BufferedImage[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            frames[i] = ImageUtils.fromByteArray(byteArray[i]);
        }
        return new Sprite(immaterialRealm, name, frameDelay, frames);
    }

    public Sprite createSprite(String name, BufferedImage image, int frameDelay, int width, int height) {
        BufferedImage[] frames = new BufferedImage[(int) Math.ceil((double) image.getWidth() / (double) width)];
        for (int i = 0; i < image.getWidth(); i += width) {
            frames[i / width] = image.getSubimage(i, 0, Math.min(width, image.getWidth() - i), height);
        }
        return new Sprite(immaterialRealm, name, frameDelay, frames);
    }

    public Sprite loadSprite(File directory) throws IOException {
        File metadataFile = new File(directory, "sprite.json");
        Map<String, Object> metadata = FileUtils.loadMetadata(metadataFile);
        File imageFile = new File(directory, "sprite.png");
        return createSprite((String) metadata.get("name"), ImageIO.read(imageFile), (int) ((double) metadata.get("frame_delay")), (int) ((double) metadata.get("width")), (int) ((double) metadata.get("height")));
    }
    
}
