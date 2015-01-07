package io.github.alyphen.immaterial_realm.common.packet.clientbound.tile;

import io.github.alyphen.immaterial_realm.common.util.ImageUtils;
import io.github.alyphen.immaterial_realm.common.packet.Packet;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class PacketSendTileSheet extends Packet {

    private String name;
    private byte[] image;
    private int tileWidth;
    private int tileHeight;

    public PacketSendTileSheet(String name, BufferedImage image, int tileWidth, int tileHeight) {
        this.name = name;
        try {
            this.image = ImageUtils.toByteArray(image);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        try {
            return ImageUtils.fromByteArray(image);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

}
