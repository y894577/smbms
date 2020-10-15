package com.test.controller.bill;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.pojo.Bill;
import com.test.pojo.Provider;
import com.test.pojo.User;
import com.test.service.bill.BillService;
import com.test.service.provider.ProviderService;
import com.test.util.Constant;
import com.test.util.PageSupport;
import org.apache.ibatis.annotations.Param;
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
import java.util.Map;

@Controller
@RequestMapping("/jsp/bill.do")
public class BillController {

    @Autowired
    @Qualifier("BillServiceImpl")
    private BillService billService;


    @RequestMapping(params = "method=query")
    private String queryBill(HttpServletRequest req, Model model) {
        String productName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        String currentPageNo = req.getParameter("pageIndex");

        Map<String, Object> result = billService.getBillList(productName, queryProviderId, queryIsPayment, currentPageNo);

        model.addAllAttributes(result);

        return "billlist";
    }

    @RequestMapping(params = {"method=view"})
    private String getBillView(String billid, String method, Model model) {
        Bill bill = billService.getBillById(billid);
        model.addAttribute("bill", bill);
        return "bill" + method;
    }

    @RequestMapping(params = {"method=modify"})
    private String modifyView(String billid, String method, Model model) {
        return this.getBillView(billid, method, model);
    }
}
