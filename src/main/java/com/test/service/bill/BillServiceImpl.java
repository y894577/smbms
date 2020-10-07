package com.test.service.bill;

import com.mysql.cj.util.StringUtils;
import com.test.dao.BaseDao;
import com.test.dao.bill.BillDao;
import com.test.dao.provider.ProviderDao;
import com.test.pojo.Bill;
import com.test.pojo.Provider;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

public class BillServiceImpl implements BillService {
    private SqlSessionTemplate sqlSession = null;
    private BillDao billMapper = null;
    private ProviderDao providerMapper = null;

    public BillServiceImpl() {
    }

    public void setSqlSession(SqlSessionTemplate sqlSession){
        this.sqlSession = sqlSession;
        this.billMapper = sqlSession.getMapper(BillDao.class);
        this.providerMapper = sqlSession.getMapper(ProviderDao.class);
    }

    public List<Bill> getBillList(String productName, String queryProviderId, String queryIsPayment) {
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
        return billList;
    }

    public Bill getBillById(String billid) {
        int id = 0;
        if (!StringUtils.isNullOrEmpty(billid)) {
            id = Integer.parseInt(billid);
        }
        Bill bill = billMapper.getBillById(id);
        return bill;
    }

    public boolean updateBill(Bill bill){
        boolean isUpdate = false;
        billMapper = sqlSession.getMapper(BillDao.class);
        int i = billMapper.updateBill(bill);
        if (i > 0) {
            isUpdate = true;
        }
        return isUpdate;
    }
}
