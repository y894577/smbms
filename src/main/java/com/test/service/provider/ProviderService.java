package com.test.service.provider;

import com.test.pojo.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderService {
    List<Provider> getProviderListByCodeAndName(String proCode, String proName);

    Provider getProviderById(int id);

    boolean updateProvider(Provider provider);

    int deleteProvider(int proid);

    boolean addProvider(Provider provider);

}
