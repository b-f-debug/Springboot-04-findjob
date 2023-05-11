package com.example.dao;

import com.example.bean.FileImage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FileImageDao {
    //获取文件列表
    List<FileImage> findByFile();

    void save(FileImage fileImage);
}
