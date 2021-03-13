package com.test.dao.user;

import com.test.pojo.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

public class UserRedisDaoImpl implements UserRedisDao {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 新增
     * @param user
     * @return
     */
    @Override
    public boolean add(final User user) {

            boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {

                    RedisSerializer<String>serializer = redisTemplate.getStringSerializer();
                    byte[]key = serializer.serialize(user.getId().toString());
                    byte[]name = serializer.serialize(user.getUserName());
                    return redisConnection.setNX(key,name);
                }
            });

        return result;
    }

    @Override
    public boolean add(final List<User> list){
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String>serializer = redisTemplate.getStringSerializer();
                for (User user : list) {
                    byte[]key = serializer.serialize(user.getId().toString());
                    byte[]name = serializer.serialize(user.getUserName());
                    redisConnection.setNX(key,name);
                }
                return true;
            }
        },false,true);
        return result;
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
                byte[] key  = serializer.serialize(user.getId().toString());
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
        return null;
    }
}
