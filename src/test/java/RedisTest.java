import com.zjj.bean.RedisConfig;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisTest {

    @Autowired
    private RedisConfig redis;

    @Test
    public void redisTest() {
        String key = "aaa";
        System.out.println(key);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                System.out.println("thread[" + Thread.currentThread().getId() + "]进入...");
                boolean lock = redis.getSyncLock(key, 2000L);
                try {
                    if (lock) {
                        System.out.println("thread[" + Thread.currentThread().getId() + "]拿到锁...");
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
    }

    @Test
    public void redisTest1() {
        String key = "aaaaaa";
//        redis.unlock(key);
        System.out.println(key);
        boolean lock = false;
        try {
            lock = redis.getSyncLock(key, 2000L);
            if (lock) {
                System.out.println("拿到锁...");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (lock) {
                System.out.println("thread[" + Thread.currentThread().getId() + "]释放锁...");
                redis.unlock(key);
            }
        }
    }
}
