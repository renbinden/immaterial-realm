package io.github.alyphen.immaterial_realm.common.sprite;

import io.github.alyphen.immaterial_realm.common.database.TableRow;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class IndexedImage extends TableRow {

    private BufferedImage image;

    public IndexedImage(UUID uuid, BufferedImage image) {
        super(uuid);
        this.image = image;
    }

    public IndexedImage(BufferedImage image) {
        this(UUID.randomUUID(), image);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
