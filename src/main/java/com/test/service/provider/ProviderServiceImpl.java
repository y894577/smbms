package com.test.service.provider;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import com.test.dao.bill.BillDao;
import com.test.dao.provider.ProviderDao;
import com.test.pojo.Provider;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.ArrayList;
import java.util.List;

public class ProviderServiceImpl implements ProviderService {
    private SqlSessionTemplate sqlSession = null;
    private ProviderDao providerMapper = null;
    private BillDao billMapper = null;

    public ProviderServiceImpl() {
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
        this.providerMapper = sqlSession.getMapper(ProviderDao.class);
        this.billMapper = sqlSession.getMapper(BillDao.class);
    }


    public List<Provider> getProviderListByCodeAndName(String proCode, String proName, int currentPageNo, int pageSize) {
        if (!StringUtils.isNullOrEmpty(proCode)) {
            proCode = "%" + proCode + "%";
        }
        if (!StringUtils.isNullOrEmpty(proName)) {
            proName = "%" + proName + "%";
        }
        PageHelper.startPage(currentPageNo, pageSize);
        List<Provider> providerList = providerMapper.getProviderListByCodeAndName(proCode, proName);
        PageInfo<Provider> pageInfo = new PageInfo<Provider>(providerList);
        return pageInfo.getList();
    }

    public Provider getProviderById(int id) {
        Provider provider = providerMapper.getProviderById(id);
        return provider;
    }

    public boolean updateProvider(Provider provider) {
        boolean isUpdate = false;
        int i = providerMapper.updateProvider(provider);
        if (i > 0) {
            isUpdate = true;
        }
        sqlSession.commit();
        return isUpdate;
    }

    /**
     * @param proid
     * @return -1->fail , 0->success , other number->billCount
     */
    public int deleteProvider(int proid) {
        int billCount = billMapper.getBillCountByProId(proid);
        if (billCount == 0) {
            int i = providerMapper.deleteProvider(proid);
            if (i > 0) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return billCount;
        }

    }

    public boolean addProvider(Provider provider) {
        boolean isAdd = false;
        int i = providerMapper.addProvider(provider);
        if (i > 0) {
            isAdd = true;
        }
        return isAdd;
    }

    public int getProviderCount(String queryProName, String queryProCode) {
        return providerMapper.getProviderCount(queryProName, queryProCode);
    }

}
