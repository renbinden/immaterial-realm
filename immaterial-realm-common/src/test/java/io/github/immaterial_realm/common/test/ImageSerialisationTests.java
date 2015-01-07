package io.github.immaterial_realm.common.test;

import com.insightfullogic.lambdabehave.JunitSuiteRunner;
import io.github.alyphen.immaterial_realm.common.util.ImageUtils;
import org.junit.runner.RunWith;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.insightfullogic.lambdabehave.Suite.describe;

@RunWith(JunitSuiteRunner.class)
public class ImageSerialisationTests {

    public ImageSerialisationTests() throws IOException {
        BufferedImage originalImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics imageGraphics = originalImage.createGraphics();
        imageGraphics.setColor(Color.BLACK);
        imageGraphics.fillRect(0, 0, originalImage.getWidth(), originalImage.getHeight());
        imageGraphics.setColor(Color.WHITE);
        imageGraphics.fillOval(0, 0, originalImage.getWidth(), originalImage.getHeight());
        imageGraphics.dispose();
        byte[] serialised = ImageUtils.toByteArray(originalImage);
        BufferedImage deserialisedImage = ImageUtils.fromByteArray(serialised);
        describe("an image", it -> {
            it.should("have equal dimensions when serialised and deserialised", expect -> {
                expect.that(originalImage.getWidth()).is(deserialisedImage.getWidth());
                expect.that(originalImage.getHeight()).is(deserialisedImage.getHeight());
            });
            it.should("have the same colour in each pixel", expect -> {
                for (int x = 0; x < originalImage.getWidth(); x++) {
                    for (int y = 0; y < originalImage.getHeight(); y++) {
                        expect.that(originalImage.getRGB(x, y)).is(deserialisedImage.getRGB(x, y));
                    }
                }
            });
        });
        originalImage.flush();
    }

}
