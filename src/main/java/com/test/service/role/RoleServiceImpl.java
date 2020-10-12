package com.test.service.role;

import com.test.dao.role.RoleDao;
import com.test.pojo.Role;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    private SqlSessionTemplate sqlSession = null;
    private RoleDao roleMapper = null;

    public RoleServiceImpl() {
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
        roleMapper = sqlSession.getMapper(RoleDao.class);
    }


    public List<Role> getRoleList() {
        List<Role> roleList = null;
        roleList = roleMapper.getRoleList();
        return roleList;
    }


    @Test
    public void test() {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        RoleServiceImpl roleService = (RoleServiceImpl) context.getBean("RoleServiceImpl");
        System.out.println(roleService.getRoleList().size());
    }
}
