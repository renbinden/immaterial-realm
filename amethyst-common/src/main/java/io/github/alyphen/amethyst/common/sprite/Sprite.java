package io.github.alyphen.amethyst.common.sprite;

import io.github.alyphen.amethyst.common.util.FileUtils;
import io.github.alyphen.amethyst.common.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Sprite {

    private BufferedImage[] frames;
    private int index;
    private int frameDelay;
    private int tick;

    public Sprite(int frameDelay, BufferedImage... frames) {
        this.frameDelay = frameDelay;
        this.frames = frames;
    }

    public void onTick() {
        tick = tick == frameDelay - 1 ? 0 : tick + 1;
        if (tick == 0) index = index == frames.length - 1 ? 0 : index + 1;
    }

    public void paint(Graphics graphics) {
        graphics.drawImage(frames[index], 0, 0, null);
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

    public static Sprite fromByteArray(byte[][] byteArray, int frameDelay) throws IOException {
        BufferedImage[] frames = new BufferedImage[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            frames[i] = ImageUtils.fromByteArray(byteArray[i]);
        }
        return new Sprite(frameDelay, frames);
    }

    public byte[][] toByteArray() throws IOException {
        BufferedImage[] frames = getFrames();
        byte[][] byteArray = new byte[frames.length][];
        for (int i = 0; i < frames.length; i++) {
            byteArray[i] = ImageUtils.toByteArray(frames[i]);
        }
        return byteArray;
    }

    public void save(File file) throws IOException {
        ImageIO.write(toImage(this), "png", file);
    }

    public static Sprite load(File directory) throws IOException {
        File metadataFile = new File(directory, "sprite.json");
        Map<String, Object> metadata = FileUtils.loadMetadata(metadataFile);
        File imageFile = new File(directory, "sprite.png");
        return fromImage(ImageIO.read(imageFile), (int) metadata.get("frame_amount"), (int) metadata.get("frame_delay"), (int) metadata.get("width"), (int) metadata.get("height"));
    }

    public BufferedImage toImage(Sprite sprite) {
        BufferedImage[] frames = sprite.getFrames();
        BufferedImage spriteSheet = new BufferedImage((sprite.getWidth() * frames.length) + 8, sprite.getHeight(), BufferedImage.TYPE_INT_RGB);
        spriteSheet.setRGB(0, 0, frames.length);
        spriteSheet.setRGB(1, 0, sprite.getFrameDelay());
        spriteSheet.setRGB(2, 0, sprite.getWidth());
        spriteSheet.setRGB(3, 0, sprite.getHeight());
        Graphics graphics = spriteSheet.createGraphics();
        for (int i = 0; i < frames.length; i++) {
            BufferedImage frame = frames[i];
            graphics.drawImage(frame, i * sprite.getWidth(), 0, null);
        }
        graphics.dispose();
        return spriteSheet;
    }

    public static Sprite fromImage(BufferedImage image, int frameAmount, int frameDelay, int width, int height) {
        BufferedImage[] frames = new BufferedImage[frameAmount];
        for (int i = 0; i < frameAmount; i++) {
            frames[i] = image.getSubimage(i * width, 0, width, height);
        }
        return new Sprite(frameDelay, frames);
    }

}
