package com.test.dao.role;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.pojo.Role;
import com.test.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleRedisDaoImpl implements RoleRedisDao {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    String prefix = "Role:";

    @Override
    public boolean add(final Role role) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String roleHash = prefix + role.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        Class<? extends Role> roleClass = role.getClass();
        for (Field field : roleClass.getDeclaredFields()) {
            StringBuffer sb = new StringBuffer();
            sb.append("get");
            sb.append(field.getName().substring(0, 1).toUpperCase());
            sb.append(field.getName().substring(1));
            Method method = roleClass.getDeclaredMethod(sb.toString());
            Object o = method.invoke(role);
            if (o == null)
                continue;
            else if (o.getClass() == LocalDateTime.class)
                map.put(field.getName(), o.toString());
            else
                map.put(field.getName(), o);
        }
        redisTemplate.opsForSet().add("Role", JSON.toJSONString(map));
        System.out.println(JSON.toJSONString(map));
        return true;
    }

    @Override
    public boolean add(final List<Role> list) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        for (Role role : list) {
            add(role);
        }
        return true;
    }

    @Override
    public Set<Role> zget() {
        Set<Object> result = redisTemplate.opsForSet().members("Role");
        Set<Role> roleSet = result.stream().map(e -> JSON.parseObject(e.toString(), Role.class)).collect(Collectors.toSet());
        return roleSet;
    }
}
