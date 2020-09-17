package com.test.service.provider;

import com.mysql.cj.util.StringUtils;
import com.test.dao.BaseDao;
import com.test.dao.provider.ProviderDao;
import com.test.pojo.Provider;
import org.apache.ibatis.session.SqlSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderServiceImpl implements ProviderService {
    private SqlSession sqlSession = null;
    private ProviderDao mapper = null;

    public ProviderServiceImpl() {

    }

    public List<Provider> getProviderListByCodeAndName(String proCode, String proName) {
        sqlSession = BaseDao.getSqlSession();
        mapper = sqlSession.getMapper(ProviderDao.class);
        List<Provider> providerList = new ArrayList<Provider>();
        if (!StringUtils.isNullOrEmpty(proCode)) {
            proCode = "%" + proCode + "%";
        }
        if (!StringUtils.isNullOrEmpty(proName)) {
            proName = "%" + proName + "%";
        }
        providerList = mapper.getProviderListByCodeAndName(proCode, proName);
        sqlSession.close();
        return providerList;
    }
}
