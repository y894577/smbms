package com.test.dao.bill;

import com.test.pojo.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillDao {
    int getBillCountByProId(@Param("proid") int proid);

    List<Bill> getBillList(@Param("productName") String productName,
                           @Param("providerId") Integer providerId,
                           @Param("isPayment") Integer isPayment);

    Bill getBillById(@Param("id") int id);

    int updateBill(Bill bill);

    int addBill(Bill bill);

    int getBillCount(@Param("productName") String productName,
                     @Param("proId") String proId,
                     @Param("isPayment") boolean isPayment);

}
