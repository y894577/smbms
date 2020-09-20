package com.test.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.test.dao.provider.ProviderDao;
import com.test.dao.provider.ProviderDaoImpl;
import com.test.pojo.Provider;
import com.test.pojo.User;
import com.test.service.provider.ProviderService;
import com.test.service.provider.ProviderServiceImpl;
import com.test.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("query")) {
            this.query(req, resp);
        } else if (method.equals("view")) {
            this.getProviderView(req, resp, "providerview.jsp");
        } else if (method.equals("modify")) {
            this.getProviderView(req, resp, "providermodify.jsp");
        } else if (method.equals("delprovider")) {
            this.deleteProvider(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        Enumeration enu = req.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            System.out.println(paraName + ": " + req.getParameter(paraName));
        }
        if (method.equals("modifyexe")) {
            this.modifyProvider(req, resp);
        }
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) {
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

    public void getProviderView(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
        String proid = req.getParameter("proid");
        ProviderService providerService = new ProviderServiceImpl();
        if (!StringUtils.isNullOrEmpty(proid)) {
            Provider provider = providerService.getProviderById(Integer.parseInt(proid));
            req.setAttribute("provider", provider);
        }
        req.getRequestDispatcher(url).forward(req, resp);
    }

    public void modifyProvider(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        Provider provider = new Provider();
        provider.setId(Integer.parseInt(id));
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProAddress(proAddress);
        provider.setProFax(proFax);
        provider.setProDesc(proDesc);
        provider.setModifyBy(((User) req.getSession().getAttribute(Constant.USER_SESSION)).getId());
        provider.setModifyDate(new Date());

        ProviderService providerService = new ProviderServiceImpl();
        boolean isUpdate = providerService.updateProvider(provider);
        if (isUpdate) {
            resp.sendRedirect(req.getContextPath() + "/jsp/provider.do?method=query");
        } else {
            req.getRequestDispatcher("providermodify.jsp").forward(req, resp);
        }
    }

    public void deleteProvider(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String proid = req.getParameter("proid");
        if (!StringUtils.isNullOrEmpty("proid")) {
            ProviderService providerService = new ProviderServiceImpl();
            Map<String, Object> resultMap = new HashMap<String, Object>();
            int result = providerService.deleteProvider(Integer.parseInt(proid));
            if (result == 0) {
                //删除成功
                resultMap.put("delResult", "true");
            } else if (result == -1) {
                //删除失败
                resultMap.put("delResult", "false");
            } else {
                //存在订单
                resultMap.put("delResult", result);
            }
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        }
    }


}
