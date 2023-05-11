package com.example.service;

import com.example.bean.User;

import java.util.List;

public interface UserService {


    User queryByName(String u_name);

    Integer ifExit(String u_email);
    Integer userInsert(String u_email,String u_password);

    int update(String u_email);

    boolean deleteById(Integer u_id);

    User login(String u_email,String u_password);

    User userExit(String u_email);
}
