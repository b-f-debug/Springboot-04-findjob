package com.example.utils;

import java.io.*;

public final class StringUtil {

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !StringUtil.isBlank(str);
    }

    public static final InputStream byteToInput(byte[] buf) {
                return new ByteArrayInputStream(buf);
            }


            public static final byte[] inputTobyte(InputStream inStream) throws IOException {
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                 byte[] buff = new byte[100];
                 int rc = 0;
                 while ((rc = inStream.read(buff, 0, 100)) > 0) {
                        swapStream.write(buff, 0, rc);
                   }
                byte[] in2b = swapStream.toByteArray();
               return in2b;
           }
}
