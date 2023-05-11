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
public class Draft implements Serializable {
    private int d_id;
    private String d_from;
    private String d_to;

    private String d_subject;
    private String d_content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date d_time;

    public Draft(int d_id,String d_from, String d_to, String d_subject, String d_content) throws ParseException {
        this.d_id=d_id;
        this.d_from = d_from;
        this.d_to = d_to;
        this.d_subject = d_subject;
        this.d_content = d_content;

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date m_time = new Date();
        String now = simpleDateFormat.format(m_time);
        Date resTime = simpleDateFormat.parse(now);
        d_time = resTime;
    }
}
