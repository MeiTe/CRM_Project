package it.com.crm.workbench.service;

import it.com.crm.workbench.domain.Activity;
import it.com.crm.vo.PaginationVo;
import it.com.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Boolean activitySave(Activity activity);

    PaginationVo<Activity> activityList(Map<String, Object> map);

    Boolean delete(String[] ids);

    Activity findActivityById(String id);

    Boolean updateActivity(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    Boolean deleteRemarkInfoById(String remarkId);

    boolean saveRemark(ActivityRemark activityRemark);

    Boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> showActivityListByClueId(String clueId);

    List<Activity> findActivityByAName(Map<String, String> map);
}
