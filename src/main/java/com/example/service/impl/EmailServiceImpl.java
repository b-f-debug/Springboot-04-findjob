package com.example.service.impl;

import com.example.bean.Email;
import com.example.dao.EmailDao;
import com.example.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collection;

@Slf4j
@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Resource
    private EmailDao emailDao;
    @Override
    public int emailInsert(String m_from, String m_to, String m_subject, String email, boolean flag) {
        return emailDao.emailInsert(m_from,m_to,m_subject,email,flag);
    }

    @Override
    public Email queryById(int m_id) {
        return emailDao.queryById(m_id);
    }

    @Override
    public Collection<Email> queryAllByReceive(String m_to) {
        return emailDao.queryAllByReceive(m_to);
    }

    @Override
    public Integer getMaxId(String m_from) {
        return emailDao.getMaxId(m_from);
    }

    @Override
    public Email queryById_R(int m_id) {
        return emailDao.queryById_R(m_id);
    }

    @Override
    public Collection<Email> queryAllBySend(String m_from) {
        return emailDao.queryAllBySend(m_from);
    }

}
