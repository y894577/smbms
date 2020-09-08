package com.test.service.role;

import com.test.dao.BaseDao;
import com.test.dao.role.RoleDao;
import com.test.dao.role.RoleDaoImpl;
import com.test.dao.user.UserDao;
import com.test.pojo.Role;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao;

    public RoleServiceImpl() {
        this.roleDao = new RoleDaoImpl();
    }

    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> roleList = null;
        try {
            connection = BaseDao.getConnection();
            if (connection != null) {
                roleList = roleDao.getRoleList(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return roleList;
    }

    @Test
    public void test() {
        RoleServiceImpl roleService = new RoleServiceImpl();
        System.out.println(roleService.getRoleList().size());
    }
}
