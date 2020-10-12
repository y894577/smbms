package com.test.service.bill;

import com.mysql.cj.util.StringUtils;
import com.test.dao.bill.BillDao;
import com.test.pojo.Bill;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class BillServiceImpl implements BillService {
    private SqlSession sqlSession = null;
    private BillDao billMapper = null;

    public BillServiceImpl() {
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
        this.billMapper = sqlSession.getMapper(BillDao.class);
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

    public boolean updateBill(Bill bill) {
        boolean isUpdate = false;
        int i = sqlSession.update("com.test.dao.bill.BillDao.updateBill", bill);
        if (i > 0) {
            isUpdate = true;
        }
        return isUpdate;
    }

    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BillServiceImpl billService = (BillServiceImpl) context.getBean("BillServiceImpl");
        System.out.println(billService.getBillById("1"));
        System.out.println(billService.getBillList("", "1", "0"));
    }
}
