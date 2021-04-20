package it.com.crm.settings.service;

import it.com.crm.exception.LoginException;
import it.com.crm.settings.domain.User;

import java.util.List;

public interface UserService {   //双击完鼠标，双击shift。找到UserServiceImpl

    //用户登录的方法
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
