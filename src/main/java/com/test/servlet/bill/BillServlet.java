package com.test.servlet.bill;

import com.mysql.cj.util.StringUtils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.test.pojo.Bill;
import com.test.pojo.Provider;
import com.test.service.bill.BillService;
import com.test.service.bill.BillServiceImpl;
import com.test.service.provider.ProviderService;
import com.test.service.provider.ProviderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("query")) {
            this.query(req, resp);
        } else if (method.equals("view")) {
            this.getBillView(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BillService billService = new BillServiceImpl();
        ProviderService providerService = new ProviderServiceImpl();
        List<Provider> providerList = providerService.getProviderListByCodeAndName(null, null);

        Enumeration enu = req.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            System.out.println(paraName + ": " + req.getParameter(paraName));
        }

        String productName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        List<Bill> billList = billService.getBillList(productName, queryProviderId, queryIsPayment);
        req.setAttribute("billList", billList);
        req.setAttribute("providerList", providerList);
        req.getRequestDispatcher("billlist.jsp").forward(req, resp);
    }

    private void getBillView(HttpServletRequest req, HttpServletResponse resp) {
        String billid = req.getParameter("billid");
    }
}
