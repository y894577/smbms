package com.test.dao.role;

import com.test.pojo.Role;
import com.test.pojo.User;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

public interface RoleRedisDao {
    boolean add(Role keyId) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    boolean add(List<Role> list) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    //获取角色列表
    Set<Role> zget();
}
