package com.test.dao.bill;

import com.alibaba.fastjson.JSON;
import com.test.pojo.Bill;
import com.test.pojo.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

public class BillRedisDaoImpl implements BillRedisDao {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean sadd(String setName, Bill bill) {
        redisTemplate.opsForSet().add(setName, bill.getId().toString());
        return true;
    }

    @Override
    public boolean sadd(String setName, String attribute, List<Bill> bills) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<Bill> billClass = Bill.class;
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(attribute.substring(0, 1).toUpperCase());
        sb.append(attribute.substring(1));
        Method method = billClass.getMethod(sb.toString());
        for (Bill bill : bills) {
            sadd(setName + attribute + ":" + method.invoke(bill).toString(), bill);
        }
        return false;
    }

    @Override
    public Set<Object> smembers(String setName) {
        return redisTemplate.opsForSet().members(setName);
    }

    @Override
    public Set<Object> sinter(String key1, String key2) {
        System.out.println(key1 + " " + key2);
        Set<Object> set = redisTemplate.opsForSet().intersect(key1, key2);
        return set;
    }

    @Override
    public boolean hset(Bill bill) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String billHash = "bill:" + bill.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        Class<? extends Bill> billClass = bill.getClass();
        for (Field field : billClass.getDeclaredFields()) {
            StringBuffer sb = new StringBuffer();
            sb.append("get");
            sb.append(field.getName().substring(0, 1).toUpperCase());
            sb.append(field.getName().substring(1));
            Method method = billClass.getDeclaredMethod(sb.toString());
            Object o = method.invoke(bill);
            if (o == null)
                continue;
            else if (o.getClass() == LocalDateTime.class)
                map.put(field.getName(), o.toString());
            else
                map.put(field.getName(), o);
        }
        redisTemplate.opsForHash().putAll(billHash, map);
        return true;
    }

    @Override
    public boolean hmset(List<Bill> bills) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (Bill bill : bills) {
            hset(bill);
        }
        return false;
    }

    @Override
    public Bill hget(String id) {
        String key = "bill:" + id;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        String s = JSON.toJSONString(entries);
        System.out.println(s);
        Bill bill = JSON.parseObject(s, Bill.class);
        System.out.println(bill.toString());
        return bill;
    }

    @Override
    public boolean hexists(String id) {
        return redisTemplate.hasKey("bill:" + id);
    }
}
