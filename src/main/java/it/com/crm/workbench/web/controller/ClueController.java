package it.com.crm.workbench.web.controller;

import it.com.crm.settings.domain.User;
import it.com.crm.settings.service.UserService;
import it.com.crm.settings.service.impl.UserServiceImpl;
import it.com.crm.utils.DateTimeUtil;
import it.com.crm.utils.PrintJson;
import it.com.crm.utils.ServiceFactory;
import it.com.crm.utils.UUIDUtil;
import it.com.crm.workbench.domain.Activity;
import it.com.crm.workbench.domain.Clue;
import it.com.crm.workbench.domain.ClueActivityRelation;
import it.com.crm.workbench.service.ActivityService;
import it.com.crm.workbench.service.ClueService;
import it.com.crm.workbench.service.impl.ActivityServiceImpl;
import it.com.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("欢迎进行到线索模块！！！");
        //获得访问的地址
        String path = request.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/clue/saveClue.do".equals(path)){
            saveClue(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/clue/showActivityList.do".equals(path)){
            showActivityList(request,response);
        }else if ("/workbench/clue/deleteByIdInRelation.do".equals(path)){
            deleteByIdInRelation(request,response);
        }else if ("/workbench/clue/findActivityByAName.do".equals(path)){
            findActivityByAName(request,response);
        }else if ("/workbench/clue/bundBtnSave.do".equals(path)){
            bundBtnSave(request,response);
        }else if ("/workbench/clue/findActivityByActivityName.do".equals(path)){
            findActivityByActivityName(request,response);
        }else if ("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("转换");
        //获取线索的ID
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");
        if ("a".equals(flag)){
            System.out.println("需要提交表单。");
        }
    }

    private void findActivityByActivityName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据用户的关键字模糊查询活动信息，不需要根据用户的ID排除已经绑定的活动");
        //获取用户输入的关键字
        String activityName = request.getParameter("activityName");
        //调用service里面的查询方法
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList=activityService.getActivityListByActivityName(activityName);
        //然后把活动信息的列表返回到前台
        PrintJson.printJsonObj(response,activityList);
    }

    private void bundBtnSave(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("关联线索信息和活动信息！！！");
        //获取前台的参数
        String clueId = request.getParameter("cid");
        String[] activityIds = request.getParameterValues("aid");
        //然后调用service里面的方法
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag=clueService.saveActivityRelation(clueId,activityIds);
        PrintJson.printJsonFlag(response,flag);

    }

    private void findActivityByAName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据活动的姓名模糊查询活动的信息！！！");
        String name = request.getParameter("name");
        String clueId = request.getParameter("clueId");
        //调用活动service里面的查询方法
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,String> map = new HashMap<String, String>();
        map.put("name",name);
        map.put("clueId",clueId);
        List<Activity> activityList=activityService.findActivityByAName(map);
        PrintJson.printJsonObj(response,activityList);
    }

    private void deleteByIdInRelation(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据ID删除div");
        //首先获取ID
        String id = request.getParameter("id");
        //调用删除的方法
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag=clueService.deleteByIdInRelation(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void showActivityList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据潜在用户的ID，获取市场活动信息");
        //首先获取潜在用户的ID
        String clueId = request.getParameter("id");
        //首先根据clueId,查询出activity信息
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList=activityService.showActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,activityList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("根据ID查询单条clue信息");
        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue=clueService.findOneByID(id);
        //返回到前台
        request.setAttribute("clue",clue);
        //转发
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("保存一条clue信息");
        String fullname= request.getParameter("fullname");
        String appellation= request.getParameter("appellation");
        String owner= request.getParameter("owner");
        String company= request.getParameter("company");
        String job= request.getParameter("job");
        String email= request.getParameter("email");
        String phone= request.getParameter("phone");
        String website= request.getParameter("website");
        String mphone= request.getParameter("mphone");
        String state= request.getParameter("state");
        String source= request.getParameter("source");
        String createBy= ((User)request.getSession().getAttribute("user")).getName();
        String createTime= DateTimeUtil.getSysTime();
        String description= request.getParameter("description");
        String contactSummary= request.getParameter("contactSummary");
        String nextContactTime= request.getParameter("nextContactTime");
        String address= request.getParameter("address");
        String id = UUIDUtil.getUUID();
        Clue clue = new Clue();
        clue.setWebsite(website);
        clue.setState(state);
        clue.setSource(source);
        clue.setPhone(phone);
        clue.setOwner(owner);
        clue.setNextContactTime(nextContactTime);
        clue.setMphone(mphone);
        clue.setJob(job);
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setEmail(email);
        clue.setDescription(description);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        clue.setContactSummary(contactSummary);
        clue.setCompany(company);
        clue.setAppellation(appellation);
        clue.setAddress(address);
        //调用service里面的
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag=clueService.saveClue(clue);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询出所有用户的姓名");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        PrintJson.printJsonObj(response,userList);
    }
}
