package it.com.crm.web.listener;

import it.com.crm.settings.domain.DicValue;
import it.com.crm.settings.service.DicService;
import it.com.crm.settings.service.impl.DicServiceImpl;
import it.com.crm.utils.ServiceFactory;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("服务器处理数据字典缓存开始······");
         ServletContext application = event.getServletContext();
         DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
         //保存查询到的不同类型
         Map<String, List<DicValue>> map = dicService.getAll();
         //将map解析为上下文域对象中保存的键值对
         Set<String> keySet = map.keySet();
         for (String key : keySet) {
             application.setAttribute(key,map.get(key));
         }
        System.out.println(map);
        System.out.println("服务器处理数据字典缓存结束······");
     }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("contextDestroyed");
    }
}
