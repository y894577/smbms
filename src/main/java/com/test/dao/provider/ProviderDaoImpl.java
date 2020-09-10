package com.test.dao.provider;

import com.mysql.jdbc.StringUtils;
import com.test.dao.BaseDao;
import com.test.pojo.Provider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderDaoImpl implements ProviderDao {

    public List<Provider> getProviderListByCodeAndName(Connection connection, String proCode, String proName) {
        List<Provider> providerList = new ArrayList<Provider>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            List<Object> list = new ArrayList<Object>();

            sql.append("select * from smbms_provider where 1=1 ");
            if (!StringUtils.isNullOrEmpty(proName)) {
                list.add("%" + proName + "%");
                sql.append("and proName like ? ");
            }
            if (!StringUtils.isNullOrEmpty(proCode)) {
                list.add("%" + proCode + "%");
                sql.append("and proCode like ? ");
            }
            try {
                ResultSet resultSet = BaseDao.execute(connection, sql.toString(), list.toArray());
                while (resultSet.next()) {
                    Provider provider = new Provider();
                    provider.setId(resultSet.getInt("id"));
                    provider.setProCode(resultSet.getString("proCode"));
                    provider.setProName(resultSet.getString("proName"));
                    provider.setProDesc(resultSet.getString("proDesc"));
                    provider.setProContact(resultSet.getString("proContact"));
                    provider.setProPhone(resultSet.getString("proPhone"));
                    provider.setProAddress(resultSet.getString("proAddress"));
                    provider.setProFax(resultSet.getString("proFax"));
                    provider.setCreationDate(resultSet.getTimestamp("creationDate"));
                    providerList.add(provider);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return providerList;
    }
}
