package com.test.service.bill;

import com.mysql.cj.util.StringUtils;
import com.test.dao.BaseDao;
import com.test.dao.bill.BillDao;
import com.test.dao.provider.ProviderDao;
import com.test.pojo.Bill;
import com.test.pojo.Provider;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class BillServiceImpl implements BillService {
    private SqlSession sqlSession = null;
    private BillDao billMapper = null;
    private ProviderDao providerMapper = null;

    public BillServiceImpl() {
    }

    public List<Bill> getBillList(String productName, String queryProviderId, String queryIsPayment) {
        sqlSession = BaseDao.getSqlSession();
        billMapper = sqlSession.getMapper(BillDao.class);
        providerMapper = sqlSession.getMapper(ProviderDao.class);

        int providerId = 0;
        int isPayment = 0;
        if (!StringUtils.isNullOrEmpty(queryProviderId)) {
            providerId = Integer.parseInt(queryProviderId);
        }
        if (!StringUtils.isNullOrEmpty(queryIsPayment)) {
            isPayment = Integer.parseInt(queryIsPayment);
        }

        List<Provider> providerList = providerMapper.getProviderListByCodeAndName(null, null);
        if (!StringUtils.isNullOrEmpty(productName)) {
            productName = "%" + productName + "%";
        }
        List<Bill> billList = billMapper.getBillList(productName, providerId, isPayment);
        sqlSession.close();
        return billList;
    }
}
