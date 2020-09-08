package com.test.dao.role;

import com.test.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleDao {
    //获取角色列表
    public List<Role> getRoleList(Connection connection);
}
