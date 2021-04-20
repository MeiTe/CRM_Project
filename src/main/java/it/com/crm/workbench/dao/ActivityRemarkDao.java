package it.com.crm.workbench.dao;

import it.com.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    //查询出需要删除的评价的条数
    int findCount(String[] ids);

    //获取删除受影响的评价条数
    int findCountByDelete(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteRemarkInfoById(String remarkId);

    int saveRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark activityRemark);
}
