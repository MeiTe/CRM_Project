package it.com.crm.workbench.service.impl;

import it.com.crm.utils.SqlSessionUtil;
import it.com.crm.workbench.dao.ActivityDao;
import it.com.crm.workbench.dao.ActivityRemarkDao;
import it.com.crm.workbench.domain.Activity;
import it.com.crm.workbench.domain.ActivityRemark;
import it.com.crm.workbench.service.ActivityService;
import it.com.crm.vo.PaginationVo;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    @Override
    public Boolean activitySave(Activity activity) {
        boolean flag = true;
        //条用dao中保存活动的方法
        int count=activityDao.save(activity);
        if (count!=1){
            flag=false;
        }
        return flag;

    }

    @Override
    public PaginationVo<Activity> activityList(Map<String, Object> map) {
        //取得活动列表
        List<Activity> activityList=activityDao.dataListActivity(map);
        //获取满足条件的总数量
        int count=activityDao.dataListActivityCount(map);
        //然后封装在PaginationVo中
        PaginationVo<Activity> paginationVo = new PaginationVo<Activity>();
        paginationVo.setDataList(activityList);
        paginationVo.setTotal(count);
        //最后进行返回
        return paginationVo;
    }

    @Override
    public Boolean delete(String[] ids) {
        Boolean flag=true;
        //查询出需要删除的评价的条数
        int findCount=activityRemarkDao.findCount(ids);
        //获取删除受影响的评价条数
        int findCountByDelete=activityRemarkDao.findCountByDelete(ids);
        //查询出来的和受影响的条数进行比较，相同则返回true，不同则返回false
        if (findCount!=findCountByDelete){
            flag=false;
        }
        //获取删除的活动条数
        int countDeleteInActivity=activityDao.countDeleteInActivity(ids);
        //删除的活动条数和数组的长度进行比较，如果不一样则返回false
        if (countDeleteInActivity!=ids.length){
            flag=false;
        }
        return flag;
    }

    @Override
    public Activity findActivityById(String id) {
        //根据ID查询活动信息的单条
        return activityDao.findActivityById(id);
    }

    @Override
    public Boolean updateActivity(Activity activity) {
        Boolean flag=true;
        //调用修改的方法
        int count=activityDao.updateActivity(activity);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        return activityDao.detailInfo(id);
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        return activityRemarkDao.getRemarkListByAid(activityId);
    }

    @Override
    public Boolean deleteRemarkInfoById(String remarkId) {
        Boolean flag = true;
        int count=activityRemarkDao.deleteRemarkInfoById(remarkId);
        //当删除成功，那么会返回受到影响的行数
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {
        Boolean flag=true;
        //调用dao里面的保存方法
        int count=activityRemarkDao.saveRemark(activityRemark);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Boolean updateRemark(ActivityRemark activityRemark) {
        Boolean flag =true;
        //调用dao里面修改备注的方法
        int count=activityRemarkDao.updateRemark(activityRemark);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public List<Activity> showActivityListByClueId(String clueId) {
        return activityDao.showActivityListByClueId(clueId);
    }

    @Override
    public List<Activity> findActivityByAName(Map<String, String> map) {
        return activityDao.findActivityByAName(map);
    }

    @Override
    public List<Activity> getActivityListByActivityName(String activityName) {
        return activityDao.getActivityListByActivityName(activityName);
    }


}
