import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

@Component
public class HideUtil {

    // 将字符串转换为二进制字符串
    private static String textToBinary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char c : text.toCharArray()) {
            String charBinary = Integer.toBinaryString(c);
            while (charBinary.length() < 8) {
                charBinary = "0" + charBinary;
            }
            binary.append(charBinary);
        }
        return binary.toString();
    }

    // 将二进制字符串转换为字符串
    private static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            String binaryChar = binary.substring(i, i + 8);
            char c = (char) Integer.parseInt(binaryChar, 2);
            text.append(c);
        }
        return text.toString();
    }

    // 将文本隐藏在图像中
    public static void hideText(BufferedImage image, String text, String outputImagePath) throws IOException {
        String binaryText = textToBinary(text);
        int binaryLength = binaryText.length();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int pixelCount = 0;
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                if (pixelCount < binaryLength) {
                    int pixel = image.getRGB(x, y);
                    int alpha = (pixel >> 24) & 0xff;
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = pixel & 0xff;
                    red = (red & 0xfe) | (binaryText.charAt(pixelCount) == '1' ? 1 : 0);
                    pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                    image.setRGB(x, y, pixel);
                    pixelCount++;
                }
            }
        }
        ImageIO.write(image, "png", new File(outputImagePath));
    }

    // 从图像中提取隐藏的文本
    public static String extractText(BufferedImage image) {
        StringBuilder binaryText = new StringBuilder();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int pixelCount = 0;
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                if (pixelCount < 8) {
                    int pixel = image.getRGB(x, y);
                    int red = (pixel >> 16) & 0xff;
                    binaryText.append(red & 1);
                    pixelCount++;
                } else {
                    String binaryLengthString = binaryText.toString();
                    int binaryLength = Integer.parseInt(binaryLengthString, 2);
                    StringBuilder binaryTextBuilder = new StringBuilder();
                    for (int i = 0; i < binaryLength * 8; i++) {
                        int pixel = image.getRGB(x, y);
                        int red = (pixel >> 16) & 0xff;
                        binaryTextBuilder.append(red & 1);
                        pixelCount++;
                        if (pixelCount % 8 == 0) {
                            x++;
                            if (x >= imageWidth) {
                                x = 0;
                                y++;
                            }
                        }
                    }
                    return binaryToText(binaryTextBuilder.toString());
                }
            }
        }
        return "";
    }


}