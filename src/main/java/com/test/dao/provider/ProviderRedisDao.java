package com.test.dao.provider;

import com.test.pojo.Provider;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

public interface ProviderRedisDao {
    boolean add(Provider provider);

    boolean zadd(Provider provider);

    boolean zadd(Set<Provider> providerList);

    boolean sadd(String key, Provider provider);

    boolean sadd(String key, List<Provider> providers) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    boolean hset(Provider provider) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    boolean hmset(List<Provider> providers) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    boolean hexists (String id);

    Provider hget(String id);
}
