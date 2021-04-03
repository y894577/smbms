package com.test.dao.user;

import com.test.dao.user.UserDao;
import com.test.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserRedisDao {

    boolean add(User keyId) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    boolean add(List<User> list) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    void delete(String key);

    void delete(List<String> keys);

    boolean update(User user);

    boolean update(List<User> list);

    User get(String keyID);


}
