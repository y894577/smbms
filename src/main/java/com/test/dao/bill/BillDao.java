package com.test.dao.bill;

import org.apache.ibatis.annotations.Param;

public interface BillDao {
    int getBillCountByProId(@Param("proid") int proid);
}
