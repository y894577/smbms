package com.test.service.provider;

import com.test.pojo.Provider;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface ProviderService {
    Map<String,Object> getProviderListByCodeAndName(String proCode, String proName, int currentPageNo, int pageSize) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    Provider getProviderById(int id);

    boolean updateProvider(Provider provider);

    int deleteProvider(int proid);

    boolean addProvider(Provider provider);

    int getProviderCount(String queryProName, String queryProCode);

}
