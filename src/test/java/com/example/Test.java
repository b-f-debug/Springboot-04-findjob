package com.example;

import io.jsonwebtoken.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Test {

    private long time=1000*60*60*24;
    private String signature="admin";
    @org.junit.Test
    public void jwt(){
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken=jwtBuilder
                .setHeaderParam("alg","HS256")
                .setHeaderParam("typ","JWT")
                .claim("username","tom")
                .claim("role","admin")
                .setSubject("test")
                .setExpiration(new Date(System.currentTimeMillis()+time))
                //signature
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        System.out.println(jwtToken);
    }

    @org.junit.Test
    public void parse(){
        String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRvbSIsInJvbGUiOiJhZG1pbiIsInN1YiI6InRlc3QiLCJleHAiOjE2ODI4MzczMTV9.n_hxf_MhW_3eqb91HvJbx34gzAyr4fIXoe0eKou1_Ak";
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws= jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        System.out.println(claims.get("username"));
        System.out.println(claims.get("role"));
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getExpiration());

    }
    public static void main(String[] args) {
        try {
            // 加载图像
            BufferedImage image = ImageIO.read(new File("input.png"));

            // 隐藏文本
            String textToHide = "这是一个秘密文本";
           // hideText(image, textToHide, "output.png");

            // 从图像中提取隐藏的文本
            BufferedImage hiddenTextImage = ImageIO.read(new File("output.png"));
           // String extractedText = extractText(hiddenTextImage);
           // System.out.println("从图像中提取的文本：" + extractedText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
