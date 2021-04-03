package com.test.service.role;

import com.test.pojo.Role;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public interface RoleService {
    public Set<Role> getRoleList() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}
