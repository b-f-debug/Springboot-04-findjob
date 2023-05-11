package com.example.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LsbUtils {

    /**
     * Private constructor preventing outer initialization
     */
    private LsbUtils() {
        // Prevent initialization
    }

    /**
     * Gets an image from the given stream
     *
     * @param stream Stream to get the image from
     * @return The extracted image
     */
    public static BufferedImage streamToImage(InputStream stream) throws IOException {
        return ImageIO.read(stream);
    }

    /**
     * Calculates the index of the starting byte
     *
     * @param password The password given from the user
     * @param maxValue The length of the image
     * @return The index of the starting byte
     */
    public static int calculateStartingOffset(final String password, final long maxValue) {
        int offset = 0;

        if (StringUtil.isNotBlank(password)) {
            for (char c : password.toCharArray()) {
                offset += c;
            }

            offset %= maxValue;
        }

        return offset;
    }
}
