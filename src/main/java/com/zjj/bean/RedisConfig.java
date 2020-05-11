package com.zjj.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjj.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);
    @Autowired
    private RedisTemplate<Object, Object> template;

    @Autowired
    private RedisTemplate<String, String> templateList;

    /**
     * 用以解决key值前出现\xAC\xED\x00\x05t\x00\x06等乱码问题
     *
     * @param redisTemplate
     */
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.template = redisTemplate;
    }

    /**
     * 判断key是什么类型
     * none, string, list, set, zset, hash
     */
    public String type(Object key) {
        return template.type(key).code();
    }

    /**
     * 自动增长指定的step大小
     */
    public long increment(String key, long step) {
        return template.boundValueOps(key).increment(step);
    }

    /**
     * 检查给定 key 是否存在
     *
     * @param key
     */
    public boolean exists(Object key) {
        boolean flag = template.hasKey(key);
        long expiretime = getExpireTime(key);
        if (flag && (expiretime > 0L || expiretime == -1))
            return true;
        return false;
    }

    /**
     * 检查给定 key 是否存在
     *
     * @param key
     */
    public long getExpireTime(Object key) {
        return template.getExpire(key);
    }

    /**
     * 更新key的过期时间
     *
     * @param key
     * @param timeout（秒）
     */
    public boolean expire(String key, long timeout) {
        return template.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置到什么时候过期
     *
     * @param key
     * @param date
     */
    public boolean expireAt(String key, Date date) {
        return template.expireAt(key, date);
    }

    public void del(String key) {
        template.delete(key);
    }

    /**
     * 单个对象添加缓存
     *
     * @param key
     * @param value
     * @param timeout 过期时间（秒），不填默认永不过期
     */
    public void put(String key, Object value, Long timeout) {
        try {
            if (null != timeout && timeout.longValue() != 0l) {
                template.opsForValue().set(key, String.valueOf(value), timeout, TimeUnit.SECONDS);
            } else {
                template.opsForValue().set(key, String.valueOf(value));
            }
        } catch (Exception e) {
            LOGGER.error("redis setString method exception", e);
        }
    }

    public String getString(String key) {
        return (String) template.opsForValue().get(key);
    }

    public Object getObject(String key) {
        return template.opsForValue().get(key);
    }

    /**
     * 添加Object对象, timeout为空表示永不过期
     */
    public void putJavaObject(String key, Object obj, Long timeout) {
        this.put(key, JSON.toJSONString(obj), timeout);
    }

    /**
     * 获取Object对象
     */
    public <T> T getJavaObject(String key, Class<T> clazz) {
        try {
            String objStr = this.getString(key);
            if (StringUtil.isEmpty(objStr)) {
                return null;
            }
            return JSONObject.parseObject(objStr, clazz);
        } catch (Exception e) {
            LOGGER.error("读取缓存失败", e);
        }
        return null;
    }

    /**
     * 存储list对象, timeout为空表示永不过期
     */
    public void putList(String key, List<?> list, Long timeout) {
        try {
            this.put(key, JSON.toJSONString(list), timeout);
        } catch (Exception e) {
            LOGGER.error("putList exception", e);
        }
    }

    /**
     * 存储set对象, timeout为空表示永不过期
     */
    public void putSet(String key, Set<?> set, Long timeout) {
        try {
            this.put(key, JSON.toJSONString(set), timeout);
        } catch (Exception e) {
            LOGGER.error("putSet exception", e);
        }
    }

    /**
     * 获取set对象
     */
    public Set<String> getSet(String key) {
        Set<String> result = null;
        try {
            String str = this.getString(key);
            if (StringUtil.isNotEmpty(str)) {
                result = (Set) JSON.parseArray(str, List.class);
            }
        } catch (Exception e) {
            LOGGER.error("getSet exception", e);
        }
        return result;
    }

    /**
     * 存储list, timeout为空表示永不过期
     *
     * @param timeout 过期时间（秒）
     */
    public boolean leftPush(String key, String value, Long timeout) {
        try {
            templateList.opsForList().leftPush(key, value);
            if (null != timeout) {
                templateList.expire(key, timeout, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("leftPush exception", e);
            return false;
        }
    }

    /**
     * 从list中pop出一个元素
     */
    public String leftPop(String key) {
        String result = null;
        try {
            result = templateList.opsForList().leftPop(key);
        } catch (Exception e) {
            LOGGER.error("leftPop exception", e);
        }
        return result;
    }

    /**
     * 从list中pop出一个元素
     */
    public String rightPop(String key) {
        String result = null;
        try {
            result = templateList.opsForList().rightPop(key);
        } catch (Exception e) {
            LOGGER.error("rightPop exception", e);
        }
        return result;
    }

    /**
     * 从list中pop出一个元素
     */
    public String rightPopAndLeftPush(String sourceKey, String destinationKey, long timeout) {
        String result = null;
        try {
            result = templateList.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("rightPopAndLeftPush exception", e);
        }
        return result;
    }

    /**
     * 获取list对象
     */
    public List<?> getList(String key, Class<?> clazz) {
        List<?> result = null;
        try {
            String str = this.getString(key);
            if (StringUtil.isNotEmpty(str)) {
                result = JSON.parseArray(str, clazz);
            }
        } catch (Exception e) {
            LOGGER.error("getList exception", e);
        }
        return result;
    }

    public List<String> getRelList(String key) {
        List<String> result = null;
        try {
            if (exists(key)) {
                Long size = templateList.opsForList().size(key);
                result = templateList.opsForList().range(key, 0, size);
            }
        } catch (Exception e) {
            LOGGER.error("getRelList exception", e);
        }
        return result;
    }

    /**
     * 截取缓存List部分内容
     *
     * @param key
     * @param start 开始位置，下标从0起
     * @param end   结束位置
     */
    public List<String> getRangeList(String key, long start, long end) {
        List<String> result = null;
        try {
            if (exists(key)) {
                Long size = templateList.opsForList().size(key);
                if (size >= end)
                    result = templateList.opsForList().range(key, start, size);
            }
        } catch (Exception e) {
            LOGGER.error("getRangeList exception", e);
        }
        return result;
    }

    public Long lrem(String key, long count, String value) {
        Long length = null;
        try {
            if (exists(key)) {
                length = templateList.opsForList().remove(key, count, value);
            }
        } catch (Exception e) {
            LOGGER.error("lrem exception", e);
        }
        return length;
    }

    public Long lpushx(String key, List<String> values) {
        Long length = null;
        try {
            length = templateList.opsForList().rightPushAll(key, values);
        } catch (Exception e) {
            LOGGER.error("lpushx exception", e);
        }
        return length;
    }

    public Long lsize(String key) {
        Long length = (long) 0;
        try {
            if (exists(key)) {
                length = templateList.opsForList().size(key);
            }
        } catch (Exception e) {
            LOGGER.error("lpushx exception", e);
        }
        return length;
    }

    /***************************** Set操作 ******************************/
    /**
     * 向Set中添加多个元素
     *
     * @return 添加数量
     */
    public Long sadd(String key, Object... values) {
        return template.opsForSet().add(key, values);
    }

    /**
     * 向Set中添加多个元素
     *
     * @param timeout 有效期（秒）
     * @return 添加数量
     */
    public Long sadd(Long timeout, String key, String... values) {
        Long add = templateList.opsForSet().add(key, values);
        templateList.expire(key, timeout, TimeUnit.SECONDS);
        return add;
    }

    /**
     * 移除并返回集合中的一个随机元素
     *
     * @return 添加数量
     */
    public String spop(String key) {
        return templateList.opsForSet().pop(key);
    }

    /**
     * 获取集合中所有的元素
     */
    public Set<Object> members(String key) {
        return template.opsForSet().members(key);
    }

    /**
     * 获取Set集合内元素个数
     */
    public Long scard(String key) {
        return template.opsForSet().size(key);
    }

    /**
     * 将 member 元素从 source 集合移动到 destination 集合。
     * 若source 集合不存在或不包含指定的 member 元素，返回0，执行成功返回1，当 source 或 destination 不是集合类型时，返回一个错误
     *
     * @param source
     * @param destination
     * @param member
     */
    public Boolean smove(String source, Object member, String destination) {
        return template.opsForSet().move(source, member, destination);
    }

    /**
     * 判断 member 元素是否集合 key 的成员
     */
    public Boolean isSMember(String key, Object member) {
        return template.opsForSet().isMember(key, member);
    }

    /**
     * 移除Set里的一个或多个元素
     */
    public Long srem(String key, Object... members) {
        return template.opsForSet().remove(key, members);
    }

    /**
     * 返回所有集合的并集
     */
    public Set<Object> sunion(String key, Set<Object> otherKeys) {
        return template.opsForSet().union(key, otherKeys);
    }


    /**
     * 增加map
     */
    public void setMap(String key, Map<String, Object> map) {
        try {
            template.opsForHash().putAll(key, map);
        } catch (Exception e) {
            LOGGER.error("redis 操作出错！", e);
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean setMap(String key, Map<String, Object> map, long time) {
        try {
            template.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("redis 操作出错！", e);
            return false;
        }
    }

    /**
     * 向已经存在的 map 里添加键值对  hashKey: value
     */
    public void putMapKV(String key, Object hashKey, Object value) {
        try {
            template.opsForHash().put(key, hashKey, value);
        } catch (Exception e) {
            LOGGER.error("redis 操作出错！", e);
        }
    }

    /**
     * 向已经存在的 map 里添加键值对  hashKey: value
     *
     * @param key
     * @param hashKey
     * @param value
     * @param time    超时时间（秒）
     */
    public void putMapKV(String key, Object hashKey, Object value, long time) {
        try {
            template.opsForHash().put(key, hashKey, value);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            LOGGER.error("redis 操作出错！", e);
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return template.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0) 要减少几（小于0）
     * @return
     */
    public Long hincr(String key, String item, long by) {
        return template.opsForHash().increment(key, item, by);
    }

    /**
     * 获取 map 里键  hashKey 的值
     */
    public Object getMapKV(String key, Object hashKey) {
        try {
            return template.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            LOGGER.error("redis 操作出错！", e);
            return null;
        }
    }

    /**
     * 获取 map 里键  hashKey 的值
     */
    public Object delMapKV(String key, Object hashKey) {
        try {
            return template.opsForHash().delete(key, hashKey);
        } catch (Exception e) {
            LOGGER.error("redis 操作出错！", e);
            return null;
        }
    }

    /**
     * 通过key值获取map集合
     *
     * @param key
     * @return
     */
    public Map<Object, Object> getMap(String key) {
        try {
            return template.opsForHash().entries(key);
        } catch (Exception e) {
            LOGGER.error("redis 操作出错！", e);
            return null;
        }
    }

    /**
     * 通过values(H key)方法获取变量中的hashMap值
     *
     * @param key
     * @return
     */
    public List<Object> getMapV(String key) {
        try {
            return template.opsForHash().values(key);
        } catch (Exception e) {
            LOGGER.error("redis 操作出错！", e);
            return null;
        }
    }

    /**
     * 对map里hashKey对应的値自增 delta
     */
    public void incrementMapValue(String key, Object hashKey, long delta) {
        try {
            template.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {
            LOGGER.error("redis 操作出错！", e);
        }
    }

    /**
     * 判断hash中的key是否存在
     */
    public boolean existMapKey(String key, Object hashKey) {
        return template.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取同步锁
     *
     * @param key
     * @param timeout 毫秒
     * @return boolean 返回 true 表示获取成功
     */
    public boolean getSyncLock(String key, Long timeout) {
        boolean result = true;
        RedisConnection connection = null;
        try {
            connection = template.getConnectionFactory().getConnection();
            while (true) {
                long now = System.currentTimeMillis();//当前时间
                long lockTimeout = now + timeout;
                Boolean isLock = connection.setNX(key.getBytes(), String.valueOf(lockTimeout).getBytes());
                if (isLock) {
                    template.expire(key, timeout, TimeUnit.MILLISECONDS);//设置过期时间
                    break;
                }
                LOGGER.info("线程[{}]没拿到锁，等待中。。。"+ Thread.currentThread().getId());
                Thread.sleep(100);
            }
        } catch (Exception e) {
            LOGGER.error("获取同步锁[{}]失败！", key, e);
            result = false;
        } finally {
            if (null != connection) {
                connection.close();
            }
        }
        return result;
    }

    /**
     * 获取自定义时长同步锁
     *
     * @param timeout key过期时间，单位：ms
     * @author CYF
     * @Date 2019年1月12日
     */
    public boolean getCustLock(String key, long timeout) {
        boolean result = false;
        try {
            result = template.opsForValue().setIfAbsent(key, timeout + "", timeout, TimeUnit.MILLISECONDS);
//    		LOGGER.info("key={} 获取同步锁方法返回结果：{}", key, result);
//			if (result) {
//				template.expire(key, timeout, TimeUnit.MILLISECONDS);
//			}
        } catch (Exception e) {
            LOGGER.error("获取同步锁[{}]失败！", key, e);
            result = false;
        }
        return result;
    }

    /**
     * 获取自定义时长同步锁,一直循环直到获取到锁
     *
     * @param timeout key过期时间，单位：ms
     * @author CYF
     * @Date 2019年1月12日
     */
    public boolean getCustLockStill(String key, long timeout) {
        boolean custLock = getCustLock(key, timeout);
        while (!custLock) {
            custLock = getCustLock(key, timeout);
        }
        return custLock;
    }

    /**
     * 释放锁
     */
    public void unlock(String key) {
        RedisConnection connection = null;
        try {
            connection = template.getConnectionFactory().getConnection();
            connection.del(key.getBytes());
        } catch (Exception e) {
            LOGGER.error("释放同步锁[{}]失败！", key, e);
        } finally {
            if (null != connection) {
                connection.close();
            }
        }
    }

    /**
     * 获取定时任务同步锁
     *
     * @param timeout key过期时间，单位：ms
     * @author CYF
     * @Date 2019年1月12日
     */
    public boolean getTaskLock(String key, long timeout) {
        boolean result = false;
        try {
            result = template.opsForValue().setIfAbsent(key, timeout + "", timeout, TimeUnit.MILLISECONDS);
            LOGGER.info("获取同步锁方法返回结果：{}", result);
        } catch (Exception e) {
            LOGGER.error("获取同步锁[{}]失败！", key, e);
            result = false;
        }
        return result;
    }


    /**
     * 获取当前锁状态
     *
     * @param key
     * @return boolean 返回 true 表示获取成功
     */
    public boolean getCurrentLockState(String key) {
        boolean result = false;
        RedisConnection connection = null;
        try {
            long now = System.currentTimeMillis();// 当前时间
            long lockTimeout = now + 2000;
            connection = template.getConnectionFactory().getConnection();
            Boolean isLock = connection.setNX(key.getBytes(), String.valueOf(lockTimeout).getBytes());
            if (isLock) {
                template.expire(key, 2000L, TimeUnit.MILLISECONDS);// 设置过期时间
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("获取同步锁[{}]失败！", key, e);
            result = false;
        } finally {
            if (null != connection) {
                connection.close();
            }
        }
        return result;
    }

    /**
     * 修改商品库存
     *
     * @param keys
     */
    public List<Object> updateGoodNumMulti(long num, String... keys) {
        List<Object> txResults = template.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                for (String key : keys) {
                    operations.boundValueOps(key).increment(num);
                }
                return operations.exec();
            }
        });
        LOGGER.info("out updateGoodNumMulti() num={} result={}", num, txResults);
        return txResults;
    }
}
