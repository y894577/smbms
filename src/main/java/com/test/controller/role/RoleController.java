package com.test.controller.role;

import com.alibaba.fastjson.JSONArray;
import com.test.pojo.Role;
import com.test.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/jsp/role.do")
public class RoleController {
    @Autowired
    @Qualifier("RoleServiceImpl")
    private RoleService roleService;

    @RequestMapping(params = "method=getrolelist",
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    private String getRoleList() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Set<Role> roleList = roleService.getRoleList();
        return JSONArray.toJSONString(roleList);
    }
}
