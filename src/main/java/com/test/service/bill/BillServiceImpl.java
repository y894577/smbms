package com.test.service.bill;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import com.test.dao.bill.BillDao;
import com.test.dao.bill.BillRedisDao;
import com.test.pojo.Bill;
import com.test.util.Constant;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BillServiceImpl implements BillService {
    private SqlSession sqlSession = null;
    private BillDao billMapper = null;

    static volatile boolean isLoad = false;

    @Autowired
    BillRedisDao billRedisDao;

    public BillServiceImpl() {
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
        this.billMapper = sqlSession.getMapper(BillDao.class);
    }

    public synchronized void getBillListToRedis() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!isLoad) {
            List<Bill> billList = billMapper.getBillList(null, 0, 0);
            billRedisDao.hmset(billList);
            billRedisDao.sadd("bill:", "providerId", billList);
            billRedisDao.sadd("bill:", "isPayment", billList);
            isLoad = true;
        }
    }

    public Map<String, Object> getBillList(String productName, String queryProviderId, String queryIsPayment, String currentPageNo) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!isLoad)
            getBillListToRedis();

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

        if (StringUtils.isNullOrEmpty(productName)) {
            String prefix = "bill:";
            List<Bill> bills = new ArrayList<>();
            if (providerId != 0 && isPayment != 0) {
                Set<Object> set = billRedisDao.sinter(prefix + "providerId:" + providerId, prefix + "isPayment:" + isPayment);
                for (Object o : set) {
                    Bill bill = billRedisDao.hget(o.toString());
                    bills.add(bill);
                }
                result.put("billList", bills);
            }
            else if (providerId != 0) {
                Set<Object> set = billRedisDao.smembers(prefix + "providerId:" + queryProviderId);
                for (Object o : set) {
                    Bill bill = billRedisDao.hget(String.valueOf(o));
                    if (bill != null)
                        bills.add(bill);
                }
            } else if (isPayment != 0) {
                Set<Object> set = billRedisDao.smembers(prefix + "isPayment:" + queryIsPayment);
                for (Object o : set) {
                    Bill bill = billRedisDao.hget(String.valueOf(o));
                    if (bill != null)
                        bills.add(bill);
                }
            } else {
                for (int i = 2; i <= 6; i++) {
                    Bill bill = billRedisDao.hget(String.valueOf(i));
                    bills.add(bill);
                }
            }
            result.put("billList", bills);
        } else {
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

            // 加入redis缓存
            billRedisDao.hmset(pageInfo.getList());
            billRedisDao.sadd("bill:", "providerId", pageInfo.getList());
            billRedisDao.sadd("bill:", "isPayment", pageInfo.getList());
        }


        return result;


    }

    public Bill getBillById(String billid) {
        if (billRedisDao.hexists(billid))
            return billRedisDao.hget(billid);
        else {
            int id = 0;
            if (!StringUtils.isNullOrEmpty(billid)) {
                id = Integer.parseInt(billid);
            }
            return billMapper.getBillById(id);
        }
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
