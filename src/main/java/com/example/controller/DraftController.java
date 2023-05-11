package com.example.controller;

import com.example.bean.Draft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DraftController {

    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping("/set")
    public void set(@RequestBody Draft draft ){
        redisTemplate.opsForValue().set("draft",draft);
        redisTemplate.opsForValue().get("draft");
    }

}
