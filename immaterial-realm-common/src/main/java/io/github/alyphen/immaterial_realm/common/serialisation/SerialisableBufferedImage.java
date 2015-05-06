package io.github.alyphen.immaterial_realm.common.serialisation;

import io.github.alyphen.immaterial_realm.common.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

public class SerialisableBufferedImage implements Serializable {

    private final byte[] data;

    public SerialisableBufferedImage(BufferedImage image) throws IOException {
        this.data = ImageUtils.toByteArray(image);
    }

    public BufferedImage toImage() throws IOException {
        return ImageUtils.fromByteArray(data);
    }

}
