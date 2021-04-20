package it.com.crm.workbench.dao;

import it.com.crm.workbench.domain.Activity;
import it.com.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> dataListActivity(Map<String, Object> map);

    int dataListActivityCount(Map<String, Object> map);

    //获取删除的活动条数
    int countDeleteInActivity(String[] ids);

    Activity findActivityById(String id);

    int updateActivity(Activity activity);

    Activity detailInfo(String id);

    List<Activity> showActivityListByClueId(String clueId);

    List<Activity> findActivityByAName(Map<String, String> map);
}
