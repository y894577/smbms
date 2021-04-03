package com.test.dao.user;

import com.test.pojo.User;
import com.test.util.SerializableUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import sun.util.resources.LocaleData;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRedisDaoImpl implements UserRedisDao {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    String prefix = "loginUser:";

    /**
     * 新增
     *
     * @param user
     * @return
     */
    @Override
    public boolean add(final User user) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String userHash = prefix + user.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        Class<? extends User> userClass = user.getClass();
        for (Field field : userClass.getDeclaredFields()) {
            StringBuffer sb = new StringBuffer();
            sb.append("get");
            sb.append(field.getName().substring(0, 1).toUpperCase());
            sb.append(field.getName().substring(1));
            Method method = userClass.getDeclaredMethod(sb.toString());
            Object o = method.invoke(user);
            if (o == null)
                continue;
            else if (o.getClass() == LocalDateTime.class)
                map.put(field.getName(), o.toString());
            else
                map.put(field.getName(), o);
        }
        redisTemplate.opsForHash().putAll(userHash, map);
        return true;
    }

    @Override
    public boolean add(final List<User> list) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        for (User user : list) {
            add(user);
        }
        return true;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public boolean update(final User user) {
        String key = user.getId().toString();

        if (get(key) == null) {
            throw new NullPointerException("数据行不存在, key = " + key);
        }
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key = serializer.serialize(user.getId().toString());
                byte[] name = serializer.serialize(user.getUserName());
                connection.set(key, name);
                return true;
            }
        });
        return result;
    }

    @Override
    public boolean update(List<User> key) {
        return false;
    }

    @Override
    public User get(String keyID) {
        redisTemplate.opsForHash().scan(keyID, ScanOptions.scanOptions().build());
        return null;
    }
}
