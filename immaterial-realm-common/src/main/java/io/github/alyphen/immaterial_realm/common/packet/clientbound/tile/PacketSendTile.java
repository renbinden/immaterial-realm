package io.github.alyphen.immaterial_realm.common.packet.clientbound.tile;

import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.tile.Tile;
import io.github.alyphen.immaterial_realm.common.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class PacketSendTile extends Packet {

    private String tileName;
    private byte[][] tileFramesData;
    private int tileWidth;
    private int tileHeight;
    private int tileFrameDuration;

    public PacketSendTile(Tile tile) throws IOException {
        tileName = tile.getName();
        BufferedImage[] frames = tile.getFrames();
        tileFramesData = new byte[frames.length][];
        for (int i = 0; i < tile.getFrames().length; i++) {
            tileFramesData[i] = ImageUtils.toByteArray(frames[i]);
        }
        tileWidth = tile.getWidth();
        tileHeight = tile.getHeight();
        tileFrameDuration = tile.getFrameDuration();
    }

    public String getTileName() {
        return tileName;
    }

    public BufferedImage[] getTileFrames() throws IOException {
        BufferedImage[] frames = new BufferedImage[tileFramesData.length];
        for (int i = 0; i < tileFramesData.length; i++) {
            frames[i] = ImageUtils.fromByteArray(tileFramesData[i]);
        }
        return frames;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileFrameDuration() {
        return tileFrameDuration;
    }

}
