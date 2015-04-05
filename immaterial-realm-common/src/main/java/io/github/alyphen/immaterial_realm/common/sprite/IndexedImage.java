package io.github.alyphen.immaterial_realm.common.sprite;

import io.github.alyphen.immaterial_realm.common.database.TableRow;

import java.awt.image.BufferedImage;

public class IndexedImage extends TableRow {

    private BufferedImage image;

    public IndexedImage(long id, BufferedImage image) {
        super(id);
        this.image = image;
    }

    public IndexedImage(BufferedImage image) {
        super(0);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
