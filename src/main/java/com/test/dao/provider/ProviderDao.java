package com.test.dao.provider;

import com.test.pojo.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderDao {
    List<Provider> getProviderListByCodeAndName(@Param("proCode") String proCode,
                                                @Param("proName") String proName);

    Provider getProviderById(@Param("id") int id);

    int updateProvider(Provider provider);

    int deleteProvider(@Param("id") int id);

    int addProvider(Provider provider);
}
