package com.example.coladder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class Redis
{
    @Autowired
    private StringRedisTemplate template;

    @Test
    public void set(){
        template.opsForValue().set("key","value");

    }

    @Test
    public void get(){
        ValueOperations<String, String> ops = template.opsForValue();
        System.out.println(ops.get("key"));
    }
}
