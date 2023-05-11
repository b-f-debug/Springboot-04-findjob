package com.example.service;

import com.example.bean.Email;


import java.util.Collection;

public interface EmailService {
     int emailInsert(String m_from,String m_to,String m_subject,String email,boolean flag);
     Collection<Email> queryAllBySend(String m_from);
     Email queryById(int m_id);
     Email queryById_R(int m_id);
     Collection<Email> queryAllByReceive(String m_to);

    Integer getMaxId(String m_from);

}
