package com.test.controller.bill;

import com.test.pojo.Bill;
import com.test.pojo.Provider;
import com.test.service.bill.BillService;
import com.test.service.provider.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping("/jsp/bill.do")
public class BillController {

    @Autowired
    @Qualifier("BillServiceImpl")
    private BillService billService;


    @RequestMapping(params = "method=query")
    private String queryBill(HttpServletRequest req){
        String productName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        List<Bill> billList = billService.getBillList(productName, queryProviderId, queryIsPayment);
        req.setAttribute("billList", billList);
        return "billlist";
    }

    


}
