package io.github.alyphen.immaterial_realm.common.tile;

import io.github.alyphen.immaterial_realm.common.packet.clientbound.tile.PacketSendTile;
import io.github.alyphen.immaterial_realm.common.util.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class Tile {

    private static Map<String, Tile> tiles;

    static {
        tiles = new HashMap<>();
    }

    public static void loadTiles() throws IOException {
        File tilesDirectory =new File("./tiles");
        if (tilesDirectory.isDirectory()) {
            File[] tileDirectories = tilesDirectory.listFiles(File::isDirectory);
            if (tileDirectories != null) {
                for (File tileDirectory : tileDirectories) {
                    load(tileDirectory);
                }
            }
        }
    }

    public static void load(File directory) throws IOException {
        tiles.put(directory.getName(), new Tile(directory));
    }

    public static void load(PacketSendTile packet) throws IOException {
        tiles.put(packet.getTileName(),
                new Tile(
                        packet.getTileName(), packet.getTileWidth(), packet.getTileHeight(), packet.getTileFrames(),
                        packet.getTileFrameDuration()
                )
        );
    }

    public static Tile getTile(String name) {
        return tiles.get(name);
    }

    public static Collection<Tile> getTiles() {
        return tiles.values();
    }

    private BufferedImage[] frames;
    private int frameIndex;
    private String name;
    private int width;
    private int height;
    private int frameDuration;
    private long currentFrameTicks;

    public Tile(File directory) throws IOException {
        File metadataFile = new File(directory, "tile.json");
        File imageFile = new File(directory, "tile.png");
        Map<String, Object> metadata = FileUtils.loadMetadata(metadataFile);
        name = directory.getName();
        width = (int) ((double) metadata.get("frame-width"));
        height = (int) ((double) metadata.get("frame-height"));
        frameDuration = (int) ((double) metadata.get("frame-duration"));
        BufferedImage sheet = ImageIO.read(imageFile);
        frames = new BufferedImage[(sheet.getWidth() / width) * (sheet.getHeight() / height)];
        for (int x = 0; x < sheet.getWidth(); x += width) {
            for (int y = 0; y < sheet.getHeight(); y += height) {
                frames[(((x / width) + 1) * ((y / height) + 1)) - 1] = sheet.getSubimage(x, y, width, height);
            }
        }
    }

    public Tile(String name, int width, int height, BufferedImage[] frames, int frameDuration) {
        this.frames = frames;
        this.name = name;
        this.width = width;
        this.height = height;
        this.frameDuration = frameDuration;
    }

    public Tile(String name, int width, int height, BufferedImage image, int frameDuration) {
        frames = new BufferedImage[(int) Math.ceil((double) image.getWidth() / (double) width)];
        for (int i = 0; i < image.getWidth(); i += width) {
            frames[i / width] = image.getSubimage(i, 0, Math.min(width, image.getWidth() - i), height);
        }
        this.name = name;
        this.width = width;
        this.height = height;
        this.frameDuration = frameDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BufferedImage[] getFrames() {
        return frames;
    }

    public BufferedImage getFrame(int index) {
        return frames[index];
    }

    public void setFrame(int index, BufferedImage frame) {
        frames[index] = frame;
    }

    public void addFrame(BufferedImage frame) {
        frames = Arrays.copyOf(frames, frames.length + 1);
        frames[frames.length - 1] = frame;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFrameDuration() {
        return frameDuration;
    }

    public void paint(Graphics graphics, int x, int y) {
        graphics.drawImage(frames[frameIndex], x, y, null);
    }

    public void onTick() {
        if (frameDuration < 0) return;
        currentFrameTicks += 1;
        if (currentFrameTicks >= frameDuration) {
            currentFrameTicks -= frameDuration;
            frameIndex = frameIndex < frames.length - 1 ? frameIndex + 1 : 0;
        }
    }


    public void save(File directory) throws IOException {
        if (!directory.isDirectory()) directory.delete();
        if (!directory.exists()) directory.mkdirs();
        File imageFile = new File(directory, "tile.png");
        File metadataFile = new File(directory, "tile.json");
        BufferedImage image = new BufferedImage(getWidth() * getFrames().length, getHeight(), TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        int x = 0;
        for (BufferedImage frame : getFrames()) {
            graphics.drawImage(frame, x, 0, null);
            x += frame.getWidth();
        }
        graphics.dispose();
        ImageIO.write(image, "png", new FileOutputStream(imageFile));
        image.flush();
        Map<String, Object> tileMetadata = new HashMap<>();
        tileMetadata.put("frame-width", getWidth());
        tileMetadata.put("frame-height", getHeight());
        tileMetadata.put("frame-duration", getFrameDuration());
        FileUtils.saveMetadata(tileMetadata, metadataFile);
    }
}
