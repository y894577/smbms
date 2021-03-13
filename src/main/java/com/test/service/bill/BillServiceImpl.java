package com.test.service.bill;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import com.test.dao.bill.BillDao;
import com.test.pojo.Bill;
import com.test.util.Constant;
import com.test.util.PageSupport;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillServiceImpl implements BillService {
    private SqlSession sqlSession = null;
    private BillDao billMapper = null;

    public BillServiceImpl() {
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
        this.billMapper = sqlSession.getMapper(BillDao.class);
    }

    public Map<String, Object> getBillList(String productName, String queryProviderId, String queryIsPayment, String currentPageNo) {
        Map<String, Object> result = new HashMap();

        result.put("queryProviderId", queryProviderId);
        result.put("queryProductName", productName);
        result.put("queryIsPayment", queryIsPayment);

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


        final int finalIsPayment = isPayment;
        final int finalProviderId = providerId;
        final String finalProductName = productName;
        PageInfo<Bill> pageInfo = PageHelper.startPage(0, Integer.MAX_VALUE - 1).doSelectPageInfo(new ISelect() {
            public void doSelect() {
                billMapper.getBillList(finalProductName, finalProviderId, finalIsPayment);
            }
        });


        int pageNo = currentPageNo == null || currentPageNo.equals("") ? 1 : Integer.parseInt(currentPageNo);
        //获取总条数
        int totalCount = (int) pageInfo.getTotal();
        //获取总页数
        int totalPageCount = (int) Math.ceil(totalCount * 1.0 / Constant.PAGESIZE);
        pageNo = totalCount == 0 ? 0 : pageNo;


        pageInfo = PageHelper.startPage(pageNo, Constant.PAGESIZE).doSelectPageInfo(new ISelect() {
            public void doSelect() {
                billMapper.getBillList(finalProductName, finalProviderId, finalIsPayment);
            }
        });


        result.put("billList", pageInfo.getList());
        result.put("totalCount", totalCount);
        result.put("currentPageNo", pageNo);
        result.put("totalPageCount", totalPageCount);

        return result;


    }

    public Bill getBillById(String billid) {
        int id = 0;
        if (!StringUtils.isNullOrEmpty(billid)) {
            id = Integer.parseInt(billid);
        }
        return billMapper.getBillById(id);
    }

    public boolean updateBill(Bill bill) {
        int i = billMapper.updateBill(bill);
        return i > 0;
    }

    public boolean deleteBill(String billID) {
        int i = billMapper.deleteBill(billID);
        return i > 0;
    }

    public boolean addBill(Bill bill) {
        int i = billMapper.insertBill(bill);
        return i > 0;
    }
}
