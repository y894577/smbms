package com.test.service.provider;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import com.test.dao.bill.BillDao;
import com.test.dao.provider.ProviderDao;
import com.test.dao.provider.ProviderRedisDao;
import com.test.pojo.Provider;
import com.test.util.Constant;
import com.test.util.PageSupport;
import org.apache.ibatis.jdbc.Null;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderServiceImpl implements ProviderService {
    private SqlSessionTemplate sqlSession = null;
    private ProviderDao providerMapper = null;
    private BillDao billMapper = null;

    @Autowired
    private ProviderRedisDao providerRedisDao;

    public ProviderServiceImpl() {
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
        this.providerMapper = sqlSession.getMapper(ProviderDao.class);
        this.billMapper = sqlSession.getMapper(BillDao.class);
    }


    public Map<String, Object> getProviderListByCodeAndName(String proCode, String proName, int currentPageNo, int pageSize) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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


        // 添加redis
        String key = "pro:";

        providerRedisDao.sadd("proName", pageInfo.getList());

        providerRedisDao.hmset(pageInfo.getList());


        return result;
    }

    public Provider getProviderById(int id) {
        Provider provider;
        if (providerRedisDao.hexists(String.valueOf(id))) {
            provider = providerRedisDao.hget(String.valueOf(id));
        } else
            provider = providerMapper.getProviderById(id);
        return provider;
    }

    public boolean updateProvider(Provider provider) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int i = providerMapper.updateProvider(provider);
        boolean isUpdate = providerRedisDao.hset(provider);
        return i > 0 && isUpdate;
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
                providerRedisDao.hdel(String.valueOf(proid));
                providerRedisDao.srem(String.valueOf(proid));
                return 0;
            } else {
                return -1;
            }
        } else {
            return billCount;
        }

    }

    public boolean addProvider(Provider provider) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int i = providerMapper.addProvider(provider);
        boolean isAdd = providerRedisDao.hset(provider);
        return i > 0 && isAdd;
    }

    public int getProviderCount(String queryProName, String queryProCode) {
        return providerMapper.getProviderCount(queryProName, queryProCode);
    }

}
