package com.test.service.provider;

import com.test.dao.BaseDao;
import com.test.dao.provider.ProviderDao;
import com.test.dao.provider.ProviderDaoImpl;
import com.test.dao.user.UserDao;
import com.test.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderServiceImpl implements ProviderService {
    private ProviderDao providerDao;

    public ProviderServiceImpl() {
        this.providerDao = new ProviderDaoImpl();
    }

    public List<Provider> getProviderListByCodeAndName(String proCode, String proName) {
        List<Provider> providerList = new ArrayList<Provider>();
        try {
            Connection connection = BaseDao.getConnection();
            if (connection != null) {
                providerList = providerDao.getProviderListByCodeAndName(connection, proCode, proName);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return providerList;
    }
}
