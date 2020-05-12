package com.zjj.task;

import com.zjj.bean.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
@Component
@EnableScheduling
public class TestTask {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisConfig redis;

    @Scheduled(cron = "0/10 * * * * ?")
    public void reSavePayInfo() {
        long start = System.currentTimeMillis();
        try {
            String key = String.valueOf(start);
            redis.put(key,key,2L);
            logger.info("redis,key[{}],value[{}]" , redis.getString(key),redis.getString(key));
        } catch (Exception e) {
            logger.error("每10秒钟执行一次redis——put", e);
        }
        long end = System.currentTimeMillis();
        logger.info("每10秒钟执行一次每10秒钟执行一次redis——put，cost:{} ms", end - start);
    }


}
