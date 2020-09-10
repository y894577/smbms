package com.test.service.provider;

import com.test.pojo.Provider;

import java.util.List;

public interface ProviderService {
    public List<Provider> getProviderListByCodeAndName(String proCode, String proName);
}
