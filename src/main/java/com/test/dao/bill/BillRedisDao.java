package com.test.dao.bill;

import com.test.pojo.Bill;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

public interface BillRedisDao {
    boolean sadd(String key, Bill bill);

    boolean sadd(String setName, String attribute, List<Bill> bills) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    Set<Object> smembers(String setName);

    Set<Object> sinter(String key1, String key2);

    boolean hset(Bill bill) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    boolean hmset(List<Bill> bills) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    Bill hget(String id);

    boolean hexists(String id);

}
