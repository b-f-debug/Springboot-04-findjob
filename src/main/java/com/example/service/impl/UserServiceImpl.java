package com.example.service.impl;

import com.example.bean.User;
import com.example.dao.UserDao;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User queryByName(String u_name) {
        return userDao.queryByName(u_name);
    }

    @Override
    public User login(String u_email, String u_password) {
        log.info("===========》调用了serviceimpl--login");
        return userDao.login(u_email,u_password);
    }

    @Override
    public Integer ifExit(String u_email) {
        log.info("=================>调用了serviceimpl--ifExit");
        return userDao.ifExit(u_email);
    }

    @Override
    public User userExit(String u_email) {
        log.info("===========》调用了serviceimpl--userExit");
        return userDao.userExit(u_email);
    }

    @Override
    public Integer userInsert(String u_email,String u_password) {
        log.info("===========》调用了serviceimpl--userInsert");
        return userDao.userInsert(u_email,u_password);
    }

    @Override
    public int update(String u_email) {
        log.info("===============>>调用了serviceimpl的update");
        return userDao.update(u_email);
    }

    @Override
    public boolean deleteById(Integer u_id) {
        return false;
    }
}
