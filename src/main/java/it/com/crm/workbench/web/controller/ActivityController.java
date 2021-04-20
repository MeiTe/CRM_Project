package it.com.crm.workbench.web.controller;

import it.com.crm.settings.domain.User;
import it.com.crm.settings.service.UserService;
import it.com.crm.settings.service.impl.UserServiceImpl;
import it.com.crm.utils.DateTimeUtil;
import it.com.crm.utils.PrintJson;
import it.com.crm.utils.ServiceFactory;
import it.com.crm.utils.UUIDUtil;
import it.com.crm.workbench.domain.Activity;
import it.com.crm.workbench.domain.ActivityRemark;
import it.com.crm.workbench.service.ActivityService;
import it.com.crm.workbench.service.impl.ActivityServiceImpl;
import it.com.crm.vo.PaginationVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("欢迎用户进入到活动页面！！！");
        //获取用户访问的路径
        String path = request.getServletPath();
        //然后判断路径是否允许访问
        if ("/workbench/activity/getUserList.do".equals(path)){
            //执行查询用户信息的操作
            getUserList(request,response);
        }else if ("/workbench/activity/save.do".equals(path)){
            //执行保存活动的操作
            saveActive(request,response);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            //执行分页操作
            pageList(request,response);
        }else if ("/workbench/activity/deleteActivity.do".equals(path)){
            //执行删除操作
            deleteActivity(request,response);
        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){
            //查询用户信息列表和根据市场活动ID查询单挑记录
            getUserListAndActivity(request,response);
        }else if ("/workbench/activity/update.do".equals(path)){
            //修改活动信息
            updateActivity(request,response);
        }else if ("/workbench/activity/detail.do".equals(path)){
            //查询活动的详细信息
            detail(request,response);
        }else if ("/workbench/activity/getRemarkListByAid.do".equals(path)){
            //根据活动的ID获取，备注信息
            getRemarkListByAid(request,response);
        }else if ("/workbench/activity/deleteRemarkInfoById.do".equals(path)){
            //根据备注的ID，删除备注信息
            deleteRemarkInfoById(request,response);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            //保存备注信息
            saveRemark(request,response);
        }else if ("/workbench/activity/updateRemark.do".equals(path)){
            //修改备注信息
            updateRemark(request,response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        //修改备注信息
        System.out.println("修改备注信息");
        //首先获取页面传过来的备注ID值，和修改的内容值
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        //获取修改人，和修改的事件
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        //是否修改的标识改为1
        String editFlag="1";
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditTime(editTime);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditFlag(editFlag);
        //调用service里面修改备注的方法
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=activityService.updateRemark(activityRemark);
        //封装到map里面
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("remarkInfo",activityRemark);
        //以json的格式传到前台
        PrintJson.printJsonObj(response,map);

    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("保存备注信息！！！");
        //获取用户输入的备注信息
        String noteContent = request.getParameter("noteContent");
        //获取活动的ID
        String activityId = request.getParameter("activityId");
        //获取当前的创建时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人，为当前系统登录的人
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        //自动生成一个ID
        String id = UUIDUtil.getUUID();
        String editFlag = "0";
        ActivityRemark activityRemark = new ActivityRemark();
        //进行数据的封装
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(createTime);
        activityRemark.setCreateBy(createBy);
        activityRemark.setEditFlag(editFlag);
        activityRemark.setActivityId(activityId);
        //然后调用service里面保存备注信息的方法
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=activityService.saveRemark(activityRemark);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("activityRemark",activityRemark);
        PrintJson.printJsonObj(response,map);

    }

    private void deleteRemarkInfoById(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据备注的ID，删除备注信息");
        //首先获取备注的ID
        String remarkId = request.getParameter("remarkId");
        //然后调用service里面
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=activityService.deleteRemarkInfoById(remarkId);
        PrintJson.printJsonFlag(response,flag);
    }

    //根据活动的ID获取，备注信息
    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据活动的ID获取，备注信息。。。");
        //首先获取，活动的ID
        String activityId = request.getParameter("activityId");
        System.out.println(activityId);
        //调用service里面的方法
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> activityRemarkList=activityService.getRemarkListByAid(activityId);
        //然后返回到前台
        PrintJson.printJsonObj(response,activityRemarkList);
    }

    //查询活动的详细信息
    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        //调用Service里面的方法
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activityDetail=activityService.detail(id);
        request.setAttribute("activityDetail",activityDetail);
        //转发
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);
    }

    private void updateActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("对活动信息进行修改！！！");
        //修改活动信息，首先获取前端用户输入的信息
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //获得编辑的时间，和编辑的人
        String editDate = DateTimeUtil.getSysTime();
        String editName = ((User) request.getSession().getAttribute("user")).getName();
        //然后把其封装在Activity中
        Activity activity =new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editDate);
        activity.setEditBy(editName);
        //然后调用service里面的修改方法
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=activityService.updateActivity(activity);
        //然后返回到前台
        PrintJson.printJsonFlag(response,flag);

    }


    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询用户信息列表和根据市场活动ID查询单挑记录");
        //首先查询用户列表
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = userService.getUserList();
        //然后根据Id查询单条活动的信息,首先获得ID
        String id = request.getParameter("id");
        //然后调用service里面查单条的方法
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activityInfo=activityService.findActivityById(id);
        //因为要以{"uList":[{用户1},{用户2},{用户3}],"activityInfo":{市场活动}}形式呈现，所以封装在map中
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uList",uList);
        map.put("activityInfo",activityInfo);
        //饭后返回到前台
        PrintJson.printJsonObj(response,map);
    }

    //执行删除操作
    private void deleteActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到删除操作！！！");
        //获得页面传输出来的参数
        String[] ids = request.getParameterValues("id");
        //调用service方法
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=service.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    //执行分页操作
    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进行到执行分页的操作。");
        //首先获取页面的参数
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //把当前页，和显示的页数转换成int形式的
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);
        //在SQL语句中，开始查询的数值是
        int startCount = (pageNo-1)*pageSize;
        //封装在Map中
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("owner",owner);
        map.put("name",name);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("startCount",startCount);
        map.put("pageSize",pageSize);
        //调用service里面根据条件进行查询的方法
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        PaginationVo<Activity> dataList =service.activityList(map);
        PrintJson.printJsonObj(response,dataList);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取用户信息！！！");
        //调用service里面的方法
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList=service.getUserList();
        //封装成json返回到前台
        PrintJson.printJsonObj(response,userList);
    }

    //执行保存活动的操作
    private void saveActive(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("保存活动的信息");
        //首先获取，页面中传过来的值
        String id= UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        //获取当前用的姓名，从session中获取
        String createBy =((User)request.getSession().getAttribute("user")).getName();
        //然后封装到Active当中
        Activity activity = new Activity();
        activity.setStartDate(startDate);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setEndDate(endDate);
        activity.setId(id);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        //然后调用service里面的方法
        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=service.activitySave(activity);
        //封装好返回到前台
        PrintJson.printJsonFlag(response,flag);
    }
}
