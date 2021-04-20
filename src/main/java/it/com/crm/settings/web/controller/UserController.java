package it.com.crm.settings.web.controller;

import it.com.crm.settings.domain.User;
import it.com.crm.settings.service.UserService;
import it.com.crm.settings.service.impl.UserServiceImpl;
import it.com.crm.utils.MD5Util;
import it.com.crm.utils.PrintJson;
import it.com.crm.utils.ServiceFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到用户控制器");
        //获取请求的路径
        String path = request.getServletPath();
        //进行判断，如果路径正确则
        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if ("/settings/user/zzz.do".equals(path)){
            System.out.println("路径不正确");
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("对用户的账号密码进行验证");
        //首先获取用户输入的账号和密码,是同伙Ajax传到后台的
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码转换程MD5的形式
        loginPwd= MD5Util.getMD5(loginPwd);
        //接收浏览器的IP地址
        String IP = request.getRemoteAddr();
        System.out.println("ip："+IP);
        //调用service里面验证账号密码登录的方法
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            User user=service.login(loginAct,loginPwd,IP);
            request.getSession().setAttribute("user",user);
            //表示登录成功
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();
            //表示登录失败,或者提示信息
            String msg = e.getMessage();
            //然后响应到前端
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }

    }
}
