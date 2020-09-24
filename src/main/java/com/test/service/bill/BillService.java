package com.test.service.bill;

import com.test.pojo.Bill;

import java.util.List;

public interface BillService {
    List<Bill> getBillList(String productName, String providerId, String isPayment);

    Bill getBillById(String billid);

    boolean updateBill(Bill bill);
}
