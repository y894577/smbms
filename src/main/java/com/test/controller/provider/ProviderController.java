package com.test.controller.provider;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.test.pojo.Provider;
import com.test.pojo.User;
import com.test.service.provider.ProviderService;
import com.test.service.provider.ProviderServiceImpl;
import com.test.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private String queryProvider(String queryProCode, String queryProName, Model model) {
        List<Provider> providerList = providerService.getProviderListByCodeAndName(queryProCode, queryProName);
        model.addAttribute("providerList", providerList);
        return "providerlist";
    }

    @RequestMapping(params = {"method=view", "method=modify"})
    private String getProviderView(String proid, String method, Model model) {
        if (!StringUtils.isNullOrEmpty(proid)) {
            Provider provider = providerService.getProviderById(Integer.parseInt(proid));
            model.addAttribute("provider", provider);
        }
        return "provider" + method;
    }

    @RequestMapping(params = "delprovider")
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
    public String modifyProvider(Provider provider, HttpSession session){
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
    private String addProvider(Provider provider,HttpSession session) {
        provider.setCreatedBy(((User) session.getAttribute(Constant.USER_SESSION)).getId());
        provider.setCreationDate(new Date());

        boolean isAdd = providerService.addProvider(provider);
        if (isAdd) {
            return "redirect:/jsp/provider.do?method=query";
        }
        return "";
    }

    @RequestMapping(params = "method=getproviderlist")
    private String getProviderList(String proCode,String proName) {
        List<Provider> list = providerService.getProviderListByCodeAndName(proCode, proName);
        return JSONArray.toJSONString(list);
    }

}
