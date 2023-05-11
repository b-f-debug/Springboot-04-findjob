package com.example.utils;

import com.example.utils.AesCrypt;
import com.example.utils.ImageUtils;
import com.example.utils.LsbUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class ImageSteganography {

    private static char[] KEY = {'l', 's', 'b'};

    public static File writeToImg(String data, String password, String imagePath) throws IOException {
        log.info("进入隐写术功能===============>" +imagePath);
        File imageFile = new File(imagePath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(imageFile);//字节流
            BufferedImage image = LsbUtils.streamToImage(inputStream);//字符流
            final int imageLength = image.getHeight() * image.getWidth();
            final int startingOffset = LsbUtils.calculateStartingOffset(null, imageLength);
            // hide text
            Steganography steganography = new Steganography();
            data = AesCrypt.encrypt(password, data);
            data = encryptDecrypt(password + "|" + data);
            log.info("加密后的数据============》"+data);
            BufferedImage bi = steganography.encode(image, data, startingOffset);
            ImageIO.write(image, ImageUtils.getFileExt(imageFile), imageFile);
            return imageFile;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't find file " + imagePath, e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static String readFromImg(InputStream inputStream) {

        try {
            log.info("从图片读取信息功能模块=============");
           // inputStream = new FileInputStream(file);

            BufferedImage image = LsbUtils.streamToImage(inputStream);

            final int imageLength = image.getWidth() * image.getHeight();
            final int startingOffset = LsbUtils.calculateStartingOffset(null, imageLength);

            Steganography steganography = new Steganography();
            String data = steganography.decode(image, startingOffset);
            data = encryptDecrypt(data);
            int indexOf = data.indexOf("|");
            String password = data.substring(0, indexOf);
            data = data.substring(indexOf);
            return AesCrypt.decrypt(password, data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't find file ", e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 简单加解密
     *
     * @param input
     * @return
     */
    private static String encryptDecrypt(String input) {
        StringBuilder output = new StringBuilder();
        //获取当前字符与密钥轮转异或后的值，将其转换成字符添加到 output 中
        for (int i = 0; i < input.length(); i++) {
            output.append((char) (input.charAt(i) ^ KEY[i % KEY.length]));
        }
        return output.toString();
    }

}
