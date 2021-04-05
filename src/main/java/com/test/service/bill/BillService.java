package com.test.service.bill;

import com.test.pojo.Bill;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface BillService {
    Map<String,Object> getBillList(String productName, String providerId, String isPayment, String currentPageNo) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
    void getBillListToRedis() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    Bill getBillById(String billid);

    boolean updateBill(Bill bill);

    boolean deleteBill(String billID);

    boolean addBill(Bill bill);
}
