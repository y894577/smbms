package com.test.servlet.provider;

import com.test.pojo.Provider;
import com.test.service.provider.ProviderService;
import com.test.service.provider.ProviderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAKey;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("query")) {
            this.query(req, resp);

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    public void query(HttpServletRequest req, HttpServletResponse resp){
        String queryProCode = req.getParameter("queryProCode");
        String queryProName = req.getParameter("queryProName");
        ProviderService providerService = new ProviderServiceImpl();
        List<Provider> providerList = providerService.getProviderListByCodeAndName(queryProCode, queryProName);
        req.setAttribute("providerList", providerList);
        try {
            req.getRequestDispatcher("providerlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
