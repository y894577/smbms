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

@Controller
@RequestMapping("/jsp/bill.do")
public class BillController {

    @Autowired
    @Qualifier("BillServiceImpl")
    private BillService billService;


    @RequestMapping(params = "method=query")
    private String queryBill(HttpServletRequest req,Model model) {
        String productName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        String tempCurrentPageNo = req.getParameter("pageIndex");

        List<Bill> billList = billService.getBillList(productName, queryProviderId, queryIsPayment);

        int pageSize = Constant.PAGESIZE;
        int currentPageNo = tempCurrentPageNo == null ? 1 : Integer.parseInt(tempCurrentPageNo);

        int totalCount = billList.size();

        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        //使用工具类获取总页数
        int totalPageCount = pageSupport.getTotalPageCount();

        if (currentPageNo < 1) {
            //如果页面小于1，则显示第一页的东西
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            //如果页面大于当前页面，则显示最后一页
            currentPageNo = totalPageCount;
        }

        PageHelper.startPage(currentPageNo, pageSize);

        PageInfo<Bill> pageInfo = new PageInfo<Bill>(billList);

        model.addAttribute("billList", pageInfo.getList());
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryProductName", productName);
        model.addAttribute("queryIsPayment", queryIsPayment);


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
