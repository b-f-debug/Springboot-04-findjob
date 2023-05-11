package com.example.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@NoArgsConstructor
@Data
public class User implements Serializable {
    private static final long seriaversionUID=1L;
    private Integer u_id;
    private String u_password;
    private String u_email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    public User(Integer u_id,  String u_password, String u_email) throws ParseException {
        this.u_id = u_id;
        this.u_password = u_password;
        this.u_email = u_email;

        Date date =new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = simpleDateFormat.format(date);
        Date resTime = simpleDateFormat.parse(now);
        this.time = resTime;
    }
}
