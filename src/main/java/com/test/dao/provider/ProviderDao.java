package com.test.dao.provider;

import com.test.pojo.Provider;

import java.sql.Connection;
import java.util.List;

public interface ProviderDao {
    public List<Provider> getProviderListByCodeAndName(Connection connection, String proCode, String proName);
}
