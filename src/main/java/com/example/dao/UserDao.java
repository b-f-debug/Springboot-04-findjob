package com.example.dao;

import com.example.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


@Repository
@Mapper
public interface UserDao {

    User queryByName(@Param("u_email")String u_email);
    User login(@Param("u_email") String u_email,@Param("u_password") String u_password);
    User userExit(@Param("u_email") String u_email);

    Integer ifExit(@Param("u_email")String u_email);

    int userInsert(@Param("u_email") String u_email,@Param("u_password")String u_password);

    int update(@Param("u_email")String u_email);

    int deleteById(Integer u_id);
}
