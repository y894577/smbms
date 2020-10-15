package com.test.servlet.bill;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.test.dao.bill.BillDao;
import com.test.pojo.Bill;
import com.test.pojo.Provider;
import com.test.pojo.User;
import com.test.service.bill.BillService;
import com.test.service.bill.BillServiceImpl;
import com.test.service.provider.ProviderService;
import com.test.service.provider.ProviderServiceImpl;
import com.test.util.Constant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class BillServlet extends HttpServlet {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    BillServiceImpl billService = (BillServiceImpl) context.getBean("BillServiceImpl");
    ProviderServiceImpl providerService = (ProviderServiceImpl) context.getBean("ProviderServiceImpl");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("query")) {
            this.query(req, resp);
        } else if (method.equals("view")) {
            this.getBillView(req, resp, "billview.jsp");
        } else if (method.equals("modify")) {
            this.getBillView(req, resp, "billmodify.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("modifysave")) {
            this.modify(req, resp);
        }
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Enumeration enu = req.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            System.out.println(paraName + ": " + req.getParameter(paraName));
        }

        String productName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
//        List<Bill> billList = billService.getBillList(productName, queryProviderId, queryIsPayment);
//        req.setAttribute("billList", billList);
        req.getRequestDispatcher("billlist.jsp").forward(req, resp);
    }

    private void getBillView(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
        String billid = req.getParameter("billid");
        Bill bill = billService.getBillById(billid);
        req.setAttribute("bill", bill);

        req.getRequestDispatcher(url).forward(req, resp);
    }


    public void modify(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setId(Integer.parseInt(id));
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount));
        bill.setTotalPrice(new BigDecimal(totalPrice));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setModifyBy(((User) req.getSession().getAttribute(Constant.USER_SESSION)).getId());
        bill.setModifyDate(new Date());

        boolean isUpdate = billService.updateBill(bill);

        if (isUpdate) {
            resp.sendRedirect(req.getContextPath() + "/jsp/bill.do?method=query");
        } else {
            req.getRequestDispatcher("billmodify.jsp").forward(req, resp);
        }
    }
}
