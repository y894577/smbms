package com.test.controller.bill;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Controller
@RequestMapping("/jsp/bill.do")
public class BillController {

    @Autowired
    @Qualifier("BillServiceImpl")
    private BillService billService;


    @RequestMapping(params = "method=query")
    private String queryBill(HttpServletRequest req, Model model) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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

    @RequestMapping(params = {"method=delbill"})
    @ResponseBody
    private String deleteBill(String billid) {
        Map<String, Object> result = new HashMap();
        if (StringUtils.isNullOrEmpty(billid))
            result.put("delResult", "notexist");
        else {
            boolean isDelete = billService.deleteBill(billid);
            result.put("delResult", String.valueOf(isDelete));
        }
        return JSONArray.toJSONString(result);
    }

    @RequestMapping(params = "method=modifysave")
    private String updateBill(Bill bill, HttpSession session, RedirectAttributes attr) {
        bill.setModifyDate(new Date());
        bill.setModifyBy(((User) session.getAttribute(Constant.USER_SESSION)).getId());
        boolean isUpdate = billService.updateBill(bill);
        if (isUpdate) {
            attr.addAttribute("method", "query");
            return "redirect:/jsp/bill.do";
        } else
            return "billmodify";
    }

    @RequestMapping(params = {"method=add"})
    private String addBill(Bill bill, HttpSession session, RedirectAttributes attr) {
        bill.setCreatedBy(((User) session.getAttribute(Constant.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        boolean isAdd = billService.addBill(bill);
        if (isAdd) {
            attr.addAttribute("method", "query");
            return "redirect:/jsp/bill.do";
        } else
            return "billmodify";
    }
}
