package com.example.service.impl;

import com.example.bean.FileImage;
import com.example.dao.FileImageDao;
import com.example.service.FileImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service("fileImageService")
public class FileImageImpl implements FileImageService {
   @Resource
   private FileImageDao fileImageDao;
    @Override
    public void save(FileImage fileImage) {
        log.info("进入到image功能");
        fileImageDao.save(fileImage);
    }
}
