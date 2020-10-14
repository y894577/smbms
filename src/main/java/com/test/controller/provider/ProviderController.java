package com.test.controller.provider;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.test.pojo.Provider;
import com.test.pojo.User;
import com.test.service.provider.ProviderService;
import com.test.service.provider.ProviderServiceImpl;
import com.test.util.Constant;
import com.test.util.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/jsp/provider.do")
public class ProviderController {
    @Autowired
    @Qualifier("ProviderServiceImpl")
    private ProviderService providerService;

    @RequestMapping(params = "method=query")
    private String queryProvider(String queryProCode, String queryProName, String pageIndex, Model model) {
        int pageSize = Constant.PAGESIZE;

        queryProName = queryProName == null ? "" : queryProName;
        int currentPageNo = pageIndex == null ? 1 : Integer.parseInt(pageIndex);

        int totalCount = providerService.getProviderCount(queryProName, queryProCode);

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

        List<Provider> providerList = providerService.getProviderListByCodeAndName(queryProName, queryProCode, currentPageNo, pageSize);

        model.addAttribute("providerList", providerList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("queryUserName", queryProName);
        model.addAttribute("queryUserCode", queryProCode);

        return "providerlist";
    }

    @RequestMapping(params = {"method=view"})
    private String getProviderView(String proid, String method, Model model) {
        if (!StringUtils.isNullOrEmpty(proid)) {
            Provider provider = providerService.getProviderById(Integer.parseInt(proid));
            model.addAttribute("provider", provider);
        }
        return "provider" + method;
    }

    @RequestMapping(params = {"method=modify"})
    private String modifyView(String proid, String method, Model model) {
        return getProviderView(proid, method, model);
    }

    @RequestMapping(params = "delprovider")
    @ResponseBody
    public String deleteProvider(String proid) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (!StringUtils.isNullOrEmpty("proid")) {
            ProviderService providerService = new ProviderServiceImpl();
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
        } else
            resultMap.put("delResult", false);
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping(params = "method=modifyexe")
    public String modifyProvider(Provider provider, HttpSession session) {
        provider.setModifyBy(((User) session.getAttribute(Constant.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        boolean isUpdate = providerService.updateProvider(provider);
        if (isUpdate) {
            return "redirect:/jsp/provider.do?method=query";
        } else {
            return "providermodify";
        }
    }


    @RequestMapping(params = "method=add")
    private String addProvider(Provider provider, HttpSession session) {
        provider.setCreatedBy(((User) session.getAttribute(Constant.USER_SESSION)).getId());
        provider.setCreationDate(new Date());

        boolean isAdd = providerService.addProvider(provider);
        if (isAdd) {
            return "redirect:/jsp/provider.do?method=query";
        }
        return "";
    }

    @RequestMapping(params = "method=getproviderlist",
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public String getProviderList(String proCode, String proName) {
        List<Provider> list = providerService.getProviderListByCodeAndName(proCode, proName, 0, Integer.MAX_VALUE - 1);
        return JSONArray.toJSONString(list);
    }

}
