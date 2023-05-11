package com.example.dao;

import com.example.bean.Email;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Collection;

@Repository
@Mapper
public interface EmailDao {

    int emailInsert(@Param("m_from") String m_from, @Param("m_to")String m_to, @Param("m_subject")String m_subject, @Param("email")String email,@Param("flag")boolean flag);

    Collection<Email> queryAllBySend(@Param("m_from")String m_from);
    Collection<Email> queryAllByReceive(@Param("m_to")String m_to);

    Email queryById(@Param("m_id")int m_id);
    Email queryById_R(@Param("m_id")int m_id);

    Integer getMaxId(String m_from);

}
