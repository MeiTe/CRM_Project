package it.com.crm.settings.service.impl;

import it.com.crm.exception.LoginException;
import it.com.crm.settings.dao.UserDao;
import it.com.crm.settings.domain.User;
import it.com.crm.settings.service.UserService;
import it.com.crm.utils.DateTimeUtil;
import it.com.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        //把账号密码传入
        Map<String,String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //调用DAO里面的登录方法
        User user=userDao.login(map);
        //然后判断user是否存在
        if (user==null){
            throw new LoginException("账号或者密码错误");
        }
        //如果账号和密码正确，那么进行其他的判断
        //验证账号的登录时间是否已经失效。首先获取数据库中的失效时间，然后和当前时间进行对比
        String expireTime = user.getExpireTime();
        String nowDate=DateTimeUtil.getSysTime();
        if (expireTime.compareTo(nowDate)<0){
            throw new LoginException("账号已经过期。");
        }
        //判断账号是否锁定，0表示账号已经锁定，1表示账号没有锁定。
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw  new LoginException("此账号已经锁定，无法使用！");
        }
        //判断账号使用的IP地址，是否在可访问网站的列表中。
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("您的IP地址无法进行访问，没有权限！");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        //查询用户的所有信息
        return userDao.findUserList();
    }
}
