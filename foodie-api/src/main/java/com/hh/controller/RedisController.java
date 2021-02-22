package com.hh.controller;

import com.hh.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author HuangHao
 * @date 2020/10/12 0:10
 */
@ApiIgnore
@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(String key, String value) {

        redisOperator.set(key, value);
        return "OK";
    }
    @GetMapping("/get")
    public String get(String key) {

        return redisOperator.get(key);
    }
    @GetMapping("/delete")
    public Object delete(String key) {
        redisOperator.del(key);
        return "OK";
    }


}
