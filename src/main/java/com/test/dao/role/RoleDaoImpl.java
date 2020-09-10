package com.test.dao.role;

import com.test.dao.BaseDao;
import com.test.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    public List<Role> getRoleList(Connection connection) {
        ResultSet resultSet = null;
        List<Role> roleList = new ArrayList<Role>();
        Role role = null;
        if (connection != null) {
            Object[] params = {};
            String sql = "select * from smbms_role";
            try {
                resultSet = BaseDao.execute(connection, sql, params);
                while (resultSet.next()) {
                    role = new Role();
                    role.setId(resultSet.getInt("id"));
                    role.setRoleCode(resultSet.getString("roleCode"));
                    role.setRoleName(resultSet.getString("roleName"));
                    roleList.add(role);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return roleList;
    }
}
