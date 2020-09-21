package com.test.service.provider;

import com.mysql.cj.util.StringUtils;
import com.test.dao.BaseDao;
import com.test.dao.bill.BillDao;
import com.test.dao.provider.ProviderDao;
import com.test.pojo.Provider;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class ProviderServiceImpl implements ProviderService {
    private SqlSession sqlSession = null;
    private ProviderDao providerMapper = null;
    private BillDao billMapper = null;

    public ProviderServiceImpl() {

    }

    public List<Provider> getProviderListByCodeAndName(String proCode, String proName) {
        sqlSession = BaseDao.getSqlSession();
        providerMapper = sqlSession.getMapper(ProviderDao.class);
        List<Provider> providerList = new ArrayList<Provider>();
        if (!StringUtils.isNullOrEmpty(proCode)) {
            proCode = "%" + proCode + "%";
        }
        if (!StringUtils.isNullOrEmpty(proName)) {
            proName = "%" + proName + "%";
        }
        providerList = providerMapper.getProviderListByCodeAndName(proCode, proName);
        sqlSession.close();
        return providerList;
    }

    public Provider getProviderById(int id) {
        sqlSession = BaseDao.getSqlSession();
        providerMapper = sqlSession.getMapper(ProviderDao.class);
        Provider provider = providerMapper.getProviderById(id);
        sqlSession.close();
        return provider;
    }

    public boolean updateProvider(Provider provider) {
        sqlSession = BaseDao.getSqlSession();
        providerMapper = sqlSession.getMapper(ProviderDao.class);
        boolean isUpdate = false;
        int i = providerMapper.updateProvider(provider);
        if (i > 0) {
            isUpdate = true;
        }
        sqlSession.commit();
        sqlSession.close();
        return isUpdate;
    }

    /**
     * @param proid
     * @return -1->fail , 0->success , other number->billCount
     */
    public int deleteProvider(int proid) {
        sqlSession = BaseDao.getSqlSession();
        billMapper = sqlSession.getMapper(BillDao.class);
        providerMapper = sqlSession.getMapper(ProviderDao.class);
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

}
