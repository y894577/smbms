package com.test.dao.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.pojo.Provider;
import com.test.pojo.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import redis.clients.jedis.ScanParams;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProviderRedisDaoImpl implements ProviderRedisDao {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * sadd 缓存pro的id:name
     * hset 缓存pro的具体信息
     *
     * @param provider
     * @return
     */
    @Override
    public boolean add(Provider provider) {
        return true;
    }

    @Override
    public boolean zadd(Provider provider) {
        return false;
    }

    @Override
    public boolean hset(Provider provider) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String proHash = "pro:" + provider.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        Class<? extends Provider> proClass = provider.getClass();
        for (Field field : proClass.getDeclaredFields()) {
            StringBuffer sb = new StringBuffer();
            sb.append("get");
            sb.append(field.getName().substring(0, 1).toUpperCase());
            sb.append(field.getName().substring(1));
            Method method = proClass.getDeclaredMethod(sb.toString());
            Object o = method.invoke(provider);
            if (o == null)
                continue;
            else if (o.getClass() == LocalDateTime.class)
                map.put(field.getName(), o.toString());
            else
                map.put(field.getName(), o);
        }
        redisTemplate.opsForHash().putAll(proHash, map);
        return true;
    }

    @Override
    public boolean hmset(List<Provider> providers) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (Provider provider : providers) {
            hset(provider);
        }
        return false;
    }

    @Override
    public boolean hexists(String id) {
        return redisTemplate.hasKey("pro:" + id);
    }

    @Override
    public Provider hget(String id) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries("pro:" + id);
        String s = JSON.toJSONString(entries);
        Provider provider = JSON.parseObject(s, Provider.class);
        return provider;
    }

    @Override
    public boolean hdel(String id) {
        redisTemplate.opsForHash().delete("pro:" + id);
        return false;
    }

    @Override
    public List<Provider> sscan(String prefix) {
        String pattern = "pro:*" + prefix + "*";
        Set<String> keys = redisTemplate.keys(pattern);
        return null;
    }

    @Override
    public boolean srem(String id) {
        redisTemplate.opsForSet().remove("pro:" + id);
        return true;
    }


    @Override
    public boolean sadd(String key, Provider provider) {
        redisTemplate.opsForSet().add(key, String.valueOf(provider.getId()));
        return true;
    }

    @Override
    public boolean sadd(String key, List<Provider> providers) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<Provider> p = Provider.class;
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(key.substring(0, 1).toUpperCase());
        sb.append(key.substring(1));
        Method method = p.getDeclaredMethod(sb.toString());
        for (Provider provider : providers) {
            String hash = "pro:" + method.invoke(provider);
            sadd(hash, provider);
        }
        return false;
    }

    public boolean zrem(Provider provider) {
        return true;
    }

    @Override
    public boolean zadd(Set<Provider> providerSet) {
        for (Provider provider : providerSet) {
            zadd(provider);
        }
        return false;
    }


}
