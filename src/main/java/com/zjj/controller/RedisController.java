package com.zjj.controller;

import com.zjj.bean.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequestMapping("/redis")
@RestController
public class RedisController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisConfig redis;

    @Autowired
    private RedisTemplate<Object, Object> template;

    @GetMapping("/set")
    public Object set(String key, String value) {
        template.opsForValue().set(key, value);
        return template.opsForValue().get(key);
    }

    @GetMapping("/get")
    public Object get(String key) {
        return redis.getString(key);
    }

    @GetMapping("/getLock")
    public Object getLock(String key) {
//        redis.unlock(key);

        System.out.println(key);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 2; i++) {
            executor.submit(() -> {
                System.out.println("thread[" + Thread.currentThread().getId() + "]进入...");
                boolean lock = redis.getSyncLock(key, 2000L);
                try {
                    if (lock) {
                        System.out.println("thread[" + Thread.currentThread().getId() + "]拿到锁...");
                        Thread.sleep(5000);
                        logger.info("thread[" + Thread.currentThread().getId() + "]睡眠2s...");
                    }
                } catch (Exception e) {

                } finally {
                    if (lock) {
                        System.out.println("thread[" + Thread.currentThread().getId() + "]释放锁...");
                        redis.unlock(key);
                    }
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        return true;
    }

}
