package io.github.alyphen.immaterial_realm.common.sprite;

import io.github.alyphen.immaterial_realm.common.util.FileUtils;
import io.github.alyphen.immaterial_realm.common.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static io.github.alyphen.immaterial_realm.common.util.FileUtils.saveMetadata;

public class Sprite {

    private static Map<String, Sprite> sprites;

    static {
        sprites = new HashMap<>();
        try {
            loadSprites();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void loadSprites() throws IOException {
        File spritesDirectory = new File("./sprites");
        File[] fileList = spritesDirectory.listFiles();
        if (fileList != null) {
            for (File directory : fileList) {
                sprites.put(directory.getName(), Sprite.load(directory));
            }
        }
    }

    public static Sprite getSprite(String name) {
        return sprites.get(name);
    }

    public static Collection<Sprite> getSprites() {
        return sprites.values();
    }

    private String name;
    private BufferedImage[] frames;
    private int index;
    private int frameDelay;
    private int tick;

    public Sprite(String name, int frameDelay, BufferedImage... frames) {
        this.name = name;
        this.frameDelay = frameDelay;
        this.frames = frames;
        sprites.put(name, this);
    }

    public void onTick() {
        tick = tick == frameDelay - 1 ? 0 : tick + 1;
        if (tick == 0) index = index == frames.length - 1 ? 0 : index + 1;
    }

    public void paint(Graphics graphics) {
        graphics.drawImage(frames[index], 0, 0, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        sprites.remove(this.name);
        this.name = name;
        sprites.put(name, this);
    }

    public BufferedImage[] getFrames() {
        return frames;
    }

    public int getFrameDelay() {
        return frameDelay;
    }

    public int getWidth() {
        int max = -1;
        for (BufferedImage frame : getFrames()) {
            if (frame.getWidth() > max) max = frame.getWidth();
        }
        return max;
    }

    public int getHeight() {
        int max = -1;
        for (BufferedImage frame : getFrames()) {
            if (frame.getHeight() > max) max = frame.getHeight();
        }
        return max;
    }

    public void flush() {
        for (BufferedImage frame : frames) {
            frame.flush();
        }
    }

    public static Sprite fromByteArray(String name, byte[][] byteArray, int frameDelay) throws IOException {
        BufferedImage[] frames = new BufferedImage[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            frames[i] = ImageUtils.fromByteArray(byteArray[i]);
        }
        return new Sprite(name, frameDelay, frames);
    }

    public byte[][] toByteArray() throws IOException {
        BufferedImage[] frames = getFrames();
        byte[][] byteArray = new byte[frames.length][];
        for (int i = 0; i < frames.length; i++) {
            byteArray[i] = ImageUtils.toByteArray(frames[i]);
        }
        return byteArray;
    }

    public void save(File directory) throws IOException {
        if (!directory.exists()) directory.mkdirs();
        ImageIO.write(toImage(), "png", new File(directory, "sprite.png"));
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", getName());
        metadata.put("frame_amount", getFrames().length);
        metadata.put("frame_delay", getFrameDelay());
        metadata.put("width", getWidth());
        metadata.put("height", getHeight());
        saveMetadata(metadata, new File(directory, "sprite.json"));
    }

    public static Sprite load(File directory) throws IOException {
        File metadataFile = new File(directory, "sprite.json");
        Map<String, Object> metadata = FileUtils.loadMetadata(metadataFile);
        File imageFile = new File(directory, "sprite.png");
        return fromImage((String) metadata.get("name"), ImageIO.read(imageFile), (int) ((double) metadata.get("frame_delay")), (int) ((double) metadata.get("width")), (int) ((double) metadata.get("height")));
    }

    public BufferedImage toImage() {
        BufferedImage[] frames = getFrames();
        BufferedImage spriteSheet = new BufferedImage(getWidth() * frames.length, getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = spriteSheet.createGraphics();
        for (int i = 0; i < frames.length; i++) {
            BufferedImage frame = frames[i];
            graphics.drawImage(frame, i * getWidth(), 0, null);
        }
        graphics.dispose();
        return spriteSheet;
    }

    public static Sprite fromImage(String name, BufferedImage image, int frameDelay, int width, int height) {
        BufferedImage[] frames = new BufferedImage[(int) Math.ceil((double) image.getWidth() / (double) width)];
        for (int i = 0; i < image.getWidth(); i += width) {
            frames[i / width] = image.getSubimage(i, 0, Math.min(width, image.getWidth() - i), height);
        }
        return new Sprite(name, frameDelay, frames);
    }

}
