package com.test.service.role;

import com.test.dao.BaseDao;
import com.test.dao.provider.ProviderDao;
import com.test.dao.role.RoleDao;
import com.test.dao.role.RoleDaoImpl;
import com.test.dao.user.UserDao;
import com.test.pojo.Role;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    private SqlSession sqlSession = null;
    private RoleDao mapper = null;

    public RoleServiceImpl() {

    }

    public List<Role> getRoleList() {
        sqlSession = BaseDao.getSqlSession();
        mapper = sqlSession.getMapper(RoleDao.class);
        List<Role> roleList = null;
        roleList = mapper.getRoleList();
        sqlSession.close();
        return roleList;
    }

    @Test
    public void test() {
        RoleServiceImpl roleService = new RoleServiceImpl();
        System.out.println(roleService.getRoleList().size());
    }
}
