package com.example.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@Data
public class Email {
    private int m_id;
    private String m_content;
    private String m_to;
    private String m_from;
    private String m_subject;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date m_time;
    private boolean m_flag;//0表示未发，1表示发送

    public Email(String m_content, String m_to, String m_from, String m_subject, Date m_time) throws ParseException {
        this.m_content = m_content;
        this.m_to = m_to;
        this.m_from = m_from;
        this.m_subject = m_subject;

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = simpleDateFormat.format(m_time);
        Date resTime = simpleDateFormat.parse(now);
        this.m_time = resTime;
    }
}
