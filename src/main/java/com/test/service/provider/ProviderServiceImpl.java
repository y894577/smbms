package com.test.service.provider;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import com.test.dao.bill.BillDao;
import com.test.dao.provider.ProviderDao;
import com.test.pojo.Provider;
import com.test.util.Constant;
import com.test.util.PageSupport;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderServiceImpl implements ProviderService {
    private SqlSessionTemplate sqlSession = null;
    private ProviderDao providerMapper = null;
    private BillDao billMapper = null;

    public ProviderServiceImpl() {
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
        this.providerMapper = sqlSession.getMapper(ProviderDao.class);
        this.billMapper = sqlSession.getMapper(BillDao.class);
    }


    public Map<String, Object> getProviderListByCodeAndName(String proCode, String proName, int currentPageNo, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("queryProName", proName);
        result.put("queryProCode", proCode);

        if (!StringUtils.isNullOrEmpty(proCode)) {
            proCode = "%" + proCode + "%";
        }
        if (!StringUtils.isNullOrEmpty(proName)) {
            proName = "%" + proName + "%";
        }


        final String finalProCode = proCode;
        final String finalProName = proName;
        PageInfo<Provider> pageInfo = PageHelper.startPage(0, Integer.MAX_VALUE - 1).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                providerMapper.getProviderListByCodeAndName(finalProCode, finalProName);
            }
        });

        //获取总条数
        int totalCount = (int) pageInfo.getTotal();
        //获取总页数
        int totalPageCount = (int) Math.ceil(totalCount * 1.0 / Constant.PAGESIZE);
        currentPageNo = totalCount == 0 ? 0 : currentPageNo;

        pageInfo = PageHelper.startPage(currentPageNo, pageSize).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                providerMapper.getProviderListByCodeAndName(finalProCode, finalProName);
            }
        });


        result.put("providerList", pageInfo.getList());
        result.put("totalCount", totalCount);
        result.put("currentPageNo", currentPageNo);
        result.put("totalPageCount", totalPageCount);


        return result;
    }

    public Provider getProviderById(int id) {
        Provider provider = providerMapper.getProviderById(id);
        return provider;
    }

    public boolean updateProvider(Provider provider) {
        int i = providerMapper.updateProvider(provider);
        return i > 0;
    }

    /**
     * @param proid
     * @return -1->fail , 0->success , other number->billCount
     */
    public int deleteProvider(int proid) {
        int billCount = billMapper.getBillCountByProId(proid);
        if (billCount == 0) {
            int i = providerMapper.deleteProvider(proid);
            if (i > 0) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return billCount;
        }

    }

    public boolean addProvider(Provider provider) {
        int i = providerMapper.addProvider(provider);
        return i > 0;
    }

    public int getProviderCount(String queryProName, String queryProCode) {
        return providerMapper.getProviderCount(queryProName, queryProCode);
    }

}
