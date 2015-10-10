package io.github.alyphen.immaterial_realm.common.sprite;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.database.TableRow;
import io.github.alyphen.immaterial_realm.common.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.github.alyphen.immaterial_realm.common.util.FileUtils.saveMetadata;

public class Sprite extends TableRow {

    private ImmaterialRealm immaterialRealm;

    private String name;
    private BufferedImage[] frames;
    private int index;
    private int frameDelay;
    private int tick;

    public Sprite(ImmaterialRealm immaterialRealm, String name, int frameDelay, BufferedImage... frames) {
        this(immaterialRealm, UUID.randomUUID(), name, frameDelay, frames);
    }

    public Sprite(ImmaterialRealm immaterialRealm, UUID uuid, String name, int frameDelay, BufferedImage... frames) {
        super(uuid);
        this.immaterialRealm = immaterialRealm;
        this.name = name;
        this.frameDelay = frameDelay;
        this.frames = frames;
        immaterialRealm.getSpriteManager().addSprite(this);
    }

    public void onTick() {
        tick = tick == frameDelay - 1 ? 0 : tick + 1;
        if (tick == 0) index = index == frames.length - 1 ? 0 : index + 1;
    }

    public void paint(Graphics graphics) {
        graphics.drawImage(frames[index], 0, 0, null);
    }

    public void paint(Graphics graphics, int x, int y) {
        graphics.drawImage(frames[index], x, y, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        immaterialRealm.getSpriteManager().updateSprite(this);
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

}
