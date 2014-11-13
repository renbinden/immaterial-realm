package io.github.lucariatias.amethyst.common.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    private ImageUtils() {}

    public static byte[] toByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return outputStream.toByteArray();
    }

    public static BufferedImage fromByteArray(byte[] byteArray) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(byteArray));
    }

}
