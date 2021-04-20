package it.com.crm.settings.service.impl;

import it.com.crm.settings.dao.DicTypeDao;
import it.com.crm.settings.dao.DicValueDao;
import it.com.crm.settings.domain.DicType;
import it.com.crm.settings.domain.DicValue;
import it.com.crm.settings.service.DicService;
import it.com.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map = new HashMap<String, List<DicValue>>();
        //将字典类型列表取出
        List<DicType> dicTypeList=dicTypeDao.getTypeList();
        //遍历每一个字典，获取编码。然后根据编码获取DicValue里面的值
        for (DicType dicType : dicTypeList) {
            String code = dicType.getCode();
            //然后调用dicValueDao里面根据编码查询值的方法
            List<DicValue> dicValueList=dicValueDao.getValueByCode(code);
            //保存在Map中
            map.put(code+"List",dicValueList);
        }
        return map;
    }
}
